package com.example.weatherforecast_app.data.remote.weather

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.data.remote.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class WeatherRemoteDataSourceImp : IWeatherRemoteDataSource {

    override fun getWeatherForFiveDays(
        latitude: Double,
        longitude: Double,
        language: String,
        tempUnit: String?,
        ): Flow<WeatherResponse> {

        return flow{
            val result = RetrofitHelper.weatherService.getWeatherForFiveDays(
                latitude = latitude,
                longitude = longitude,
                language = language,
                units = tempUnit
            )
            emit(result)
        }
    }

    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        tempUnit: String?,
    ): Flow<WeatherDTO> {
        return flow{
            val result = RetrofitHelper.weatherService.getCurrentWeather(
                latitude = latitude,
                longitude = longitude,
                language = language,
                units = tempUnit
            )
            emit(result)
        }
    }
}