package com.example.weatherforecast_app.data.repo.alert_repo

import com.example.weatherforecast_app.data.local.alert.IAlertLocalDataSource
import com.example.weatherforecast_app.data.model.AlertInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlertRepositoryImp @Inject constructor(
    private val localDataSource: IAlertLocalDataSource
) : IAlertRepository {

    override fun getAllAlerts(): Flow<List<AlertInfo>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: AlertInfo): Long {
        return localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertInfo): Int {
        return localDataSource.deleteAlert(alert)
    }

    companion object{
        @Volatile
        private var instance: AlertRepositoryImp? = null

        fun getInstance(localDataSource: IAlertLocalDataSource): AlertRepositoryImp{
            return instance ?: synchronized(this){ // double check
                instance ?:  AlertRepositoryImp(localDataSource).also { //  returns the newly created object it
                    instance = it
                }
            }
        }
    }
}