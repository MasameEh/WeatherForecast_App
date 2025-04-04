package com.example.weatherforecast_app.favorite_weather_details.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.favorite_weather_details.viewmodel.FavoriteDetailsViewModel
import com.example.weatherforecast_app.home.view.CurrentWeatherUI
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.ui.theme.onSecondaryColor
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.utils.ResponseState


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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground()
                    .padding(15.dp)
            ) {
                Text(
                    stringResource(R.string.sorry),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    colors = ButtonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    ),
                    onClick = {
                        viewModel.getCurrentWeather(
                            latitude,
                            longitude,
                            LanguageHelper.getAppLocale(context).language,
                            userTempUnitPref
                        )
                        viewModel.getWeeklyWeather(
                            latitude,
                            longitude,
                            LanguageHelper.getAppLocale(context).language,
                            userTempUnitPref
                        )
                    }
                ) {
                    Text(stringResource(R.string.try_again))
                }
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
                        modifier = Modifier
                            .fillMaxSize()
                            .gradientBackground()
                    ) {
                        CircularProgressIndicator(color = onSecondaryColor)
                    }
                ResponseState.Loading ->
                    Box(
                        contentAlignment = Alignment.Center ,
                        modifier = Modifier
                            .fillMaxSize()
                            .gradientBackground()
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
                                .padding(padding)
                                .gradientBackground()
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





