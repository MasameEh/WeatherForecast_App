package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getWeatherForFiveDays(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en",
        @Query("appid") apiKey: String = Constants.API_KEY,
        ): Response<WeatherResponse>
}