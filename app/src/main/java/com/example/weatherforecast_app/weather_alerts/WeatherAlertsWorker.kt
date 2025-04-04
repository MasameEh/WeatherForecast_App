package com.example.weatherforecast_app.weather_alerts

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.data.local.AppDatabase
import com.example.weatherforecast_app.data.local.location.LocationLocalDataSourceImp
import com.example.weatherforecast_app.data.remote.location.LocationRemoteDataSourceImp
import com.example.weatherforecast_app.data.remote.weather.WeatherRemoteDataSourceImp
import com.example.weatherforecast_app.data.repo.location_repo.LocationRepositoryImp
import com.example.weatherforecast_app.data.repo.weather_repo.WeatherRepositoryImp
import com.example.weatherforecast_app.main.MainActivity
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.utils.NotificationHelper.createNotificationChannel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.roundToInt

class WeatherAlertsWorker(
    private val context: Context,
    workerParameters: WorkerParameters,

): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        Log.i("WeatherAlertsWorker", "doWork: ")
        val weatherInfo = getLatestWeatherInfo()

        showWeatherNotification(weatherInfo)

        return Result.success()
    }

    private suspend fun getLatestWeatherInfo() : String{
        val locationRepo = LocationRepositoryImp.getInstance(
            LocationServices.getFusedLocationProviderClient(
                context
            ), LocationLocalDataSourceImp(
                AppDatabase.getInstance(context).getLocationsDao()
            ),
            LocationRemoteDataSourceImp()
        )



        return suspendCancellableCoroutine { continuation ->
            locationRepo.getFreshLocation { location ->
                if (location != null) {
                    // Fetch the weather for the location and continue with the result
                    val weatherInfo = getCurrentWeather(location.latitude, location.longitude)
                    continuation.resume(weatherInfo) // Resume with the weather info
                } else {
                    continuation.resume("No weather data available.")
                }
            }
        }
    }

    private fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
    ) : String{
        val weatherRepo = WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp()
        )
        var weatherInfo = ""
        runBlocking {
            weatherRepo.getCurrentWeather(
                latitude, longitude,
                language = LanguageHelper.getAppLocale(context).language,
                tempUnit = "metric"
            ).catch { }
                .collect { result ->
                    Log.i(
                        "WeatherAlertsWorker",
                        "getCurrentWeather: ${result.mainWeatherData.temperature.roundToInt()} "
                    )
                    weatherInfo =
                        "${result.mainWeatherData.temperature.roundToInt()} °C - ${result.weather[0].description}"
                }
        }
        Log.i("WeatherAlertsWorker", "weatherInfo: $weatherInfo")
        return weatherInfo
    }
    private fun showWeatherNotification(weatherInfo: String){

        createNotificationChannel(context)
        val intent = Intent(context,  MainActivity::class.java)

        Log.i("WeatherAlertsWorker", "doWork: showWeatherNotification")
        val builder =
            NotificationCompat.Builder(context, "weather_channel"   )
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Weather Alert")
                .setContentText("Don't forget to check the latest weather! \n $weatherInfo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
                .setAutoCancel(true)

        val notificationManager = getSystemService(context, NotificationManager::class.java)
        notificationManager?.notify(1, builder.build())
    }

}