package ru.prohorov.weatheraggregator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.prohorov.weatheraggregator.dto.response.AggregatedWeatherResponse;
import ru.prohorov.weatheraggregator.service.WeatherAggregationService;
import ru.prohorov.weatheraggregator.util.Constants;

/**
 * Контроллер для агрегатора погоды
 */

@RestController
@RequestMapping(Constants.Url.ROOT_ULR)
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherAggregationService weatherAggregationService;

    @GetMapping(Constants.Url.AGGREGATE_URL)
    public ResponseEntity<AggregatedWeatherResponse> getAggregatedWeather() {
        return ResponseEntity.ok(weatherAggregationService.aggregateWeatherData());
    }
}
