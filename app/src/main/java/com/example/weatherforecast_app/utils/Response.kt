package com.example.weatherforecast_app.utils


sealed class WeatherResponseState {
    data object Loading: WeatherResponseState()
    data class Success(val weatherData : Any): WeatherResponseState()
    data class Failure(val err :Throwable): WeatherResponseState()
}

sealed class ResponseState {
    data object Loading: ResponseState()
    data class Success(val weatherData : Any): ResponseState()
    data class Failure(val err :Throwable): ResponseState()
}