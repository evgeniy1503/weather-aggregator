package ru.prohorov.weatheraggregator.integration;

import ru.prohorov.weatheraggregator.model.NormalizedWeatherData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.prohorov.weatheraggregator.repository.NormalizedWeatherRepository;
import ru.prohorov.weatheraggregator.utils.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NormalizedWeatherRepositoryTest {

    @Autowired
    private NormalizedWeatherRepository repository;

    @Test
    void save_ShouldPersistData() {
        NormalizedWeatherData data = Utils.createNormalizedWeatherData(25.5, 70.0);

        NormalizedWeatherData saved = repository.save(data);

        assertNotNull(saved.getId());
        assertEquals(25.5, repository.findById(saved.getId()).get().getTemperature());
    }
}