package com.example.weatherforecast_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast_app.data.model.AlertInfo
import com.example.weatherforecast_app.data.model.LocationInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationsDao {

    @Query("SELECT * FROM location")
    fun getAllFavLocations() : Flow<List<LocationInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: LocationInfo) : Long

    @Delete
    suspend fun deleteLocation(location:LocationInfo): Int

}