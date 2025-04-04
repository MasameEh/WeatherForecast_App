package com.example.weatherforecast_app.data.local.Alert

import com.example.weatherforecast_app.data.local.AlertsDao
import com.example.weatherforecast_app.data.local.LocationsDao
import com.example.weatherforecast_app.data.model.AlertInfo
import kotlinx.coroutines.flow.Flow

class AlertLocalDataSourceImp(private val dao: AlertsDao): IAlertLocalDataSource {

    override fun getAllAlerts(): Flow<List<AlertInfo>> {
        return dao.getAllAlerts()
    }

    override suspend fun insertAlert(alert: AlertInfo): Long {
        return dao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertInfo): Int {
        return dao.deleteAlert(alert)
    }
}