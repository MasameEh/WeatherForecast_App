package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun getWeatherForFiveDays(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String?,
        @Query("lang") language: String = "en",
        @Query("appid") apiKey: String = Constants.WEATHER_API_KEY,
        ): WeatherResponse

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String?,
        @Query("lang") language: String = "en",
        @Query("appid") apiKey: String = Constants.WEATHER_API_KEY,
    ): WeatherDTO
}