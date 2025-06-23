package ru.prohorov.weatheraggregator.service;

import ru.prohorov.weatheraggregator.model.RawWeatherData;

/**
 * Сервис для работы с {@link RawWeatherData}
 */
public interface RawWeatherDataService {

    void saveRawWeatherData(final RawWeatherData rawWeatherData);

    RawWeatherData getRawWeatherData(final Long id);
}
