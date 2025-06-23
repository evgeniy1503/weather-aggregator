package ru.prohorov.weatheraggregator.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.mapper.NormalizedWeatherDataMapper;
import ru.prohorov.weatheraggregator.mapper.WeatherDataMapper;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;
import ru.prohorov.weatheraggregator.repository.NormalizedWeatherRepository;
import ru.prohorov.weatheraggregator.utils.Utils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherNormalizationServiceImplTest {

    @Mock
    private NormalizedWeatherRepository repository;

    @Mock
    private NormalizedWeatherDataMapper normalizedWeatherDataMapper;

    @Mock
    private WeatherDataMapper weatherDataMapper;

    @InjectMocks
    private WeatherNormalizationServiceImpl service;

    @Test
    void normalizeWeatherData_WithTempAndHum() {
        SourceWeatherResponse response = Utils.createSourceWeatherResponse(20.5, 60.0);
        NormalizedWeatherData normalizedWeatherData = Utils.createNormalizedWeatherData(20.5, 60.0);
        WeatherData weatherData = Utils.createWeatherData(20.5, 60.0);

        when(normalizedWeatherDataMapper.toNormalizedWeatherData(response))
                .thenReturn(normalizedWeatherData);
        when(weatherDataMapper.map(normalizedWeatherData))
                .thenReturn(weatherData);


        WeatherData result = service.normalizeWeatherData(response);

        assertEquals(20.5, result.getTemperature());
        assertEquals(60.0, result.getHumidity());
        Mockito.verify(repository).save(Mockito.any(NormalizedWeatherData.class));
    }

    @Test
    void normalizeWeatherData_WithNestedWeather() {
        SourceWeatherResponse response = Utils.createSourceWeatherResponse(Map.of("t", 22.3, "h", 45.7));
        NormalizedWeatherData normalizedWeatherData = Utils.createNormalizedWeatherData(22.3, 45.7);
        WeatherData weatherData = Utils.createWeatherData(22.3, 45.7);

        when(normalizedWeatherDataMapper.toNormalizedWeatherData(response))
                .thenReturn(normalizedWeatherData);
        when(weatherDataMapper.map(normalizedWeatherData))
                .thenReturn(weatherData);

        WeatherData result = service.normalizeWeatherData(response);

        assertEquals(22.3, result.getTemperature());
        assertEquals(45.7, result.getHumidity());
    }
}