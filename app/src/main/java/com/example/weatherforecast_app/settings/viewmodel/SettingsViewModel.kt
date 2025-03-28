package com.example.weatherforecast_app.settings.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.repo.user_pref.IUserPreferenceRepository

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: IUserPreferenceRepository
) : ViewModel() {


    val language: StateFlow<String> = repo.language
        .stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        "System Default"
    )

    val temperatureUnit: StateFlow<String> = repo.temperature
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            "Celsius"
        )

    val windSpeedUnit: StateFlow<String> = repo.wind
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            "m/s"
        )


    fun updateLanguage(language: String){
        viewModelScope.launch {
            repo.updateLanguage(language)
        }

    }


    fun updateTemperatureUnit(unit: String){
        viewModelScope.launch {
            repo.updateTemperatureUnit(unit)
        }
    }

    fun updateWindSpeedUnit(unit: String){
        viewModelScope.launch {
            repo.updateWindSpeedUnit(unit)
        }
    }
}

class SettingsViewModelFactory(private val repo: IUserPreferenceRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repo) as T
    }
}

