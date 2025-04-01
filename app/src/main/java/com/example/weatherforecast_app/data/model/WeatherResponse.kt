package com.example.weatherforecast_app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val city: City,
    @SerializedName("cnt")
    val count: Int,
    @SerializedName("dt")
    val dateTime: Int,
    @SerializedName("cod")
    val statusCode: String,
    @SerializedName("list")
    val weatherDTOList: List<WeatherDTO>,
    val message: Int
)

data class City(
    @SerializedName("coord")
    val coordinate: Coordinate,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)