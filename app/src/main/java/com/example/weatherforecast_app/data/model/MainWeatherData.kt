package com.example.weatherforecast_app.data.model

import android.health.connect.datatypes.units.Temperature
import com.google.gson.annotations.SerializedName

data class MainWeatherData(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    @SerializedName("temp")
    val temperature: Double, // Kelvin
    val temp_max: Double,
    val temp_min: Double
)