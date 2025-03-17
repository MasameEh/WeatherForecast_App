package com.example.weatherforecast_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecast_app.ui.theme.WeatherForecast_AppTheme

class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecast_AppTheme {
                navHostController = rememberNavController()
                SetUpNavHost()
            }
        }
    }
    @Composable
    fun SetUpNavHost(){

        NavHost(
            navController = navHostController,
            startDestination = ScreensRoute.Home,
            modifier = Modifier.fillMaxSize()
        ){
            composable<ScreensRoute.Home>{

            }

            composable<ScreensRoute.WeatherAlerts>{

            }

            composable<ScreensRoute.Favorites>{

            }

            composable<ScreensRoute.Settings>{

            }

        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier){
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize()
                .padding(innerPadding)
        ) {  }
    }
}