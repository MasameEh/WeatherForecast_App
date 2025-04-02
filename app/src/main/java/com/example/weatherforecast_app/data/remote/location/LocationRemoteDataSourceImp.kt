package com.example.weatherforecast_app.data.remote.location

import com.example.weatherforecast_app.data.model.LocationInfo
import com.example.weatherforecast_app.data.model.LocationResponse
import com.example.weatherforecast_app.data.remote.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationRemoteDataSourceImp : ILocationRemoteDataSource {

    override fun searchLocationByName(query: String): Flow<List<LocationInfo>> {
        return flow {
            val result = RetrofitHelper.locationService.searchLocationByName(
                query = query
            )
            emit(result)
        }
    }

    override fun searchLocationByCoordinate(
        latitude: Double,
        longitude: Double,
        language: String
    ): Flow<LocationResponse> {
        return flow {
            val result = RetrofitHelper.locationService.searchLocationByCoordinate(
                latitude = latitude,
                longitude = longitude,
                language = language
            )
            emit(result)
        }
    }
}