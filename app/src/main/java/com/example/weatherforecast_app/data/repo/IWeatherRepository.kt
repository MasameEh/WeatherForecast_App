package com.example.weatherforecast_app.data.repo

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {

    suspend fun getWeatherForFiveDays(
        latitude: Double,
        longitude: Double,
        language: String = "en"
    ): Flow<WeatherResponse>

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en"
    ): Flow<WeatherDTO>
}