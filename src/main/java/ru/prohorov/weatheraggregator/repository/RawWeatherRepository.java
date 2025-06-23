package ru.prohorov.weatheraggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.prohorov.weatheraggregator.model.RawWeatherData;

/**
 * Репозиторий для {@link RawWeatherData}.
 */

@Repository
public interface RawWeatherRepository extends JpaRepository<RawWeatherData, Long> {
}
