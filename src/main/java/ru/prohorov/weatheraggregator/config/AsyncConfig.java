package ru.prohorov.weatheraggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.prohorov.weatheraggregator.config.propetries.ExecutorProperties;
import ru.prohorov.weatheraggregator.util.Constants;

import java.util.concurrent.Executor;

/**
 * Настройка пула потоков для асинхронных задач.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(Constants.BeanNames.WEATHER_TASK_EXECUTOR)
    public Executor taskExecutor(ExecutorProperties executorProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorProperties.getCorePoolSize());
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(executorProperties.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
}
