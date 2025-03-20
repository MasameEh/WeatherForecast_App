package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRemoteDataSourceImp : IWeatherRemoteDataSource {

    override suspend fun getWeatherForFiveDays(latitude: Double,
                                               longitude: Double,
                                               language: String): Flow<WeatherResponse> {
        return flow{
            val result = RetrofitHelper.weatherService.getWeatherForFiveDays(
                latitude = latitude,
                longitude = longitude,
                language = language
            )
            emit(result)
        }
    }

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String
    ): Flow<WeatherDTO> {
        return flow{
            val result = RetrofitHelper.weatherService.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
                language = language
            )
            emit(result)
        }
    }
}