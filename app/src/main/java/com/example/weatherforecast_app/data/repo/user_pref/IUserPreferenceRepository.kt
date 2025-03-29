package com.example.weatherforecast_app.data.repo.user_pref

import kotlinx.coroutines.flow.Flow

interface IUserPreferenceRepository {

    fun getUserLanguage(): String?
    fun getTemperatureUnit(): String?
    fun getWindSpeedUnit(): String?

     fun updateLanguage(language: String)
     fun updateTemperatureUnit(unit: String)

     fun updateWindSpeedUnit(unit: String)


}
