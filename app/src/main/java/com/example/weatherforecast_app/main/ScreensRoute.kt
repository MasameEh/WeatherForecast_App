package com.example.weatherforecast_app.main


import com.example.weatherforecast_app.data.model.Coordinate
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreensRoute {

    @Serializable
    object Home: ScreensRoute()

    @Serializable
    object Favorites: ScreensRoute()

    @Serializable
    object WeatherAlerts: ScreensRoute()

    @Serializable
    object Settings: ScreensRoute()

    @Serializable
    object Map : ScreensRoute()

    @Serializable
    data class FavoriteDetails(val latitude: Double, val longitude: Double, val city: String):  ScreensRoute()

}