package ru.prohorov.weatheraggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;
import ru.prohorov.weatheraggregator.repository.NormalizedWeatherRepository;
import ru.prohorov.weatheraggregator.service.WeatherNormalizationService;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Реализация {@link WeatherNormalizationService}
 */

@RequiredArgsConstructor
@Service
public class WeatherNormalizationServiceImpl implements WeatherNormalizationService {

    private final NormalizedWeatherRepository normalizedWeatherRepository;

    @Override
    public WeatherData normalizeWeatherData(SourceWeatherResponse sourceResponse) {
        double temperature = 0.0;
        double humidity = 0.0;

        // Handle different response formats
        if (sourceResponse.getTemp() != null) {
            temperature = sourceResponse.getTemp();
            humidity = sourceResponse.getHum();
        } else if (sourceResponse.getTemperature() != null) {
            temperature = Double.parseDouble(sourceResponse.getTemperature());
            humidity = Double.parseDouble(sourceResponse.getHumidity());
        } else if (sourceResponse.getWeather() != null) {
            Map<String, Object> weather = sourceResponse.getWeather();
            temperature = ((Number) weather.get("t")).doubleValue();
            humidity = ((Number) weather.get("h")).doubleValue();
        }

        // Save normalized data to DB
        NormalizedWeatherData normalizedData = new NormalizedWeatherData();
        normalizedData.setSourceId(sourceResponse.getSourceId());
        normalizedData.setTemperature(temperature);
        normalizedData.setHumidity(humidity);
        normalizedData.setTimestamp(LocalDateTime.now());
        normalizedWeatherRepository.save(normalizedData);

        return new WeatherData(temperature, humidity);
    }
}
