package com.example.weatherforecast_app.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.weatherforecast_app.data.model.WeatherDTO
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun formatUnixTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(date)
}
fun formatDateTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy - \n hh:mm a", Locale.getDefault())
    return formatter.format(date)
}

fun getHourlyForecast(list: List<WeatherDTO>): List<WeatherDTO> {
    return list.take(8)
}

fun getWeeklyForecast(list: List<WeatherDTO>): List<WeatherDTO> {


    return list.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dateTime * 1000L))
    }.map { (_, entries) ->
        entries.getOrNull(4) ?: entries.get(0)
    }
}

fun getDayOfWeek(weatherDTO: WeatherDTO): String {
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(weatherDTO.dateTime * 1000L))
    val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    return if (dayOfWeek == today) "Today" else dayOfWeek
}

fun getLocationName(context: Context, lat: Double, lng: Double): Address? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        val address = addresses?.get(0)

        //addresses?.get(0)?.getAddressLine(0) ?: "Unknown location"
        return address

    } catch (e: Exception) {
        null
    }
}


fun formatNumberToLocale(value: Int, context: Context): String {
    val locale =
        context.resources.configuration.locales[0]
    return NumberFormat.getInstance(locale).format(value)
}

fun metersPerSecondToMilesPerHour(speedInMetersPerSecond: Double): Double {
    return speedInMetersPerSecond * 2.23694
}