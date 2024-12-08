package com.mzuha.iot_control.controller;

import com.mzuha.iot_control.model.WeatherData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final KafkaTemplate<String, WeatherData> kafkaTemplate;

    public WeatherController(KafkaTemplate<String, WeatherData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/random")
    public List<WeatherData> getRandomWeatherData() {
        var weatherDataList = new ArrayList<WeatherData>();

        Random random = new Random();
        var randomWeatherCount = random.nextInt(2, 7);

        for (int i = 0; i < randomWeatherCount; i++) {
            weatherDataList.add(new WeatherData(random.nextInt(24, 32), random.nextInt(2, 10), random.nextInt(75, 90)));
        }

        weatherDataList.forEach(weatherData -> kafkaTemplate.send("weatherTopic", weatherData));

        return weatherDataList;
    }

    @KafkaListener(topics = "weatherTopic", groupId = "weather-group-1")
    public void listedWeatherTopic(WeatherData weatherData) {
        System.out.println("RECEIVED WEATHER DATA: " + weatherData);
    }
}
