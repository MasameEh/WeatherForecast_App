package com.example.weatherforecast_app.data.local.preferences

import kotlinx.coroutines.flow.Flow

interface IPreferenceLocalDataSource {

    suspend fun saveLanguage(language: String)
    suspend fun saveTemperatureUnit(unit: String)
    suspend fun saveWindSpeedUnit(unit: String)

    val language: Flow<String>
    val temperature: Flow<String>
    val wind: Flow<String>
}