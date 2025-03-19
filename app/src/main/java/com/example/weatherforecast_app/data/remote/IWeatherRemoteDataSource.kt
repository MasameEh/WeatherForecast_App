package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.Query

interface IWeatherRemoteDataSource {
    suspend fun getWeatherForFiveDays(latitude: Double,
                                      longitude: Double,
                                      language: String): Response<WeatherResponse>
}