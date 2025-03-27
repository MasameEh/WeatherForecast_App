package com.example.weatherforecast_app.data.repo.alert_repo

import com.example.weatherforecast_app.data.model.AlertInfo
import kotlinx.coroutines.flow.Flow

interface IAlertRepository {
    fun getAllAlerts() : Flow<List<AlertInfo>>

    suspend fun insertAlert(alert: AlertInfo) : Long

    suspend fun deleteAlert(alert: AlertInfo): Int
}