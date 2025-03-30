package com.example.weatherforecast_app.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

import java.util.Locale


private const val TAG = "LanguageHelper"
object LanguageHelper {

    fun setAppLocale(context: Context, language: String) {
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

            context.resources.updateConfiguration(config, context.resources.displayMetrics)

            if (context is Activity) {
                Log.i(TAG, "setAppLocale: Activity")
                context.recreate() // Only for older versions (pre-Android 13)
            }
        }
    }

    fun wrapContext(context: Context, language: String?): Context {
        val locale = when (language) {
            "Arabic" -> Locale("ar")
            "English" -> Locale.ENGLISH
            else -> getSystemLocale()
        }

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

     fun getSystemLocale(): Locale {
        return Resources.getSystem().configuration.locales[0]
    }
}