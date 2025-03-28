package com.example.weatherforecast_app.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LanguageHelper {

    fun setAppLocale(context: Context, language: String) {

        val langCode = when (language) {
            "English" -> "en"
            "Arabic" -> "ar"
            else-> "System Default"
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (Recommended)
            val localeManager = context.getSystemService(LocaleManager::class.java)
            val localeList = LocaleList.forLanguageTags(langCode)
            localeManager.applicationLocales = localeList
        } else {
            // Fallback for Android 12 and below
            val locale = Locale(langCode)

            Locale.setDefault(locale)
            val config = context.resources.configuration.apply {
                setLocale(locale)
            }

            context.resources.updateConfiguration(config, context.resources.displayMetrics)

            if (context is Activity) {
                context.recreate() // Only for older versions (pre-Android 13)
            }
        }
    }

    fun getCurrentLanguage(context: Context): String {
        return context.resources.configuration.locales[0].language
    }
}