package ru.prohorov.weatheraggregator.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Хранение обработанных данных.
 */

@Entity
@Table(name = "normalized_weather_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NormalizedWeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "source_id", nullable = false)
    private int sourceId;

    @Column(name = "temperature", nullable = false)
    private double temperature;

    @Column(name = "humidity", nullable = false)
    private double humidity;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
