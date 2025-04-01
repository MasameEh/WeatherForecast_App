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


data class MainWeatherData(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    @SerializedName("temp")
    val temperature: Double, // Kelvin
    val temp_max: Double,
    val temp_min: Double
)

data class Clouds(
    val all: Int
)

data class Coordinate(
    val lat: Double,
    val lon: Double
)

data class Sys(
    @SerializedName("pod")
    val partOfDay: String?,
    val country: String?,
    val sunrise: Long?,
    val sunset: String?
)

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)