package com.example.weatherforecast_app.weather_alerts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherforecast_app.R

class WeatherAlertsWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    override fun doWork(): Result {

        Log.i("WeatherAlertsWorker", "doWork: ")
        showWeatherNotification("helpp")
        return Result.success()
    }

    private fun showWeatherNotification(weatherInfo: String){
        createNotificationChannel()

        Log.i("WeatherAlertsWorker", "doWork: showWeatherNotification")
        val builder =
            NotificationCompat.Builder(context, "weather_channel")
                .setSmallIcon(R.drawable.sunrise)
                .setContentTitle("Weather Alerts")
                .setContentText(weatherInfo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(context, NotificationManager::class.java)
        notificationManager?.notify(1, builder.build())
    }
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_channel", // Channel ID
                "Weather Alerts",  // Channel Name
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Shows weather alerts"
            }
            Log.i("WeatherAlertsWorker", "doWork: createNotificationChannel")
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}