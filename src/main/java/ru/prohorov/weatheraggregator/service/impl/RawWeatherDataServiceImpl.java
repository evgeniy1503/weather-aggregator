package ru.prohorov.weatheraggregator.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prohorov.weatheraggregator.model.RawWeatherData;
import ru.prohorov.weatheraggregator.repository.RawWeatherRepository;
import ru.prohorov.weatheraggregator.service.RawWeatherDataService;

/**
 * Реализация сервиса {@link RawWeatherDataService}
 */

@RequiredArgsConstructor
@Service
public class RawWeatherDataServiceImpl implements RawWeatherDataService {

    private final RawWeatherRepository rawWeatherRepository;

    @Transactional
    @Override
    public void saveRawWeatherData(final RawWeatherData rawWeatherData) {
        rawWeatherRepository.save(rawWeatherData);
    }

    @Transactional(readOnly = true)
    @Override
    public RawWeatherData getRawWeatherData(final Long id) {
        return rawWeatherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Raw weather data not found"));
    }
}
