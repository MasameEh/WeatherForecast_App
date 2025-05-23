package com.example.weatherforecast_app.data.local.location

import com.example.weatherforecast_app.data.local.LocationsDao
import com.example.weatherforecast_app.data.model.LocationInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationLocalDataSourceImp @Inject constructor(private val dao: LocationsDao): ILocationLocalDataSource {

    override fun getAllFavLocations(): Flow<List<LocationInfo>> {
        return dao.getAllFavLocations()
    }

    override suspend fun insertLocation(location: LocationInfo): Long {
        return dao.insertLocation(location)
    }

    override suspend fun deleteLocation(location: LocationInfo): Int {
        return dao.deleteLocation(location)
    }

}