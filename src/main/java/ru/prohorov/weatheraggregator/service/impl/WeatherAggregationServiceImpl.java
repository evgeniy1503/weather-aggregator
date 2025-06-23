package ru.prohorov.weatheraggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.AggregatedWeatherResponse;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.service.ExternalWeatherService;
import ru.prohorov.weatheraggregator.service.WeatherAggregationService;
import ru.prohorov.weatheraggregator.service.WeatherNormalizationService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Реализация {@link WeatherAggregationService}
 */

@RequiredArgsConstructor
@Service
public class WeatherAggregationServiceImpl implements WeatherAggregationService {

    private final ExternalWeatherService externalWeatherService;
    private final WeatherNormalizationService normalizationService;

    @Override
    public AggregatedWeatherResponse aggregateWeatherData() {
        // Create 100 requests (for source IDs 1 to 100)
        List<CompletableFuture<SourceWeatherResponse>> futures = IntStream.rangeClosed(1, 100)
                .mapToObj(externalWeatherService::fetchWeather)
                .toList();

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Process responses
        List<WeatherData> normalizedData = futures.stream()
                .map(CompletableFuture::join)
                .map(normalizationService::normalizeWeatherData)
                .toList();

        // Calculate averages
        double avgTemp = normalizedData.stream()
                .mapToDouble(WeatherData::getTemperature)
                .average()
                .orElse(0.0);

        double avgHumidity = normalizedData.stream()
                .mapToDouble(WeatherData::getHumidity)
                .average()
                .orElse(0.0);

        return new AggregatedWeatherResponse(avgTemp, avgHumidity);
    }
}
