package com.example.weatherforecast_app.data.local

import com.example.weatherforecast_app.data.model.LocationInfo
import kotlinx.coroutines.flow.Flow

interface ILocationLocalDataSource {

    fun getAllFavLocations() : Flow<List<LocationInfo>>

    suspend fun insertLocation(location: LocationInfo) : Long

    suspend fun deleteLocation(location:LocationInfo): Int
}