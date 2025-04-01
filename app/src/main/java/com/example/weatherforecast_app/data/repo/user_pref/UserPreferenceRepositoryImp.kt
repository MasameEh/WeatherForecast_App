package com.example.weatherforecast_app.data.repo.user_pref

import com.example.weatherforecast_app.data.local.preferences.CacheHelper


class UserPreferenceRepositoryImp private constructor(
    private val localPref: CacheHelper
): IUserPreferenceRepository {


    companion object{
        @Volatile
        private var instance: UserPreferenceRepositoryImp? = null

        fun getInstance(localDataSource: CacheHelper): UserPreferenceRepositoryImp {
            return instance ?: synchronized(this){ // double check
                instance ?:  UserPreferenceRepositoryImp(localDataSource).also { //  returns the newly created object it
                    instance = it
                }
            }
        }
    }

    override fun getUserLanguage(): String? {
        return localPref.getString("language")
    }

    override fun getTemperatureUnit(): String? {
        return localPref.getString("temp")
    }

    override fun getWindSpeedUnit(): String? {
        return localPref.getString("wind")
    }

    override fun getUserNotificationStatus(): Boolean {
        return localPref.getBoolean("notification")
    }

    override fun getUserLocationPref(): String? {
        return localPref.getString("location")
    }

    override fun updateLanguage(language: String) {
        localPref.saveString("language", language)
    }

    override fun updateTemperatureUnit(unit: String) {
        localPref.saveString("temp", unit)
    }

    override fun updateWindSpeedUnit(unit: String) {
        localPref.saveString("wind", unit)
    }

    override fun updateUserNotificationStatus(status: Boolean) {
        localPref.saveBoolean("notification", status)
    }

    override fun updateUserLocationPref(locationPref: String) {
        localPref.saveString("location", locationPref)
    }

}