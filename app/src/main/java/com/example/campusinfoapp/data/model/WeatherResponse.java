package com.example.campusinfoapp.data.model;

public class WeatherResponse {
    private int error_code;
    private String reason;
    private Result result;

    public int getError_code() { return error_code; }
    public String getReason() { return reason; }
    public Result getResult() { return result; }

    public static class Result {
        private String city;
        private Realtime realtime;

        public String getCity() { return city; }
        public Realtime getRealtime() { return realtime; }
    }

    public static class Realtime {
        private String temperature;
        private String info;

        public String getTemperature() { return temperature; }
        public String getInfo() { return info; }
    }
}
