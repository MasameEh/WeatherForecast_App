package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query

interface IWeatherRemoteDataSource {
     fun getWeatherForFiveDays(latitude: Double,
                                      longitude: Double,
                                      language: String
    ): Flow<WeatherResponse>

     fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
    ): Flow<WeatherDTO>
}