package ru.prohorov.weatheraggregator.integration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.prohorov.weatheraggregator.controller.WeatherController;
import ru.prohorov.weatheraggregator.dto.response.AggregatedWeatherResponse;
import ru.prohorov.weatheraggregator.service.WeatherAggregationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherAggregationService aggregationService;

    @Test
    void getAggregatedWeather_ReturnsAverages() throws Exception {
        Mockito.when(aggregationService.aggregateWeatherData())
                .thenReturn(new AggregatedWeatherResponse(22.5, 65.0));

        mockMvc.perform(get("/api/v1/weather/aggregate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTemperature").value(22.5))
                .andExpect(jsonPath("$.averageHumidity").value(65.0));
    }
}