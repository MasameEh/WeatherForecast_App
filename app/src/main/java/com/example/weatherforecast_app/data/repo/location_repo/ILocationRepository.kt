package com.example.weatherforecast_app.data.repo.location_repo

import android.location.Location
import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.model.LocationResponse
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getFreshLocation(onLocationReceived: (Location?) -> Unit)

    fun getAllFavLocations() : Flow<List<LocationInfo>>

    fun searchLocationByName(query: String): Flow<List<LocationInfo>>

    fun searchLocationByCoordinate(
        latitude: Double,
        longitude: Double,
    ): Flow<LocationResponse>

    suspend fun insertLocation(location: LocationInfo) : Long

    suspend fun deleteLocation(location: LocationInfo): Int
}