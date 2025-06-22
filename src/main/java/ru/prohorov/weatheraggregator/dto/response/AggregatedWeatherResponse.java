package ru.prohorov.weatheraggregator.dto.response;

import lombok.*;

/**
 * Ответ агрегированных данных по погоде
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedWeatherResponse {
    private double averageTemperature;
    private double averageHumidity;
}
