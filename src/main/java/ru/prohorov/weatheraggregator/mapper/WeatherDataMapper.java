package ru.prohorov.weatheraggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;

/**
 * Маппер для {@link WeatherData}
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeatherDataMapper {

    WeatherData map(final NormalizedWeatherData normalizedData);
}
