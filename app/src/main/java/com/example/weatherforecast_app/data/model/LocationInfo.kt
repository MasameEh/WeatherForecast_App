package com.example.weatherforecast_app.data.model

import androidx.room.Entity

@Entity(
    tableName = "location",
    primaryKeys = ["country", "city"]
)
data class LocationInfo(
    val country: String,
    val city: String,
    var latitude: Double,
    var longitude: Double
)
