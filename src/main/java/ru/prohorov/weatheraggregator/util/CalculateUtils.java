package ru.prohorov.weatheraggregator.util;

import lombok.experimental.UtilityClass;
import ru.prohorov.weatheraggregator.dto.WeatherData;

import java.util.List;
import java.util.function.ToDoubleFunction;

@UtilityClass
public class CalculateUtils {

    public static double calculateAverage(final List<WeatherData> dataList,
                                          final ToDoubleFunction<WeatherData> mapper,
                                          final double defaultValue) {
        if (dataList == null || dataList.isEmpty()) {
            return defaultValue;
        }
        return dataList.stream()
                .mapToDouble(mapper)
                .average()
                .orElse(defaultValue);
    }
}
