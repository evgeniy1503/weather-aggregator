package ru.prohorov.weatheraggregator.service;

import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;

/**
 * Сервис по работе по нормализации данных
 */
public interface WeatherNormalizationService {

    WeatherData normalizeWeatherData(final SourceWeatherResponse sourceResponse);
}
