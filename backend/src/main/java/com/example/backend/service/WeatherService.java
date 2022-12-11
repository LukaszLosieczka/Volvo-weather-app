package com.example.backend.service;

import com.example.backend.model.CurrentWeather;
import com.example.backend.model.WeatherForecast;

import java.util.List;

public interface WeatherService {
    CurrentWeather getWeatherToday(String city);
    List<WeatherForecast> getWeatherForecast(String city, int days);
}
