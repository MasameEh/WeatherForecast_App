package com.example.weatherforecast_app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.WorkManager


object NotificationHelper {

    private const val TAG = "NotificationHelper"

    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_channel", // Channel ID
                "Weather Alerts",  // Channel Name
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Shows weather alerts"
            }
            Log.i("WeatherAlertsWorker", " createNotificationChannel")
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun enableNotifications(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                "weather_channel",
                "Weather Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Shows weather alerts"
            }

            notificationManager.createNotificationChannel(channel)
        }
    }


    fun disableAllNotifications(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notificationChannels.forEach { channel ->
                Log.i(TAG, "disableAllNotifications: ${channel.id}")
                notificationManager.deleteNotificationChannel(channel.id)
            }
        }
        WorkManager.getInstance(context).cancelAllWork()
    }
}