package com.example.weatherforecast_app.data.remote

import com.example.weatherforecast_app.data.model.WeatherResponse
import retrofit2.Response

class WeatherRemoteDataSourceImp : IWeatherRemoteDataSource {

    override suspend fun getWeatherForFiveDays(latitude: Double,
                                               longitude: Double,
                                               language: String): Response<WeatherResponse> {
        return RetrofitHelper.weatherService.getWeatherForFiveDays(
            latitude= latitude,
            longitude=longitude,
            language = language)
    }
}