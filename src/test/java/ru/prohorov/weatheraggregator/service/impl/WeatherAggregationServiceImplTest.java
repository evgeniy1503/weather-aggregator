package ru.prohorov.weatheraggregator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.AggregatedWeatherResponse;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.service.ExternalWeatherService;
import ru.prohorov.weatheraggregator.service.WeatherNormalizationService;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WeatherAggregationServiceImplTest {

    @Mock
    private ExternalWeatherService externalService;

    @Mock
    private WeatherNormalizationService normalizationService;

    @InjectMocks
    private WeatherAggregationServiceImpl service;

    private static final int NUMBER_OF_SOURCE = 2;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "numberOfSources", NUMBER_OF_SOURCE);
    }

    @Test
    void aggregateWeatherData_ReturnsAverages() {
        // Мокируем асинхронные вызовы
        Mockito.when(externalService.fetchWeather(Mockito.anyInt()))
                .thenReturn(CompletableFuture.completedFuture(new SourceWeatherResponse()));

        // Мокируем нормализацию
        Mockito.when(normalizationService.normalizeWeatherData(Mockito.any()))
                .thenReturn(new WeatherData(20.0, 50.0))
                .thenReturn(new WeatherData(30.0, 60.0));

        AggregatedWeatherResponse result = service.aggregateWeatherData();

        assertEquals(25.0, result.getAverageTemperature()); // (20 + 30) / 2
        assertEquals(55.0, result.getAverageHumidity());    // (50 + 60) / 2
    }

}