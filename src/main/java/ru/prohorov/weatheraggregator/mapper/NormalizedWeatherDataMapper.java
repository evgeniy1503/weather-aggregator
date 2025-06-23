package ru.prohorov.weatheraggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;

/**
 * Маппер для сущности {@link NormalizedWeatherData}
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NormalizedWeatherDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "temperature", source = "sourceResponse", qualifiedByName = "mapTemperature")
    @Mapping(target = "humidity", source = "sourceResponse", qualifiedByName = "mapHumidity")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    NormalizedWeatherData toNormalizedWeatherData(final SourceWeatherResponse sourceResponse);

    @Named("mapTemperature")
    default double mapTemperature(final SourceWeatherResponse sourceResponse) {
        if (sourceResponse.getTemp() != null) {
            return sourceResponse.getTemp();
        } else if (sourceResponse.getTemperature() != null) {
            return Double.parseDouble(sourceResponse.getTemperature());
        } else if (sourceResponse.getWeather() != null) {
            return ((Number) sourceResponse.getWeather().get("t")).doubleValue();
        }
        return 0.0;
    }

    @Named("mapHumidity")
    default double mapHumidity(final SourceWeatherResponse sourceResponse) {
        if (sourceResponse.getHum() != null) {
            return sourceResponse.getHum();
        } else if (sourceResponse.getHumidity() != null) {
            return Double.parseDouble(sourceResponse.getHumidity());
        } else if (sourceResponse.getWeather() != null) {
            return ((Number) sourceResponse.getWeather().get("h")).doubleValue();
        }
        return 0.0;
    }
}
