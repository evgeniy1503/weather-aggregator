package ru.prohorov.weatheraggregator.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.mapper.RawWeatherMapper;
import ru.prohorov.weatheraggregator.model.RawWeatherData;
import ru.prohorov.weatheraggregator.service.ExternalWeatherService;
import ru.prohorov.weatheraggregator.service.RawWeatherDataService;
import ru.prohorov.weatheraggregator.util.Constants;

import java.util.concurrent.CompletableFuture;

/**
 * Заглушка для {@link ExternalWeatherService}
 */

@RequiredArgsConstructor
@Service
public class ExternalWeatherServiceMock implements ExternalWeatherService {

    private static final String MOCK_SOURCE_URL = "http://localhost:8080/source/";

    private final RestTemplate restTemplate;
    private final RawWeatherMapper rawWeatherMapper;
    private final RawWeatherDataService rawWeatherDataService;
    private final ObjectMapper objectMapper;

    @Async(Constants.BeanNames.WEATHER_TASK_EXECUTOR)
    @Override
    public CompletableFuture<SourceWeatherResponse> fetchWeather(final int sourceId) {

        final String sourceUrl = MOCK_SOURCE_URL + sourceId;
        final String response = restTemplate.getForObject(sourceUrl, String.class);

        RawWeatherData rawWeatherData = rawWeatherMapper.toRawWeatherData(sourceId, response);
        rawWeatherDataService.saveRawWeatherData(rawWeatherData);

        try {
            SourceWeatherResponse weatherResponse = objectMapper.readValue(response, SourceWeatherResponse.class);
            weatherResponse.setSourceId(sourceId);
            return CompletableFuture.completedFuture(weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse weather data from source " + sourceId, e);
        }
    }
}
