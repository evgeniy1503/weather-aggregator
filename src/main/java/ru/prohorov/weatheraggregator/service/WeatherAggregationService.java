package ru.prohorov.weatheraggregator.service;

import ru.prohorov.weatheraggregator.dto.response.AggregatedWeatherResponse;

/**
 * Сервис агрегации данных
 */
public interface WeatherAggregationService {

    AggregatedWeatherResponse aggregateWeatherData();
}
