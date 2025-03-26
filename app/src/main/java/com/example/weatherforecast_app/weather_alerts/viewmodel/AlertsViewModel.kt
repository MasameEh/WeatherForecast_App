package com.example.weatherforecast_app.weather_alerts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class AlertsViewModel : ViewModel(){
    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker = _showDatePicker.asStateFlow()

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker = _showTimePicker.asStateFlow()

    private val mutableMsg: MutableSharedFlow<String> = MutableSharedFlow()
    val message = mutableMsg.asSharedFlow()

    fun toggleDatePicker(show: Boolean) {
        _showDatePicker.value = show
        Log.i("WeatherAlerts", "toggleDatePicker: ")
    }

    fun toggleTimePicker(show: Boolean) {
        _showTimePicker.value = show
        Log.i("WeatherAlerts", "toggleTimePicker: ")
    }
    fun saveSelectedDate(timeInMillis: Long) {
        // Handle saving logic or trigger WorkManager/AlarmManager here
        Log.d("WeatherAlerts", "Selected Date: ${Date(timeInMillis)}")
    }

}