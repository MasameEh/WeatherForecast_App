package com.example.weatherforecast_app.weather_alerts.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecast_app.weather_alerts.WeatherAlertsWorker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import java.util.concurrent.TimeUnit

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


    fun scheduleWeatherAlert(context: Context, timestamp: Long) {
        val delay = timestamp - System.currentTimeMillis()

        val workRequest = OneTimeWorkRequestBuilder<WeatherAlertsWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}