package com.example.weatherforecast_app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val clouds: Clouds,
    @SerializedName("dt")
    val dateTime: Int,
    @SerializedName("dt_txt")
    val date_txt: String,
    val mainWeatherData: MainWeatherData,
    val pop: Double,
    @SerializedName("sys")
    val dayTime: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)