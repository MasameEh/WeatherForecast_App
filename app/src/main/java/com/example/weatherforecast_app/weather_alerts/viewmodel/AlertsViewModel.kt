package com.example.weatherforecast_app.weather_alerts.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.weatherforecast_app.data.model.AlertInfo
import com.example.weatherforecast_app.data.repo.alert_repo.IAlertRepository
import com.example.weatherforecast_app.utils.ResponseState
import com.example.weatherforecast_app.weather_alerts.WeatherAlertsWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


private const val TAG = "AlertsViewModel"
class AlertsViewModel(
    private val repo: IAlertRepository
) : ViewModel(){
    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker = _showDatePicker.asStateFlow()

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker = _showTimePicker.asStateFlow()


    private val _mutableAlertsList : MutableStateFlow<ResponseState> = MutableStateFlow(
        ResponseState.Loading)
    val alertsList: StateFlow<ResponseState> = _mutableAlertsList.asStateFlow()

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


    fun scheduleWeatherAlert(context: Context, timestamp: Long, alertId: String) {
        val delay = timestamp - System.currentTimeMillis()
//        val data = workDataOf(
//            "NOTIFICATION_CONTENT" to
//        )
        val workRequest = OneTimeWorkRequestBuilder<WeatherAlertsWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(alertId)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun getAllAlerts(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getAllAlerts()
            result.catch { ex ->
                mutableMsg.emit("Error!! Couldn't be retrieved, try again")
                _mutableAlertsList.value = ResponseState.Failure(ex)
            }.collect{
                _mutableAlertsList.value = ResponseState.Success(it)
            }
        }
    }

    fun insertAlertToAlerts(alert: AlertInfo){

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result  = repo.insertAlert(alert)
                if(result > 0){
                    mutableMsg.emit("Added successfully")
                }else{
                    mutableMsg.emit("Error!! Couldn't be Added, try again")
                    Log.i(TAG, "insertAlertToAlerts: res = $result")
                }
            }catch (ex: Exception){
                Log.i(TAG, "insertAlertToAlerts: ex = $ex")
                mutableMsg.emit("Error!! Couldn't be Added, try again")
            }

        }

    }

    fun deleteAlertFromAlerts(context: Context, alert: AlertInfo){

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result  = repo.deleteAlert(alert)
                if(result > 0){
                    deleteAlert(context, alert.id)
                    mutableMsg.emit("Deleted successfully")
                }else{
                    mutableMsg.emit("Error!! Couldn't be deleted, try again")
                    Log.i(TAG, "deleteAlertFromAlerts: res = $result")
                }
            }catch (ex: Exception){
                Log.i(TAG, "deleteAlertFromAlerts: ex = $ex")
                mutableMsg.emit("Error!! Couldn't be deleted, try again")
            }

        }

    }

    private fun deleteAlert(context: Context, alertId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(alertId) // Cancel work by tag
    }

}

class AlertsViewModelFactory(private val repo: IAlertRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(repo) as T
    }
}