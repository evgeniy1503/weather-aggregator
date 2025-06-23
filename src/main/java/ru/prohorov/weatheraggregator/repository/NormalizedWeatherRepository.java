package ru.prohorov.weatheraggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;

/**
 * Репозиторий для {@link NormalizedWeatherData}.
 */
@Repository
public interface NormalizedWeatherRepository extends JpaRepository<NormalizedWeatherData, Long> {
}
