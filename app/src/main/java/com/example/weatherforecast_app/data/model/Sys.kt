package com.example.weatherforecast_app.data.model

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val partOfDay: String?,
    val country: String?,
    val sunrise: Long?,
    val sunset: String?
)