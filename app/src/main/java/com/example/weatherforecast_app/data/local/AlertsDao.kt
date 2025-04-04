package com.example.weatherforecast_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast_app.data.model.AlertInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {

    @Query("SELECT * FROM alert")
    fun getAllAlerts() : Flow<List<AlertInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(alert: AlertInfo) : Long

    @Delete
    suspend fun deleteAlert(alert: AlertInfo): Int
}