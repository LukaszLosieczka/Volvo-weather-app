package com.example.backend.service;

import com.example.backend.model.CurrentWeather;
import com.example.backend.model.WeatherForecast;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private static final String WEATHER_URL = "http://api.weatherapi.com/v1/";
    @Value("${api.weatherapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherServiceImpl(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper){
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public CurrentWeather getWeatherToday(String city){
        String uri = WEATHER_URL + "current.json?key=" + apiKey + "&q=" + city + "&aqi=no";
        ResponseEntity<String> result;
        try {
            result = restTemplate.getForEntity(uri, String.class);
        }catch(RestClientException e){
            log.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        return convertToCurrentWeather(result);
    }

    public List<WeatherForecast> getWeatherForecast(String city, int days){
        String uri = WEATHER_URL + "forecast.json?key=" + apiKey + "&q=" + city + "&days=" + days + "&aqi=no&alerts=no";
        ResponseEntity<String> result;
        try {
            result = restTemplate.getForEntity(uri, String.class);
        }catch(RestClientException e){
            log.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        return convertToForecastWeather(result);
    }

    private CurrentWeather convertToCurrentWeather(ResponseEntity<String> response){
        try{
            JsonNode root = objectMapper.readTree(response.getBody());
            String location = root.path("location").path("name").asText();
            String icon = root.path("current").path("condition").path("icon").asText();
            double tempC = root.path("current").path("temp_c").asDouble();
            double humidity = root.path("current").path("humidity").asDouble();
            double windSpeedKph = root.path("current").path("wind_kph").asDouble();
            String windDir = root.path("current").path("wind_dir").asText();
            return new CurrentWeather(location, icon, tempC, humidity, windSpeedKph, windDir);
        }catch(JsonProcessingException e){
            log.error("Error while processing json object");
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<WeatherForecast> convertToForecastWeather(ResponseEntity<String> response){
        try{
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode daysList = root.path("forecast").path("forecastday");
            String location = root.path("location").path("name").asText();
            List<WeatherForecast> weatherForecasts = new ArrayList<>();
            for (JsonNode day: daysList){
                String date = day.path("date").asText();
                String icon = day.path("day").path("condition").path("icon").asText();
                double avgTempC = day.path("day").path("avgtemp_c").asDouble();
                double maxWindKph = day.path("day").path("maxwind_kph").asDouble();
                double avgHumidity = day.path("day").path("avghumidity").asDouble();
                weatherForecasts.add(new WeatherForecast(location, date, icon, avgTempC, maxWindKph, avgHumidity));
            }
            return weatherForecasts;
        }catch(JsonProcessingException e){
            log.error("Error while processing json object");
            throw new RuntimeException(e.getMessage());
        }
    }
}
