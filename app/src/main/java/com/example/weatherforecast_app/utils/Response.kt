package com.example.weatherforecast_app.utils




sealed class ResponseState {
    data object Loading: ResponseState()
    data class Success(val data : Any): ResponseState()
    data class Failure(val err :Throwable): ResponseState()
}