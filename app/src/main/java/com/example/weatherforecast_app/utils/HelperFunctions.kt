package com.example.weatherforecast_app.utils

import com.example.weatherforecast_app.data.model.WeatherDTO
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

fun getHourlyForecast(list: List<WeatherDTO>): List<WeatherDTO> {
    return list.take(8)
}

fun getWeeklyForecast(list: List<WeatherDTO>): List<WeatherDTO> {


    return list.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dateTime * 1000L))
    }.map { (_, entries) ->
        println("entries" + entries)
        entries.getOrNull(4) ?: entries.get(0)
    }
}

fun getDayOfWeek(weatherDTO: WeatherDTO): String {
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(weatherDTO.dateTime * 1000L))
    val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    return if (dayOfWeek == today) "Today" else dayOfWeek
}