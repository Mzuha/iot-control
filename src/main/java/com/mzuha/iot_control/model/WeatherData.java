package com.mzuha.iot_control.model;

import java.io.Serializable;

public record WeatherData(Integer temperature, Integer windSpeed,
                          Integer humidity) implements Serializable {

}
