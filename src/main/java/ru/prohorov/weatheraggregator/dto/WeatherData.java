package ru.prohorov.weatheraggregator.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Единый нормализованный формат.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

    private double temperature;
    private double humidity;

}
