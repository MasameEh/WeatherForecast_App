package com.example.weatherforecast_app


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

}