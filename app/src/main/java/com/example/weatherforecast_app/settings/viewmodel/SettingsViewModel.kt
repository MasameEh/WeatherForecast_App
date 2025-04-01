package com.example.weatherforecast_app.settings.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast_app.data.repo.user_pref.IUserPreferenceRepository


class SettingsViewModel(
    private val repo: IUserPreferenceRepository
) : ViewModel() {


    fun getLanguagePref(): String? {
        return repo.getUserLanguage()
    }

    fun getUserNotificationStatus(): Boolean {
        return repo.getUserNotificationStatus()
    }

    fun getTemperatureUnitPref(): String? {
        return repo.getTemperatureUnit()
    }

    fun getWindUnitPref(): String? {
        return repo.getWindSpeedUnit()
    }

    fun getUserLocationPref(): String? {
        return repo.getUserLocationPref()
    }


    fun updateTemperatureUnit(unit: String) {
        repo.updateTemperatureUnit(unit)
    }

    fun updateWindSpeedUnit(unit: String){
        repo.updateWindSpeedUnit(unit)
    }

    fun updateLanguage(language: String){
        repo.updateLanguage(language)
    }

    fun updateUserNotificationStatus(status: Boolean){
        repo.updateUserNotificationStatus(status)
    }

    fun updateUserLocationPref(): String?{
        return repo.getUserLocationPref()
    }

}

class SettingsViewModelFactory(private val repo: IUserPreferenceRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repo) as T
    }
}

