package com.example.weatherforecast_app.settings.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.repo.user_pref.IUserPreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val repo: IUserPreferenceRepository
) : ViewModel() {

    private val mutableMsg: MutableSharedFlow<String> = MutableSharedFlow()
    val message = mutableMsg.asSharedFlow()


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
        viewModelScope.launch(Dispatchers.IO) {
            mutableMsg.emit("Temperature unit changed to $unit")
            repo.updateTemperatureUnit(unit)
        }
    }

    fun updateWindSpeedUnit(unit: String){
        viewModelScope.launch(Dispatchers.IO) {
            mutableMsg.emit("Wind unit changed to $unit")
            repo.updateWindSpeedUnit(unit)
        }
    }

    fun updateLanguage(language: String){
        viewModelScope.launch(Dispatchers.IO) {
            mutableMsg.emit("Language changed to $language")
            repo.updateLanguage(language)
        }
    }

    fun updateUserNotificationStatus(status: Boolean){
        repo.updateUserNotificationStatus(status)
    }

    fun updateUserLocationPref(locationPref: String){
        repo.updateUserLocationPref(locationPref)
    }

}

class SettingsViewModelFactory(private val repo: IUserPreferenceRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repo) as T
    }
}

