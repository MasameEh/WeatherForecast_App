package com.example.weatherforecast_app


import kotlinx.serialization.Serializable

@Serializable
sealed class ScreensRoute {

    @Serializable
    object Home: ScreensRoute() {
        const val route = "home"
    }

    @Serializable
    object Favorites: ScreensRoute() {
        const val route = "favorites"
    }

    @Serializable
    object WeatherAlerts: ScreensRoute() {
        const val route = "weather_alerts"
    }

    @Serializable
    object Settings: ScreensRoute() {
        const val route = "settings"
    }

    @Serializable
    object Map : ScreensRoute() {
        const val route = "map"
    }

}