package com.example.weatherforecast_app.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.data.model.AddressInfo
import com.example.weatherforecast_app.data.model.WeatherDTO
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.TimeZone


fun formatUnixTimestamp(timestamp: Long, context: Context): String {
    val date = Date(timestamp * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat("hh:mm a", Locale(LanguageHelper.getAppLocale(context).language))
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(date)
}
fun formatDateTimestamp(timestamp: Long, context: Context): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy - \n hh:mm a", Locale(LanguageHelper.getAppLocale(context).language))
    return formatter.format(date)
}

fun getHourlyForecast(list: List<WeatherDTO>): List<WeatherDTO> {
    return list.take(8)
}

fun getWeeklyForecast(list: List<WeatherDTO>, context: Context): List<WeatherDTO> {
    return list.groupBy {
        SimpleDateFormat("yyyy-MM-dd",  Locale(LanguageHelper.getAppLocale(context).language)).format(Date(it.dateTime * 1000L))
    }.map { (_, entries) ->
        entries.getOrNull(4) ?: entries.get(0)
    }
}

fun getDayOfWeek(weatherDTO: WeatherDTO, context: Context): String {
    val dayOfWeek = SimpleDateFormat("EEEE",  Locale(LanguageHelper.getAppLocale(context).language)).format(Date(weatherDTO.dateTime * 1000L))
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


fun formatNumberToLocale(value: Any, context: Context): String {
    val locale =
        context.resources.configuration.locales[0]
    return NumberFormat.getInstance(locale).format(value)
}

fun metersPerSecondToMilesPerHour(speedInMetersPerSecond: Double): Double {
    return speedInMetersPerSecond * 2.23694
}


fun formatAddress(address: AddressInfo): String {

    val city = address.city ?: address.town ?: address.village ?: address.neighbourhood ?: address.county
    val state = address.state
    val country = address.country

    return listOfNotNull(address.road, city, state, country)
        .joinToString(", ")
}

fun getWeatherIcon(iconName: String): Int {

    return when(iconName){
        "01n" -> R.drawable.clear_sky_n
        "01d" ->  R.drawable.clear_sky
        "02n" ->  R.drawable.few_clouds
        "02d" ->  R.drawable.few_clouds
        "03n" ->  R.drawable.cloudy
        "03d" ->  R.drawable.cloudy
        "04n" ->  R.drawable.broken_clouds
        "04d" ->  R.drawable.broken_clouds
        "09n" ->  R.drawable.rainy_day
        "09d" ->  R.drawable.rainy_day
        "10n" ->  R.drawable.rainy_day
        "10d" ->  R.drawable.rainy_day
        "11n" ->  R.drawable.storm
        "11d" ->  R.drawable.storm
        "13n" ->  R.drawable.snowflake
        "13d" ->  R.drawable.snowflake
        "50n" ->  R.drawable.snowflake
        "50d" ->  R.drawable.mist
        else-> R.drawable.clear_sky
    }
}