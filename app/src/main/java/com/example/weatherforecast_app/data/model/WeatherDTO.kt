package com.example.weatherforecast_app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    val clouds: Clouds,
    @SerializedName("dt")
    val dateTime: Int,
    @SerializedName("dt_txt")
    val date_txt: String? = null,
    @SerializedName("main")
    val mainWeatherData: MainWeatherData,
    val pop: Double,
    @SerializedName("sys")
    val placeInfo: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind,
    val name: String?
)