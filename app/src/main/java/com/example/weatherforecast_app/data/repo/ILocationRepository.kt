package com.example.weatherforecast_app.data.repo

import android.location.Location

interface ILocationRepository {
    fun getFreshLocation(onLocationReceived: (Location?) -> Unit)
}