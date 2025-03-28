package com.example.weatherforecast_app.data.repo.user_pref

import com.example.weatherforecast_app.data.local.Alert.IAlertLocalDataSource
import com.example.weatherforecast_app.data.local.preferences.IPreferenceLocalDataSource
import com.example.weatherforecast_app.data.repo.alert_repo.AlertRepositoryImp
import kotlinx.coroutines.flow.Flow

class UserPreferenceRepositoryImp private constructor(
    private val localPref: IPreferenceLocalDataSource
): IUserPreferenceRepository {


    override val language: Flow<String> = localPref.language
    override val temperature: Flow<String> = localPref.temperature
    override val wind: Flow<String> = localPref.wind


    override suspend fun updateLanguage(language: String) {
        localPref.saveLanguage(language)
    }


    override suspend fun updateTemperatureUnit(unit: String) {
        localPref.saveTemperatureUnit(unit)
    }

    override suspend fun updateWindSpeedUnit(unit: String) {
        localPref.saveWindSpeedUnit(unit)
    }

    companion object{
        @Volatile
        private var instance: UserPreferenceRepositoryImp? = null

        fun getInstance(localDataSource: IPreferenceLocalDataSource): UserPreferenceRepositoryImp {
            return instance ?: synchronized(this){ // double check
                instance ?:  UserPreferenceRepositoryImp(localDataSource).also { //  returns the newly created object it
                    instance = it
                }
            }
        }
    }

}