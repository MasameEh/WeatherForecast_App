package com.example.weatherforecast_app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val city: City,
    @SerializedName("cnt")
    val count: Int,
    @SerializedName("cod")
    val statusCode: String,
    @SerializedName("list")
    val weatherDataList: List<WeatherData>,
    val message: Int
)

