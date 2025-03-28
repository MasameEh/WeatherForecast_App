package com.example.weatherforecast_app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")


class PreferenceLocalDataSourceImp(
    private val context: Context
) : IPreferenceLocalDataSource{

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val TEMPERATURE_UNIT_KEY = stringPreferencesKey("temperature_unit")
        val WIND_SPEED_UNIT_KEY = stringPreferencesKey("wind_speed_unit")
    }


    override val language: Flow<String> = context.dataStore.data.map {
        it[LANGUAGE_KEY] ?: "System Default"
    }

    override val temperature: Flow<String> = context.dataStore.data.map {
        it[TEMPERATURE_UNIT_KEY] ?: "Celsius"
    }

    override val wind: Flow<String> = context.dataStore.data.map {
        it[WIND_SPEED_UNIT_KEY] ?: "m/s"
    }

    override suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
    }

    override suspend fun saveTemperatureUnit(unit: String) {
        context.dataStore.edit { it[TEMPERATURE_UNIT_KEY] = unit }
    }

    override suspend fun saveWindSpeedUnit(unit: String) {
        context.dataStore.edit { it[WIND_SPEED_UNIT_KEY] = unit}
    }

}