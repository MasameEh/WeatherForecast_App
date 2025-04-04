package com.example.weatherforecast_app.data.remote.location

import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.model.LocationResponse
import kotlinx.coroutines.flow.Flow

interface ILocationRemoteDataSource {

    fun searchLocationByName(query: String): Flow<List<LocationInfo>>

     fun searchLocationByCoordinate(
        latitude: Double,
        longitude: Double,
        language: String = "en"
    ): Flow<LocationResponse>
}