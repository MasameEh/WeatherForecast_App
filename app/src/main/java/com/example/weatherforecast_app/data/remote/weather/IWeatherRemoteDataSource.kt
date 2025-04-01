package com.example.weatherforecast_app.data.remote.weather

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


interface IWeatherRemoteDataSource {
     fun getWeatherForFiveDays(
         latitude: Double,
         longitude: Double,
         language: String,
         tempUnit: String?,
    ): Flow<WeatherResponse>

     fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        tempUnit: String?,
    ): Flow<WeatherDTO>
}