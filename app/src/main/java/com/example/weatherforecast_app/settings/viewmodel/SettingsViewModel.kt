package com.example.weatherforecast_app.settings.viewmodel


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast_app.data.model.AlertInfo
import com.example.weatherforecast_app.data.repo.user_pref.IUserPreferenceRepository
import com.example.weatherforecast_app.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.TimeZone


private const val TAG = "SettingsViewModel"
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



    private fun getDefaultCalendarId(context: Context): Long? {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT,
            CalendarContract.Calendars.ACCOUNT_TYPE
        )

        val selection = "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.OWNER_ACCOUNT} = ?"
        val selectionArgs = arrayOf("samehms116@gmail.com", "samehms116@gmail.com")

        val cursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return it.getLong(0)
            }
        }

        return null
    }



    private fun insertAlertAsEvent(context: Context, alert: AlertInfo) {
        val calendarId = getDefaultCalendarId(context) ?: return
        Log.i(TAG, "Event timestamp: ${Date(alert.timestamp)}")

        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, "Weather Alert")
            put(CalendarContract.Events.DESCRIPTION, "Scheduled weather alert.")
            put(CalendarContract.Events.DTSTART, alert.timestamp)
            put(CalendarContract.Events.DTEND, alert.timestamp + 60 * 60 * 1000)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val eventUri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        Log.i(TAG, "Inserted event URI: $eventUri")
    }
    fun syncWeatherAlertsToCalendar(context: Context) {

        val cursor = context.contentResolver.query(Constants.CONTENT_URI, null, null, null, null)
        Log.i(TAG, "syncWeatherAlertsToCalendar: function called")
        cursor?.use {
            val idIndex = it.getColumnIndex("id")
            val timestampIndex = it.getColumnIndex("timestamp")

            while (it.moveToNext()) {

                val id = it.getString(idIndex)
                val timestamp = it.getLong(timestampIndex)
                val alert = AlertInfo(id,  timestamp)
                insertAlertAsEvent(context, alert)
            }
        }
    }


}

class SettingsViewModelFactory(private val repo: IUserPreferenceRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repo) as T
    }
}

