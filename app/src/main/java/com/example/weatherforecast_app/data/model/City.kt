package com.example.weatherforecast_app.data.model

import com.google.gson.annotations.SerializedName

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