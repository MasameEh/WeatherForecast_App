package com.example.weatherforecast_app.data.local.preferences

import android.content.Context
import android.content.SharedPreferences


class CacheHelper private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Save string value
    fun saveString(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    // Get string value
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "System Default")
    }

    // Save boolean value
    fun saveBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    // Get boolean value
    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    // Remove a specific key
    fun remove(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    // Clear all data
    fun clear() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val PREF_NAME = "weather_prefs"
        private var cacheHelper: CacheHelper? = null

        fun getInstance(context: Context): CacheHelper {
            if (cacheHelper == null) {
                cacheHelper = CacheHelper(context)
            }
            return cacheHelper as CacheHelper
        }
    }
}