package com.example.weatherforecast_app.data.repo.user_pref

import kotlinx.coroutines.flow.Flow

interface IUserPreferenceRepository {

    suspend fun updateLanguage(language: String)

    suspend fun updateTemperatureUnit(unit: String)

    suspend fun updateWindSpeedUnit(unit: String)


    val language: Flow<String>
    val temperature: Flow<String>
    val wind: Flow<String>
}
