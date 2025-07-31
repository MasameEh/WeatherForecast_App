package com.example.weatherforecast_app.utils

import android.net.Uri


object Constants {
    const val WEATHER_API_KEY = "bde14ab9dcc5803b94a1fd63eb296c5d"
    const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val LOCATION_BASE_URL = "https://nominatim.openstreetmap.org/"


    const val REQUEST_CODE_NOTIFICATIONS = 1001
    const val LOCATION_REQUEST_CODE = 550


    const val AUTHORITY = "com.example.weatherapp.provider"
    const val PATH_ALERTS = "alerts"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_ALERTS")

}