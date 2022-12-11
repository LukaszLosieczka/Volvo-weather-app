package com.example.backend.controller;

import com.example.backend.model.CurrentWeather;
import com.example.backend.model.WeatherForecast;
import com.example.backend.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService service;

    @GetMapping("/{city}")
    ResponseEntity<Object> getCurrentWeather(@PathVariable String city){
        try {
            CurrentWeather weather = service.getWeatherToday(city);
            return new ResponseEntity<>(weather, HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/forecast/{city}")
    ResponseEntity<Object> getWeatherForecast(@PathVariable String city, @RequestParam(required = false) Integer days){
        try {
            days = days == null ? 3 : days;
            List<WeatherForecast> weather = service.getWeatherForecast(city, days);
            return new ResponseEntity<>(weather, HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
