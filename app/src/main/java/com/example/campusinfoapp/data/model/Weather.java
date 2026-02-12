package com.example.campusinfoapp.data.model;

public class Weather {
    private String city;
    private String description;
    private double temperature;

    // 空构造方法（用于 JSON 解析）
    public Weather() {}

    public Weather(String city, String description, double temperature) {
        this.city = city;
        this.description = description;
        this.temperature = temperature;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    @Override
    public String toString() {
        return city + ": " + description + ", " + temperature + "°C";
    }
}
