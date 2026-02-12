package com.example.campusinfoapp.data.network;

import com.example.campusinfoapp.data.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("simpleWeather/query")
    Call<WeatherResponse> getWeather(
            @Query("city") String city,
            @Query("key") String key
    );
}
