package ru.prohorov.weatheraggregator.config.propetries;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Параметры асинхронной обработки
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "executor")
public class ExecutorProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String threadNamePrefix;
}
