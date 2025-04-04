package com.example.weatherforecast_app.favorite_weather_details.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.favorite_weather_details.viewmodel.FavoriteDetailsViewModel
import com.example.weatherforecast_app.home.view.CurrentWeatherUI
import com.example.weatherforecast_app.home.view.HourlyWeather
import com.example.weatherforecast_app.home.view.IconSquare
import com.example.weatherforecast_app.home.view.TemperatureDisplay
import com.example.weatherforecast_app.home.view.WeeklyWeather
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.ui.theme.onSecondaryColor
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.utils.ResponseState
import com.example.weatherforecast_app.utils.formatUnixTimestamp
import com.example.weatherforecast_app.utils.getHourlyForecast
import com.example.weatherforecast_app.utils.getWeeklyForecast
import com.example.weatherforecast_app.utils.metersPerSecondToMilesPerHour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


private const val TAG = "FavoriteDetailsScreen"
@Composable
fun FavoriteDetailsScreen(
    viewModel: FavoriteDetailsViewModel,
    latitude: Double,
    longitude: Double,
    city: String
){

    val currentWeatherState by viewModel.currentWeatherData.collectAsStateWithLifecycle()
    val weeklyWeatherState by viewModel.weeklyWeatherData.collectAsStateWithLifecycle()

    val userTempUnitPref = viewModel.getTemperatureUnitPref()
    val userWindUnitPref = viewModel.getWindUnitPref() ?: "m/s"

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather(latitude, longitude, LanguageHelper.getAppLocale(context).language, userTempUnitPref)
        viewModel.getWeeklyWeather(latitude, longitude, LanguageHelper.getAppLocale(context).language, userTempUnitPref)
    }

    when(currentWeatherState){
        ResponseState.Loading -> {
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier.fillMaxSize().gradientBackground()
            ) {
                CircularProgressIndicator(color = onSecondaryColor)
            }
        }
        is ResponseState.Failure -> {
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Sorry we couldn't show Weather data now")
            }
        }
        is ResponseState.Success -> {
            val successCurrentWeatherData = (currentWeatherState as ResponseState.Success).data as WeatherDTO
            Log.i(TAG, "FavoriteDetailsScreen-> CurrentWeatherData: ${successCurrentWeatherData.placeInfo} ${successCurrentWeatherData.weather} ")

            when(weeklyWeatherState){
                is ResponseState.Failure ->
                    Box(
                        contentAlignment = Alignment.Center ,
                        modifier = Modifier.fillMaxSize().gradientBackground()
                    ) {
                        Text("Sorry we couldn't show Weather data now")
                    }
                ResponseState.Loading ->
                    Box(
                        contentAlignment = Alignment.Center ,
                        modifier = Modifier.fillMaxSize().gradientBackground()
                    ) {
                        CircularProgressIndicator(color = onSecondaryColor)
                    }
                is ResponseState.Success ->{
                    val successWeeklyWeatherData = (weeklyWeatherState as ResponseState.Success).data as WeatherResponse
                    Log.i(TAG, "HomeScreen-> FavoriteDetailsScreen: ${successWeeklyWeatherData.message} ${successWeeklyWeatherData.city}  ${successWeeklyWeatherData.count} ")
                    Scaffold { padding->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 65.dp)
                                .padding(padding).gradientBackground()
                        ) {
                            CurrentWeatherUI(
                                successCurrentWeatherData,
                                successWeeklyWeatherData.weatherDTOList,
                                userTempUnitPref,
                                userWindUnitPref,
                                city
                            )
                        }
                    }

                }

            }

        }
    }

}

//
//@Composable
//fun CurrentWeatherUI(currentWeatherData: WeatherDTO,
//                     weeklyWeatherData: List<WeatherDTO>,
//                     userTempUnitPref: String?,
//                     userWindUnitPref: String,
//                     city: String){
//
//    val formatter = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
//    val date = formatter.format(Date())
//    Column(
//        modifier = Modifier.padding(start = 18.dp, top = 20.dp, end = 18.dp)
//    ) {
//        Text(
//            text = "${city}\n$date",
//            color = Color.White,
//            style = MaterialTheme.typography.titleMedium
//        )
//    }
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .padding(start = 18.dp, top = 10.dp, end = 18.dp)
//            .fillMaxWidth()
//    ) {
//
//        Image(
//            painter = painterResource(R.drawable.clouds),
//            contentScale = ContentScale.Fit,
//            contentDescription =  null,
//            modifier = Modifier.size(120.dp)
//        )
//        currentWeatherData.placeInfo.sunrise?.let { formatUnixTimestamp(it, context) }?.let {
//            TemperatureDisplay(
//                temperature = currentWeatherData.mainWeatherData.temperature.roundToInt(),
//                feelsLikeTemp = currentWeatherData.mainWeatherData.feels_like.roundToInt(),
//                weatherStatus = currentWeatherData.weather[0].main + " / " + currentWeatherData.weather[0].description,
//                sunriseTime = it,
//                unit = userTempUnitPref
//            )
//        }
//        Spacer(Modifier.height(8.dp))
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            IconSquare(
//                R.drawable.wind,
//                stringResource(R.string.wind),
//                if (userWindUnitPref == "m/s") currentWeatherData.wind.speed else metersPerSecondToMilesPerHour(
//                    currentWeatherData.wind.speed
//                ),
//                if (userWindUnitPref == "m/s") stringResource(R.string.meter_per_sec) else stringResource(
//                    R.string.mile_per_hour
//                )
//            )
//            IconSquare(
//                R.drawable.humidity,
//                stringResource(R.string.humidity),
//                currentWeatherData.mainWeatherData.humidity.toDouble(),
//                "%"
//            )
//            IconSquare(
//                R.drawable.pressure,
//                stringResource(R.string.pressure),
//                currentWeatherData.mainWeatherData.pressure.toDouble(),
//                stringResource(R.string.pascal)
//            )
//            IconSquare(
//                R.drawable.clouds,
//                stringResource(R.string.clouds),
//                currentWeatherData.clouds.all.toDouble(),
//                "%"
//            )
//        }
//        Spacer(Modifier.height(10.dp))
//        HourlyWeather(getHourlyForecast(weeklyWeatherData), userTempUnitPref)
//        Spacer(Modifier.height(10.dp))
//        WeeklyWeather(getWeeklyForecast(weeklyWeatherData), userTempUnitPref)
//    }
//}




