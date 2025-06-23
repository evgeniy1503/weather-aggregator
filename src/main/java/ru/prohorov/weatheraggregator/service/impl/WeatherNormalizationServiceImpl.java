package ru.prohorov.weatheraggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.mapper.NormalizedWeatherDataMapper;
import ru.prohorov.weatheraggregator.mapper.WeatherDataMapper;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;
import ru.prohorov.weatheraggregator.repository.NormalizedWeatherRepository;
import ru.prohorov.weatheraggregator.service.WeatherNormalizationService;

/**
 * Реализация {@link WeatherNormalizationService}
 */

@RequiredArgsConstructor
@Service
public class WeatherNormalizationServiceImpl implements WeatherNormalizationService {

    private final NormalizedWeatherRepository normalizedWeatherRepository;
    private final NormalizedWeatherDataMapper normalizedWeatherDataMapper;
    private final WeatherDataMapper weatherDataMapper;

    @Override
    public WeatherData normalizeWeatherData(final SourceWeatherResponse sourceResponse) {
        NormalizedWeatherData normalizedData = normalizedWeatherDataMapper.toNormalizedWeatherData(sourceResponse);
        normalizedWeatherRepository.save(normalizedData);
        return weatherDataMapper.map(normalizedData);
    }
}
