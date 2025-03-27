package com.example.weatherforecast_app.data.local.Alert

import androidx.room.Delete

import com.example.weatherforecast_app.data.model.AlertInfo
import kotlinx.coroutines.flow.Flow

interface IAlertLocalDataSource {

    fun getAllAlerts() : Flow<List<AlertInfo>>

    suspend fun insertAlert(alert: AlertInfo) : Long

    suspend fun deleteAlert(alert: AlertInfo): Int
}