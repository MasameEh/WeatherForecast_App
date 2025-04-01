package com.example.weatherforecast_app.data.repo.user_pref


interface IUserPreferenceRepository {

    fun getUserLanguage(): String?
    fun getTemperatureUnit(): String?
    fun getWindSpeedUnit(): String?
    fun getUserNotificationStatus(): Boolean
    fun getUserLocationPref(): String?


    fun updateLanguage(language: String)
    fun updateTemperatureUnit(unit: String)
    fun updateWindSpeedUnit(unit: String)
    fun updateUserNotificationStatus(status: Boolean)
    fun updateUserLocationPref(locationPref: String)
}
