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
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.utils.ResponseState
import com.example.weatherforecast_app.data.model.WeatherDTO
import com.example.weatherforecast_app.data.model.WeatherResponse
import com.example.weatherforecast_app.home.viewmodel.HomeViewModel
import com.example.weatherforecast_app.ui.theme.LightBlue
import com.example.weatherforecast_app.ui.theme.MediumBlue
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.ui.theme.onSecondaryColor
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.utils.formatAddress
import com.example.weatherforecast_app.utils.formatNumberToLocale
import com.example.weatherforecast_app.utils.formatUnixTimestamp
import com.example.weatherforecast_app.utils.getDayOfWeek
import com.example.weatherforecast_app.utils.getHourlyForecast
import com.example.weatherforecast_app.utils.getWeatherIcon
import com.example.weatherforecast_app.utils.getWeeklyForecast
import com.example.weatherforecast_app.utils.metersPerSecondToMilesPerHour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

private const val TAG = "HomeScreen"


@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val locationState by viewModel.locationStateFlow.collectAsStateWithLifecycle()
    val currentWeatherState by viewModel.currentWeatherData.collectAsStateWithLifecycle()
    val weeklyWeatherState by viewModel.weeklyWeatherData.collectAsStateWithLifecycle()
    val searchedLocation by viewModel.searchedLocation.collectAsStateWithLifecycle()

    val userTempUnitPref = viewModel.getTemperatureUnitPref() ?: "metric"
    val userWindUnitPref = viewModel.getWindUnitPref() ?: "m/s"

    Log.i(TAG, "userTempUnitPref: $userTempUnitPref")
    var formattedAddress = ""
    var country =  ""
    val context = LocalContext.current

    Log.i(TAG, "language: ${LanguageHelper.getAppLocale(context).language}")
    locationState?.let { location ->
        viewModel.searchLocationByCoordinate(location.lat, location.lon, LanguageHelper.getAppLocale(context).language)
        searchedLocation?.let { response ->
             formattedAddress = response.features.firstOrNull()?.properties?.let { it1 ->
                formatAddress(it1.address)
            } ?: "UnKnown Place"
        }
    }

    LaunchedEffect(locationState) {
        locationState?.let {
            viewModel.getCurrentWeather(
                it.lat,
                it.lon,
                LanguageHelper.getAppLocale(context).language,
                userTempUnitPref
            )

            viewModel.getWeeklyWeather(
                it.lat,
                it.lon,
                LanguageHelper.getAppLocale(context).language,
                userTempUnitPref
            )
        }
    }

    when(currentWeatherState){
        ResponseState.Loading -> {
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground()
            ) {
                CircularProgressIndicator(color = onSecondaryColor)
            }
        }
        is ResponseState.Failure -> {
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
                        Log.i(TAG, "try again: $locationState")
                        locationState?.let {
                            viewModel.getCurrentWeather(
                                it.lat,
                                it.lon,
                                LanguageHelper.getAppLocale(context).language,
                                userTempUnitPref
                            )
                            viewModel.getWeeklyWeather(
                                it.lat,
                                it.lon,
                                LanguageHelper.getAppLocale(context).language,
                                userTempUnitPref
                            )
                        }
                    }
                ) {
                    Text(stringResource(R.string.try_again))
                }
            }
        }
        is ResponseState.Success -> {
            val successCurrentWeatherData = (currentWeatherState as ResponseState.Success).data as WeatherDTO

            when(weeklyWeatherState){
                is ResponseState.Failure ->
                    Column(

                    ) {
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

                    val successWeeklyWeatherData =
                        (weeklyWeatherState as ResponseState.Success).data as WeatherResponse

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .gradientBackground()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    ) {
                        Log.i(TAG, "address $formattedAddress: $country")
                            CurrentWeatherUI(
                                successCurrentWeatherData,
                                successWeeklyWeatherData.weatherDTOList,
                                userTempUnitPref,
                                userWindUnitPref,
                                formattedAddress,
                            )
                        }

                }

            }

        }
    }

}


@Composable
fun CurrentWeatherUI(
    currentWeatherData: WeatherDTO,
    weeklyWeatherData: List<WeatherDTO>,
    userTempUnitPref: String?,
    userWindUnitPref: String,
    place: String,

){
    val context = LocalContext.current
    val formatter = SimpleDateFormat("EEE, d MMM", Locale(LanguageHelper.getAppLocale(context).language))
    val date = formatter.format(Date())


    Column(
        modifier = Modifier.padding(start = 18.dp, top = 20.dp, end = 18.dp)
    ) {
        Text(
            text = "${place}\n$date",
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
        Image(
            painter = painterResource(getWeatherIcon(currentWeatherData.weather[0].icon)),
            contentScale = ContentScale.Fit,
            contentDescription = "current weather icon",
            modifier = Modifier.size(130.dp)
        )

        currentWeatherData.placeInfo.sunrise?.let { formatUnixTimestamp(it, context) }?.let {
            TemperatureDisplay(
                temperature = currentWeatherData.mainWeatherData.temperature.roundToInt(),
                feelsLikeTemp = currentWeatherData.mainWeatherData.feels_like.roundToInt(),
                weatherStatus = currentWeatherData.weather[0].main + " / " + currentWeatherData.weather[0].description,
                sunriseTime = it,
                unit = userTempUnitPref
            )
        }
        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconSquare(
                R.drawable.wind,
                stringResource(R.string.wind),
                if (userWindUnitPref == "m/s") currentWeatherData.wind.speed else metersPerSecondToMilesPerHour(
                    currentWeatherData.wind.speed
                ),
                if (userWindUnitPref == "m/s") stringResource(R.string.meter_per_sec) else stringResource(
                    R.string.mile_per_hour
                )
            )
            IconSquare(
                R.drawable.humidity,
                stringResource(R.string.humidity),
                currentWeatherData.mainWeatherData.humidity.toDouble(),
                "%"
            )
            IconSquare(
                R.drawable.pressure,
                stringResource(R.string.pressure),
                currentWeatherData.mainWeatherData.pressure.toDouble(),
                stringResource(R.string.pascal)
            )
            IconSquare(
                R.drawable.clouds,
                stringResource(R.string.clouds),
                currentWeatherData.clouds.all.toDouble(),
                "%"
            )
        }
        Spacer(Modifier.height(10.dp))
        HourlyWeather(getHourlyForecast(weeklyWeatherData), userTempUnitPref)
        Spacer(Modifier.height(10.dp))
        WeeklyWeather(getWeeklyForecast(weeklyWeatherData, context), userTempUnitPref)
    }
}


@Composable
fun TemperatureDisplay(temperature: Int,
                       weatherStatus: String,
                       feelsLikeTemp: Int,
                       unit: String?,
                       sunriseTime: String = "5:50"
) {
    val context = LocalContext.current

    val unitResId = when (unit) {
        "metric" -> R.string.celsius
        "imperial" -> R.string.fahrenheit
        else -> R.string.kelvin
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = formatNumberToLocale(temperature, context),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = stringResource(unitResId),
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
            text = "${stringResource(R.string.Feels_like)} ${formatNumberToLocale(feelsLikeTemp, context)}",
            color = Color.White.copy(alpha = .6f),
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(unitResId),
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
fun IconSquare(iconResId: Int, description: String, measurement: Double, unit: String){
    val context = LocalContext.current

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
                modifier = Modifier.size(30.dp)
            )

        }
        Spacer(Modifier.height(5.dp))
        Text(
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 13.sp
            )
        )

        Text(
            text = "${formatNumberToLocale(measurement, context)} $unit",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 15.sp
            )
        )
    }

}


@Composable
fun HourlyWeather(weatherList: List<WeatherDTO>, unit: String?){

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp))
    {
        items(weatherList.size) {
            HourlyWeatherItem(weatherList[it], unit)
        }
    }

}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyWeatherItem(weatherDTO: WeatherDTO, unit: String?){

    val context = LocalContext.current

    val unitResId = when (unit) {
        "metric" -> R.string.celsius
        "imperial" -> R.string.fahrenheit
        else -> R.string.kelvin
    }

    Box(
        modifier = Modifier
            .border(2.dp, LightBlue, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = formatUnixTimestamp(weatherDTO.dateTime.toLong(), context),
                color = Color.White,
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(Modifier.height(5.dp))
//            GlideImage(
//                model = "https://openweathermap.org/img/wn/${weatherDTO.weather[0].icon}@2x.png",
//                contentDescription = " ",
//                modifier = Modifier.size(30.dp),
//                contentScale = ContentScale.Fit,
//            )
            Image(
                painter = painterResource(getWeatherIcon(weatherDTO.weather[0].icon)),
                contentScale = ContentScale.Fit,
                contentDescription = "weather icon",
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.height(5.dp))
            Row {
                Text(
                    text = formatNumberToLocale(weatherDTO.mainWeatherData.temperature.roundToInt(), context),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = stringResource(unitResId),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
                )
            }
        }
    }

}

@Composable
fun WeeklyWeather(weatherList: List<WeatherDTO>, unit: String?){
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
                style = MaterialTheme.typography.titleMedium,
            )
        }
        items(weatherList.size){
            WeeklyWeatherItem(weatherList[it], unit)
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeeklyWeatherItem(weatherDTO: WeatherDTO, unit: String?){

    val context = LocalContext.current
    val dayOfWeek = if (getDayOfWeek(weatherDTO, context).trim() == "Today" && LanguageHelper.getAppLocale(context).language == "ar"){
        "اليوم"
    }else {
        getDayOfWeek(weatherDTO, context)
    }
    getDayOfWeek(weatherDTO, context)

    val unitResId = when (unit) {
        "metric" -> R.string.celsius
        "imperial" -> R.string.fahrenheit
        else -> R.string.kelvin
    }

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
            Image(
                painter = painterResource(getWeatherIcon(weatherDTO.weather[0].icon)),
                contentScale = ContentScale.Fit,
                contentDescription = "weather icon",
                modifier = Modifier
                    .size(30.dp)
                    .weight(1.5f),
            )
            Row(
                modifier = Modifier.weight(1.2f),
                verticalAlignment = Alignment.CenterVertically,
                ) {
                Text(

                    text = "${formatNumberToLocale(weatherDTO.mainWeatherData.temp_max.roundToInt(), context)} " +
                            "/ ${formatNumberToLocale(weatherDTO.mainWeatherData.temp_min.roundToInt(), context)}",
                    color = MediumBlue,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = stringResource(unitResId),
                    color = MediumBlue,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                    ),
                    modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
                )
            }
        }

    }
}