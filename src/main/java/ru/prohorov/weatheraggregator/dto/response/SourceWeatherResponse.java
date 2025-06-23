package ru.prohorov.weatheraggregator.dto.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Парсинг разноформатных данных от источников.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceWeatherResponse {
    private Integer sourceId;

    @JsonProperty("temp")
    private Double temp;

    @JsonProperty("hum")
    private Double hum;

    @JsonProperty("temperature")
    private String temperature;

    @JsonProperty("humidity")
    private String humidity;

    @JsonProperty("weather")
    private Map<String, Object> weather;

    private Map<String, Object> unknown;

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        unknown.put(key, value);
    }
}
