package ru.prohorov.weatheraggregator.util;

import lombok.experimental.UtilityClass;

/**
 * Константы
 */

@UtilityClass
public class Constants {

    @UtilityClass
    public class Url {
        public final String ROOT_ULR = "/weather";
        public final String AGGREGATE_URL = "/aggregate";
    }

    @UtilityClass
    public class BeanNames {
        public final String WEATHER_TASK_EXECUTOR = "weatherTaskExecutor";
    }
}
