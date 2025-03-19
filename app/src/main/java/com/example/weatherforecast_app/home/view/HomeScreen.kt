package com.example.weatherforecast_app.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.ui.theme.DarkBlue
import com.example.weatherforecast_app.ui.theme.gradientBackground


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Weather Forecast",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .gradientBackground()
        ) {
            CurrentWeatherUI()
        }
    }
}


@Composable
fun CurrentWeatherUI(){

    Column(
        modifier = Modifier.padding(start = 18.dp, top = 10.dp)
    ) {
        Text(
            text = "Cairo, EG\nSat, 6 Aug",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 18.dp, top = 10.dp).fillMaxWidth()
    ) {
        Text(
            text = "Today",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
        )
        Image(
            painter = painterResource(R.drawable.raining),
            contentScale = ContentScale.Fit,
            contentDescription =  null,
            modifier = Modifier.size(180.dp)
        )
        TemperatureDisplay("18")
    }
}


@Composable
fun TemperatureDisplay(temperature: String,
                       feelsLikeTemp: String = "25",
                       unit: String = "°C",
                       time: String = "5:50"
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
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = unit,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
    Spacer(Modifier.height(10.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Feels like $feelsLikeTemp",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = unit,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = ".",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
        )
        Image(
            painter = painterResource(R.drawable.sunrise),
            contentScale = ContentScale.Fit,
            contentDescription =  null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = "Sunrise $time",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )
    }
}



