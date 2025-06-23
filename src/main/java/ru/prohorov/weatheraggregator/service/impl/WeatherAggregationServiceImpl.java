package ru.prohorov.weatheraggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.AggregatedWeatherResponse;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.service.ExternalWeatherService;
import ru.prohorov.weatheraggregator.service.WeatherAggregationService;
import ru.prohorov.weatheraggregator.service.WeatherNormalizationService;
import ru.prohorov.weatheraggregator.util.CalculateUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * Реализация {@link WeatherAggregationService}
 */

@RequiredArgsConstructor
@Service
public class WeatherAggregationServiceImpl implements WeatherAggregationService {

    private final ExternalWeatherService externalWeatherService;
    private final WeatherNormalizationService normalizationService;

    /**
     * Количество источников
     */
    @Value("${weather-aggregator.number-of-sources:1}")
    private int numberOfSources;

    @Override
    public AggregatedWeatherResponse aggregateWeatherData() {
        List<CompletableFuture<SourceWeatherResponse>> futures = IntStream.rangeClosed(1, numberOfSources)
                .mapToObj(externalWeatherService::fetchWeather)
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<WeatherData> normalizedData = futures.stream()
                .map(CompletableFuture::join)
                .map(normalizationService::normalizeWeatherData)
                .toList();

        double avgTemp = CalculateUtils.calculateAverage(
                normalizedData,
                WeatherData::getTemperature,
                0.0
        );
        double avgHumidity = CalculateUtils.calculateAverage(
                normalizedData,
                WeatherData::getHumidity,
                0.0
        );

        return new AggregatedWeatherResponse(avgTemp, avgHumidity);
    }
}
