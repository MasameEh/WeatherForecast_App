package com.example.weatherforecast_app.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "location",
    primaryKeys = ["country", "city"]
)
data class LocationInfo(
    val country: String,
    val city: String,
    @SerializedName("lat") var latitude: Double,
    @SerializedName("lon")var longitude: Double,
    @SerializedName("display_name") val displayName: String? = ""
)
