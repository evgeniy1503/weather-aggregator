package ru.prohorov.weatheraggregator.utils;

import ru.prohorov.weatheraggregator.dto.WeatherData;
import ru.prohorov.weatheraggregator.dto.response.SourceWeatherResponse;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;

import java.time.LocalDateTime;
import java.util.Map;

public class Utils {

    public static final Integer ID = 1;

    public static SourceWeatherResponse createSourceWeatherResponse(double temp, double hum) {
        SourceWeatherResponse response = new SourceWeatherResponse();
        response.setSourceId(ID);
        response.setTemp(temp);
        response.setHum(hum);
        return response;
    }

    public static NormalizedWeatherData createNormalizedWeatherData(double temp, double hum) {
        NormalizedWeatherData normalizedWeatherData = new NormalizedWeatherData();
        normalizedWeatherData.setHumidity(hum);
        normalizedWeatherData.setTemperature(temp);
        normalizedWeatherData.setTimestamp(LocalDateTime.now());
        return normalizedWeatherData;
    }

    public static WeatherData createWeatherData(double temp, double hum) {
        WeatherData weatherData = new WeatherData();
        weatherData.setHumidity(hum);
        weatherData.setTemperature(temp);
        return weatherData;
    }

    public static SourceWeatherResponse createSourceWeatherResponse(Map<String, Object> weather) {
        SourceWeatherResponse response = new SourceWeatherResponse();
        response.setSourceId(ID);
        response.setWeather(weather);
        return response;
    }

    public static SourceWeatherResponse createSourceWeatherResponse(String temperature, String humidity) {
        SourceWeatherResponse response = new SourceWeatherResponse();
        response.setTemperature(temperature);
        response.setHumidity(humidity);
        return response;
    }
}
