package com.example.weatherforecast_app.settings.view


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.settings.viewmodel.SettingsViewModel
import com.example.weatherforecast_app.ui.theme.LightBlue

import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.utils.LanguageHelper
import com.example.weatherforecast_app.utils.NotificationHelper



private const val TAG = "SettingsScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel,
                   navigateToMap: () -> Unit,
                   ) {

    var selectedLanguage by remember { mutableStateOf("") }
    var selectedTempUnit by remember { mutableStateOf("") }
    var selectedWindUnit by remember { mutableStateOf("") }
    var selectedNotificationStatus by remember { mutableStateOf(true) }
    var selectedLocation by remember { mutableStateOf("GPS") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        selectedLanguage = settingsViewModel.getLanguagePref() ?: "System Default"
        selectedTempUnit = settingsViewModel.getTemperatureUnitPref() ?: "Celsius"
        selectedWindUnit = settingsViewModel.getWindUnitPref() ?: "m/s"
        selectedNotificationStatus = settingsViewModel.getUserNotificationStatus()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },

        ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .gradientBackground()
                .padding(start = 8.dp, end = 8.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row {
                SettingsItem(
                    Modifier.weight(1f),
                    name = stringResource(R.string.lang),
                    listOf("English", "Arabic", "System Default"),
                    onOptionSelected = {
                        Log.i(TAG, "SettingsScreen: $it")
                        selectedLanguage = it
                        settingsViewModel.updateLanguage(it)
                        LanguageHelper.setAppLocale(context, it)
                    },
                    selectedOption = selectedLanguage,
                )
                Spacer(Modifier.width(10.dp))
                SettingsItem(
                    Modifier.weight(1f),
                    name = stringResource(R.string.temp_unit),
                    listOf("Celsius", "Fahrenheit", "Kelvin"), {
                        selectedTempUnit = it
                        settingsViewModel.updateTemperatureUnit(it)
                    },
                    selectedTempUnit)
            }
            Spacer(Modifier.width(20.dp))
            Row {
                SettingsItem(
                    Modifier.weight(1f),
                    name = stringResource(R.string.wind_speed_unit),
                    listOf("m/s", "m/h"),
                    onOptionSelected = {
                        selectedWindUnit = it
                        settingsViewModel.updateWindSpeedUnit(it)
                    },
                    selectedOption = selectedWindUnit,
                )
                Spacer(Modifier.width(10.dp))
                SettingsItem(
                    Modifier.weight(1f),
                    name = stringResource(R.string.notification),
                    listOf("Enable", "Disable"), {
                        selectedNotificationStatus = it == "Enable"
                        if(it == "Enable"){
                            NotificationHelper.enableNotifications(context)
                        }else NotificationHelper.disableAllNotifications(context)

                        settingsViewModel.updateUserNotificationStatus(selectedNotificationStatus)
                    },
                    if(selectedNotificationStatus) "Enable" else "Disable"
                )
            }
            SettingsItem(
                Modifier,
                name = stringResource(R.string.location),
                listOf("GPS", "MAP"), {
                    selectedLocation = it
                    if(it == "MAP") navigateToMap()

                },
                selectedLocation
            )
        }
    }
}



@Composable
fun SettingsItem(
    modifier: Modifier,
    name: String,
    radioOptions: List<String>,
    onOptionSelected: (String) -> Unit,
    selectedOption:  String,
) {

    Column(
        modifier
            .background(LightBlue.copy(alpha = 0.5f))
            .fillMaxWidth()
            .border(2.dp, LightBlue, shape = RoundedCornerShape(8.dp))
    ) {

        Box(
            Modifier.fillMaxWidth().padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = name,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                fontSize = 22.sp,
                style = MaterialTheme.typography.labelMedium.copy(lineHeight = 30.sp)
            )
        }
        Column(Modifier.selectableGroup()) {
            radioOptions.forEach { text ->

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected =( text  == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = stringResource(getResource(text)),
                        style = MaterialTheme.typography.labelMedium.copy(lineHeight = 25.sp),
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}



@StringRes
fun getResource(resource: String): Int {
    return when (resource) {
        "English" -> R.string.en
        "Arabic" -> R.string.ar
        "Celsius" -> R.string.c
        "Fahrenheit" -> R.string.f
        "Kelvin" -> R.string.k
        "m/s" -> R.string.meter_per_sec
        "m/h" -> R.string.mile_per_hour
        "MAP" -> R.string.map
        "GPS" -> R.string.gps
        "Enable" -> R.string.enable
        "Disable" -> R.string.disable
        else -> R.string.system_def
    }
}

