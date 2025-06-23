package ru.prohorov.weatheraggregator.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.mapper.RawWeatherMapper;
import ru.prohorov.weatheraggregator.model.RawWeatherData;
import ru.prohorov.weatheraggregator.service.RawWeatherDataService;
import ru.prohorov.weatheraggregator.utils.Utils;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
class ExternalWeatherServiceMockTest {

    private static final int TEST_SOURCE_ID = 1;
    private static final String TEST_RESPONSE_1 = "{\"temp\":25.5,\"hum\":60.0}}";
    private static final String TEST_RESPONSE_2 = "{\"temperature\":\"21.7\",\"humidity\":\"58\"}";
    private static final String TEST_RESPONSE_MIXED = "{\"temp\":19.0,\"humidity\":\"45\",\"weather\":{\"t\":19.5}}";
    private static final String TEST_RESPONSE_3 = "{\"weather\":{\"t\":22.5,\"h\":53.3}}";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RawWeatherMapper rawWeatherMapper;

    @Mock
    private RawWeatherDataService rawWeatherDataService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ExternalWeatherServiceMock externalWeatherService;

    private static final String MOCK_SOURCE_URL = "http://localhost:8080/source/";

    private static final String SOURCE_URL = MOCK_SOURCE_URL + TEST_SOURCE_ID;

    @Test
    void fetchWeather_Success() throws JsonProcessingException, ExecutionException, InterruptedException {
        RawWeatherData mockRawWeatherData = new RawWeatherData();
        SourceWeatherResponse mockResponse = Utils.createSourceWeatherResponse(25.5, 60.0);

        when(restTemplate.getForObject(SOURCE_URL, String.class)).thenReturn(TEST_RESPONSE_1);
        when(rawWeatherMapper.toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_1)).thenReturn(mockRawWeatherData);
        when(objectMapper.readValue(TEST_RESPONSE_1, SourceWeatherResponse.class)).thenReturn(mockResponse);

        CompletableFuture<SourceWeatherResponse> future = externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        SourceWeatherResponse result = future.get();

        assertNotNull(result);
        assertEquals(TEST_SOURCE_ID, result.getSourceId());
        assertEquals(25.5, result.getTemp());
        assertEquals(60.0, result.getHum());

        verify(rawWeatherDataService).saveRawWeatherData(mockRawWeatherData);
        verify(restTemplate).getForObject(SOURCE_URL, String.class);
        verify(rawWeatherMapper).toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_1);
        verify(objectMapper).readValue(TEST_RESPONSE_1, SourceWeatherResponse.class);
    }

    @Test
    void fetchWeather_ParsingError() throws JsonProcessingException {
        RawWeatherData mockRawWeatherData = new RawWeatherData();

        when(restTemplate.getForObject(SOURCE_URL, String.class)).thenReturn(TEST_RESPONSE_1);
        when(rawWeatherMapper.toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_1)).thenReturn(mockRawWeatherData);
        when(objectMapper.readValue(TEST_RESPONSE_1, SourceWeatherResponse.class))
                .thenThrow(new JsonProcessingException("Parse error") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        });

        assertEquals("Failed to parse weather data from source " + TEST_SOURCE_ID, exception.getMessage());
        assertTrue(exception.getCause() instanceof JsonProcessingException);

        verify(restTemplate).getForObject(SOURCE_URL, String.class);
        verify(rawWeatherMapper).toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_1);
        verify(rawWeatherDataService).saveRawWeatherData(mockRawWeatherData);
    }

    @Test
    void fetchWeather_RestTemplateError() {
        when(restTemplate.getForObject(SOURCE_URL, String.class))
                .thenThrow(new RuntimeException("Connection error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        });

        assertEquals("Connection error", exception.getMessage());

        verify(restTemplate).getForObject(SOURCE_URL, String.class);
        verifyNoInteractions(rawWeatherMapper, rawWeatherDataService, objectMapper);
    }

    @Test
    void fetchWeather_WithUnknownFields() throws JsonProcessingException, ExecutionException, InterruptedException {

        String responseWithUnknown = "{\"temp\":20.0,\"hum\":50.0,\"unknownField\":\"value\"}";
        RawWeatherData mockRawWeatherData = new RawWeatherData();
        SourceWeatherResponse mockResponse = Utils.createSourceWeatherResponse(20.0, 50.0);
        mockResponse.setUnknown(Map.of("unknownField", "value"));

        when(restTemplate.getForObject(SOURCE_URL, String.class)).thenReturn(responseWithUnknown);
        when(rawWeatherMapper.toRawWeatherData(TEST_SOURCE_ID, responseWithUnknown)).thenReturn(mockRawWeatherData);
        when(objectMapper.readValue(responseWithUnknown, SourceWeatherResponse.class)).thenReturn(mockResponse);


        CompletableFuture<SourceWeatherResponse> future = externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        SourceWeatherResponse result = future.get();

        assertNotNull(result.getUnknown());
        assertEquals("value", result.getUnknown().get("unknownField"));
    }

    @Test
    void fetchWeather_WithTemperatureHumidityStrings() throws JsonProcessingException, ExecutionException, InterruptedException {
        // Arrange
        RawWeatherData mockRawWeatherData = new RawWeatherData();
        SourceWeatherResponse mockResponse = Utils.createSourceWeatherResponse("21.7", "58");

        when(restTemplate.getForObject(SOURCE_URL, String.class)).thenReturn(TEST_RESPONSE_2);
        when(rawWeatherMapper.toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_2)).thenReturn(mockRawWeatherData);
        when(objectMapper.readValue(TEST_RESPONSE_2, SourceWeatherResponse.class)).thenReturn(mockResponse);

        // Act
        CompletableFuture<SourceWeatherResponse> future = externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        SourceWeatherResponse result = future.get();

        // Assert
        assertEquals("21.7", result.getTemperature());
        assertEquals("58", result.getHumidity());
        assertNull(result.getTemp());
        assertNull(result.getHum());
        assertNull(result.getWeather());

        verify(restTemplate).getForObject(SOURCE_URL, String.class);
        verify(rawWeatherMapper).toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_2);
        verify(rawWeatherDataService).saveRawWeatherData(mockRawWeatherData);
    }

    @Test
    void fetchWeather_WithWeatherObjectContainingTH() throws JsonProcessingException, ExecutionException, InterruptedException {

        RawWeatherData mockRawWeatherData = new RawWeatherData();
        SourceWeatherResponse mockResponse = Utils.createSourceWeatherResponse(Map.of("t", 22.5, "h", 53.3));

        when(restTemplate.getForObject(SOURCE_URL, String.class)).thenReturn(TEST_RESPONSE_3);
        when(rawWeatherMapper.toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_3)).thenReturn(mockRawWeatherData);
        when(objectMapper.readValue(TEST_RESPONSE_3, SourceWeatherResponse.class)).thenReturn(mockResponse);

        CompletableFuture<SourceWeatherResponse> future = externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        SourceWeatherResponse result = future.get();

        assertNotNull(result.getWeather());
        assertEquals(22.5, result.getWeather().get("t"));
        assertEquals(53.3, result.getWeather().get("h"));
        assertNull(result.getTemp());
        assertNull(result.getHum());
        assertNull(result.getTemperature());
        assertNull(result.getHumidity());

        verify(restTemplate).getForObject(SOURCE_URL, String.class);
        verify(rawWeatherMapper).toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_3);
        verify(rawWeatherDataService).saveRawWeatherData(mockRawWeatherData);
    }

    @Test
    void fetchWeather_WithMixedResponseFormat() throws JsonProcessingException, ExecutionException, InterruptedException {

        RawWeatherData mockRawWeatherData = new RawWeatherData();
        SourceWeatherResponse mockResponse = new SourceWeatherResponse();
        mockResponse.setTemp(19.0);
        mockResponse.setHumidity("45");
        mockResponse.setWeather(Map.of("t", 19.5));

        when(restTemplate.getForObject(SOURCE_URL, String.class)).thenReturn(TEST_RESPONSE_MIXED);
        when(rawWeatherMapper.toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_MIXED)).thenReturn(mockRawWeatherData);
        when(objectMapper.readValue(TEST_RESPONSE_MIXED, SourceWeatherResponse.class)).thenReturn(mockResponse);

        CompletableFuture<SourceWeatherResponse> future = externalWeatherService.fetchWeather(TEST_SOURCE_ID);
        SourceWeatherResponse result = future.get();

        assertEquals(19.0, result.getTemp());
        assertEquals("45", result.getHumidity());
        assertNotNull(result.getWeather());
        assertEquals(19.5, result.getWeather().get("t"));
        assertNull(result.getHum());
        assertNull(result.getTemperature());

        verify(restTemplate).getForObject(SOURCE_URL, String.class);
        verify(rawWeatherMapper).toRawWeatherData(TEST_SOURCE_ID, TEST_RESPONSE_MIXED);
        verify(rawWeatherDataService).saveRawWeatherData(mockRawWeatherData);
    }
}
