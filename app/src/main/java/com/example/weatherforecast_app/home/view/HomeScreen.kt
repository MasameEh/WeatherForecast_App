package com.example.weatherforecast_app.home.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.WeatherDtoResponse
import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.home.viewmodel.HomeViewModel
import com.example.weatherforecast_app.ui.theme.DarkBlue
import com.example.weatherforecast_app.ui.theme.LightBlue
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.utils.formatUnixTimestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

private const val TAG = "HomeScreen"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val locationState by viewModel.locationStateFlow.collectAsStateWithLifecycle()
    val weatherDataState by viewModel.weatherData.collectAsStateWithLifecycle()

    LaunchedEffect(locationState) {
        locationState?.let {
            viewModel.getCurrentWeather(it.lat, it.lon)
        }
    }

    when(weatherDataState){
        WeatherDtoResponse.Loading -> {
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        is WeatherDtoResponse.Failure -> {
            Text("Sorry we couldn't show products now")
        }
        is WeatherDtoResponse.Success -> {

            val successData = (weatherDataState as WeatherDtoResponse.Success).weatherData
            Log.i(TAG, "HomeScreen:${successData} ${successData.weather} ")

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
//                    TopAppBar(
//                        title = {
//                            Text(
//                                text = "Weather Forecast",
//                                modifier = Modifier.fillMaxWidth(),
//                                textAlign = TextAlign.Center,
//                                style = MaterialTheme.typography.titleLarge
//                            )
//                        },
//                        colors = TopAppBarDefaults.topAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.primary,
//                            titleContentColor = Color.White
//                        )
//                    )
                }
            ) { innerPadding ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .gradientBackground()
                ) {
                    CurrentWeatherUI(successData)
                }
            }
        }
    }

}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CurrentWeatherUI(weatherDto: WeatherDTO){

    val formatter = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
    val date = formatter.format(Date())
    Column(
        modifier = Modifier.padding(start = 18.dp, top = 20.dp)
    ) {
        Text(
            text = "${weatherDto.name}, ${weatherDto.placeInfo.country}\n$date",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 18.dp, top = 10.dp)
            .fillMaxWidth()
    ) {
//        Text(
//            text = "Today",
//            color = Color.White,
//            style = MaterialTheme.typography.titleLarge,
//        )

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
        weatherDto.placeInfo.sunrise?.let { formatUnixTimestamp(it) }?.let {
            TemperatureDisplay(
                temperature = "${weatherDto.mainWeatherData.temperature}",
                feelsLikeTemp = "${weatherDto.mainWeatherData.feels_like}",
                weatherStatus = weatherDto.weather[0].main + " / " + weatherDto.weather[0].description,
                sunriseTime = it
            )
        }
        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconSquare(R.drawable.wind, "Wind","${weatherDto.wind.speed}", "m/s")
            IconSquare(R.drawable.humidity, "Humidity", "${weatherDto.mainWeatherData.humidity}", "%")
            IconSquare(R.drawable.pressure, "Pressure", "${weatherDto.mainWeatherData.pressure}", "hpa")
            IconSquare(R.drawable.clouds, "Clouds", " ${weatherDto.clouds.all}", "%")
        }
        Spacer(Modifier.height(10.dp))
        HourlyWeather()
        Spacer(Modifier.height(10.dp))
        WeeklyWeather()
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
            text = "Feels like $feelsLikeTemp",
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
            text = "Sunrise $sunriseTime",
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
fun HourlyWeather(){

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
        //contentPadding = PaddingValues(5.dp)
    ) {
        items(9) {
            HourlyWeatherItem()
        }
    }

}



@Composable
fun HourlyWeatherItem(){
    Box(
        modifier = Modifier
            .border(2.dp, LightBlue, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "12:00 PM",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(5.dp))
            Image(
                painter = painterResource(R.drawable.sunrise),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.height(5.dp))
            Row {
                Text(
                    text = "25",
                    color = Color.White,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleSmall
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
@Preview
@Composable
fun WeeklyWeather(){

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
            text = "Forecast for 7 days",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,)
        }
        items(7){
            WeeklyWeatherItem()
        }
    }

}


@Composable
fun WeeklyWeatherItem(){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = .3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Monday",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Image(
                painter = painterResource(R.drawable.sunrise),
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Row {
                Text(
                    text = "25",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "°C",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
                )
            }
        }

    }
}