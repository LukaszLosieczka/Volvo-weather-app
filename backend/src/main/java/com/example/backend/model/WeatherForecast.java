package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecast {
    private String location;
    private String date;
    private String icon;
    private double avgTempC;
    private double maxWindKph;
    private double avgHumidity;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getAvgTempC() {
        return avgTempC;
    }

    public void setAvgTempC(double avgTempC) {
        this.avgTempC = avgTempC;
    }

    public double getMaxWindKph() {
        return maxWindKph;
    }

    public void setMaxWindKph(double maxWindKph) {
        this.maxWindKph = maxWindKph;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }
}
