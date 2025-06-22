package ru.prohorov.weatheraggregator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Хранение сырых данных от источников.
 */
@Entity
@Table(name = "raw_weather_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RawWeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "source_id", nullable = false)
    private Integer sourceId;

    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    private String payload;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
