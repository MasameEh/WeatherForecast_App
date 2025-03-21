package com.example.weatherforecast_app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatUnixTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(date)
}