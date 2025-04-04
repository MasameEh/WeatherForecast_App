package com.example.weatherforecast_app.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import android.util.Log
import com.example.weatherforecast_app.main.MainActivity

import java.util.Locale


private const val TAG = "LanguageHelper"
object LanguageHelper {

    fun setAppLocale(context: Context, language: String, isOnCreate: Boolean) : Context {
        Log.i(TAG, "setAppLocale: lang: $language")

        val langCode = when (language) {
            "English" -> "en"
            "Arabic" -> "ar"
            else-> ""
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (Recommended)
            val localeManager = context.getSystemService(LocaleManager::class.java)
            val localeList = LocaleList.forLanguageTags(langCode)
            localeManager.applicationLocales = localeList
        } else {
//          AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(langCode))
            Log.i(TAG, "setAppLocale: $language")
            val locale = Locale(langCode)
            Locale.setDefault(locale)
            val config = context.resources.configuration.apply {
                setLocale(locale)
                setLayoutDirection(locale)
            }
            context.createConfigurationContext(config)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)

            if (!isOnCreate && context is Activity) {
                Log.i(TAG, "setAppLocale: Activity")
                restartApp(context) // Only for older versions (pre-Android 13)
            }
        }
        return context as Activity
    }

    private fun restartApp(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
        activity.finish()
    }
     fun getAppLocale(context: Context): Locale {
        return context.resources.configuration.locales[0]
    }
}