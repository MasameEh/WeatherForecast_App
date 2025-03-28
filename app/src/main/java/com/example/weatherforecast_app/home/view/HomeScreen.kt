package com.example.weatherforecast_app.home.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.utils.ResponseState
import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.home.viewmodel.HomeViewModel
import com.example.weatherforecast_app.ui.theme.LightBlue
import com.example.weatherforecast_app.ui.theme.MediumBlue
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.ui.theme.onSecondaryColor
import com.example.weatherforecast_app.utils.formatUnixTimestamp
import com.example.weatherforecast_app.utils.getDayOfWeek
import com.example.weatherforecast_app.utils.getHourlyForecast
import com.example.weatherforecast_app.utils.getWeeklyForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

private const val TAG = "HomeScreen"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val locationState by viewModel.locationStateFlow.collectAsStateWithLifecycle()
    val currentWeatherState by viewModel.currentWeatherData.collectAsStateWithLifecycle()
    val weeklyWeatherState by viewModel.weeklyWeatherData.collectAsStateWithLifecycle()


    LaunchedEffect(locationState) {
        locationState?.let {
            viewModel.getCurrentWeather(it.lat, it.lon)
            viewModel.getWeeklyWeather(it.lat, it.lon)
        }
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
                Text(
                    stringResource(R.string.sorry),
                    style = MaterialTheme.typography.titleLarge,
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
                        locationState?.let {
                            viewModel.getCurrentWeather(it.lat, it.lon)
                        }
                    }
                ) {
                    Text(stringResource(R.string.try_again))
                }
            }
        }
        is ResponseState.Success -> {
            val successCurrentWeatherData = (currentWeatherState as ResponseState.Success).data as WeatherDTO
            Log.i(TAG, "HomeScreen-> CurrentWeatherData: ${successCurrentWeatherData.placeInfo} ${successCurrentWeatherData.weather} ")

            when(weeklyWeatherState){
                is ResponseState.Failure ->
                    Box(
                        contentAlignment = Alignment.Center ,
                        modifier = Modifier.fillMaxSize().gradientBackground()
                    ) {
                        Text(
                            stringResource(R.string.sorry),
                            style = MaterialTheme.typography.titleLarge,
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
                                locationState?.let {
                                    viewModel.getCurrentWeather(it.lat, it.lon)
                                }
                            }
                        ) {
                            Text(stringResource(R.string.try_again))
                        }
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
                    Log.i(TAG, "HomeScreen-> WeeklyWeatherData: ${successWeeklyWeatherData.message} ${successWeeklyWeatherData.city}  ${successWeeklyWeatherData.count} ")
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .gradientBackground()
                                .statusBarsPadding()
                                .navigationBarsPadding()
                        ) {
                            CurrentWeatherUI(successCurrentWeatherData, successWeeklyWeatherData.weatherDTOList)
                        }

                }

            }

        }
    }

}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeatherUI(currentWeatherData: WeatherDTO, weeklyWeatherData: List<WeatherDTO>){
    val context = LocalContext.current
    val formatter = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
    val date = formatter.format(Date())

    Column(
        modifier = Modifier.padding(start = 18.dp, top = 20.dp, end = 18.dp)
    ) {
        Text(
            text = "${currentWeatherData.name}, ${currentWeatherData.placeInfo.country}\n$date",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 18.dp, top = 10.dp, end = 18.dp)
            .fillMaxWidth()
    ) {


//        GlideImage(
//            model = "https://openweathermap.org/img/wn/${weatherDto.weather[0].icon}@2x.png",
//            contentDescription = " ",
//            modifier = Modifier.size(120.dp),
//            contentScale = ContentScale.Fit,
//        )
        Image(
            painter = painterResource(R.drawable.clouds),
            contentScale = ContentScale.Fit,
            contentDescription =  null,
            modifier = Modifier.size(120.dp)
        )
        currentWeatherData.placeInfo.sunrise?.let { formatUnixTimestamp(it) }?.let {
            TemperatureDisplay(
                temperature = "${currentWeatherData.mainWeatherData.temperature.roundToInt()}",
                feelsLikeTemp = "${currentWeatherData.mainWeatherData.feels_like}",
                weatherStatus = currentWeatherData.weather[0].main + " / " + currentWeatherData.weather[0].description,
                sunriseTime = it
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconSquare(R.drawable.wind, stringResource(R.string.wind),"${currentWeatherData.wind.speed}", "m/s")
            IconSquare(R.drawable.humidity, stringResource(R.string.humidity), "${currentWeatherData.mainWeatherData.humidity}", "%")
            IconSquare(R.drawable.pressure, stringResource(R.string.pressure), "${currentWeatherData.mainWeatherData.pressure}", "hpa")
            IconSquare(R.drawable.clouds, stringResource(R.string.clouds), " ${currentWeatherData.clouds.all}", "%")
        }
        Spacer(Modifier.height(10.dp))
        HourlyWeather(getHourlyForecast(weeklyWeatherData))
        Spacer(Modifier.height(10.dp))
        WeeklyWeather(getWeeklyForecast(weeklyWeatherData))
    }
}


@Composable
fun TemperatureDisplay(temperature: String,
                       weatherStatus: String,
                       feelsLikeTemp: String = "25",
                       unit: String = "°C",
                       sunriseTime: String = "5:50"
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = temperature,
            color = Color.White,
            fontSize = 32.sp,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = unit,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
        )
    }
    Spacer(Modifier.height(5.dp))
    Text(
        text = weatherStatus,
        color = Color.White,
        style = MaterialTheme.typography.titleMedium,
        )
    Spacer(Modifier.height(5.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "${stringResource(R.string.Feels_like)} $feelsLikeTemp",
            color = Color.White.copy(alpha = .6f),
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = unit,
            color = Color.White.copy(alpha = .6f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = ".",
            color = Color.White.copy(alpha = .6f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
        )
        Image(
            painter = painterResource(R.drawable.sunrise),
            contentScale = ContentScale.Fit,
            contentDescription =  null,
            modifier = Modifier.size(25.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = "${stringResource(R.string.sunrise)} $sunriseTime",
            color = Color.White.copy(alpha = .6f),
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelSmall
        )
    }
}


@Composable
fun IconSquare(iconResId: Int, description: String, measurement: String, unit: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(2.dp, LightBlue, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = description,
                //colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(30.dp)
            )

        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = "$measurement $unit",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }

}


@Composable
fun HourlyWeather(weatherList: List<WeatherDTO>){

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp))
    {
        items(weatherList.size) {
            HourlyWeatherItem(weatherList[it])
        }
    }

}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherItem(weatherDTO: WeatherDTO){
    formatUnixTimestamp(weatherDTO.dateTime.toLong())

    Box(
        modifier = Modifier
            .border(2.dp, LightBlue, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = formatUnixTimestamp(weatherDTO.dateTime.toLong()),
                color = Color.White,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(Modifier.height(5.dp))
            GlideImage(
                model = "https://openweathermap.org/img/wn/${weatherDTO.weather[0].icon}@2x.png",
                contentDescription = " ",
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Fit,
            )
//            Image(
//                painter = painterResource(R.drawable.sunrise),
//                contentScale = ContentScale.Fit,
//                contentDescription = null,
//                modifier = Modifier.size(30.dp)
//            )
            Spacer(Modifier.height(5.dp))
            Row {
                Text(
                    text = "${weatherDTO.mainWeatherData.temperature.roundToInt()}",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "°C",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
                )
            }
        }
    }

}

@Composable
fun WeeklyWeather(weatherList: List<WeatherDTO>){
    Log.i(TAG, "WeeklyWeather: ${weatherList.size}")
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 18.dp),

    ) {
        item {
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
            text = stringResource(R.string.forecast_for_upcoming),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,)
        }
        items(weatherList.size){
            WeeklyWeatherItem(weatherList[it])
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeeklyWeatherItem(weatherDTO: WeatherDTO){
    val dayOfWeek = getDayOfWeek(weatherDTO)

    Log.i(TAG, "dayOfWeek: ${dayOfWeek}")
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = onSecondaryColor
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = dayOfWeek,
                color = MediumBlue,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1.3f)

            )
            GlideImage(
                model = "https://openweathermap.org/img/wn/${weatherDTO.weather[0].icon}@2x.png",
                contentDescription = " ",
                modifier = Modifier
                    .size(35.dp)
                    .weight(1.7f),
                contentScale = ContentScale.Fit,
            )
//            Image(
//                painter = painterResource(R.drawable.sunrise),
//                contentScale = ContentScale.Fit,
//                contentDescription = null,
//                modifier = Modifier.size(30.dp)
//            )
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                ) {
                Text(
                    text = "${weatherDTO.mainWeatherData.temp_max.roundToInt()} / ${weatherDTO.mainWeatherData.temp_min.roundToInt()}",
                    color = MediumBlue,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "°C",
                    color = MediumBlue,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
                )
            }
        }

    }
}