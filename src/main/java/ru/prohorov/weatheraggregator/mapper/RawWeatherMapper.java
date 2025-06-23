package ru.prohorov.weatheraggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.prohorov.weatheraggregator.model.RawWeatherData;

import java.time.LocalDateTime;

/**
 * Маппер для {@link RawWeatherData}
 */

@Mapper(componentModel = "spring")
public interface RawWeatherMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sourceId", source = "sourceId")
    @Mapping(target = "payload", source = "response")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    RawWeatherData toRawWeatherData(Integer sourceId, String response);
}
