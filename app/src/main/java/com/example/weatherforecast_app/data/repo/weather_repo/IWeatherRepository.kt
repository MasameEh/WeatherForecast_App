package com.example.weatherforecast_app.data.repo.weather_repo

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {

     fun getWeatherForFiveDays(
        latitude: Double,
        longitude: Double,
        language: String = "en" ,
        tempUnit: String?
    ): Flow<WeatherResponse>

     fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String = "en",
        tempUnit: String?
    ): Flow<WeatherDTO>
}