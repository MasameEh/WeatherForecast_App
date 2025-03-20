package com.example.weatherforecast_app

import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse


sealed class WeatherResponseState {
    data object Loading: WeatherResponseState()
    data class Success(val weatherData : WeatherResponse): WeatherResponseState()
    data class Failure(val err :Throwable): WeatherResponseState()
}

sealed class WeatherDtoResponse {
    data object Loading: WeatherDtoResponse()
    data class Success(val weatherData : WeatherDTO): WeatherDtoResponse()
    data class Failure(val err :Throwable): WeatherDtoResponse()
}