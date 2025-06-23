package ru.prohorov.weatheraggregator.service;

import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Сервис для обращения во внешнюю систему
 */
public interface ExternalWeatherService {

    CompletableFuture<SourceWeatherResponse> fetchWeather(int sourceId);
}
