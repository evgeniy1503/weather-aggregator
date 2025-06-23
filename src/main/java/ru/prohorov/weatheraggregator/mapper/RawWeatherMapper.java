package ru.prohorov.weatheraggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.prohorov.weatheraggregator.model.RawWeatherData;

import java.time.LocalDateTime;

/**
 * Маппер для {@link RawWeatherData}
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RawWeatherMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sourceId", source = "sourceId")
    @Mapping(target = "payload", source = "response")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    RawWeatherData toRawWeatherData(final Integer sourceId, final String response);
}
