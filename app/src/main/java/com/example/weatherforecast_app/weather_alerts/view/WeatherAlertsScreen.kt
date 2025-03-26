package com.example.weatherforecast_app.weather_alerts.view

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecast_app.favorites.view.FavoriteLocations
import com.example.weatherforecast_app.ui.theme.MediumBlue
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.ui.theme.onSecondaryColor
import com.example.weatherforecast_app.weather_alerts.viewmodel.AlertsViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAlertsScreen(viewModel: AlertsViewModel,  onDateTimeSelected: (Long) -> Unit ) {

    val showDatePicker by viewModel.showDatePicker.collectAsStateWithLifecycle()
    val showTimePicker by viewModel.showTimePicker.collectAsStateWithLifecycle()
    val calendar =  Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        selectableDates = PresentSelectableDates
    )

    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = false,
    )


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                onClick = { viewModel.toggleDatePicker(true) },
                containerColor = onSecondaryColor,
            ) {
                Icon(Icons.Default.Add, tint = MediumBlue, contentDescription = "Add Alarm")
            } },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Weather Alerts",
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
        },

        ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .gradientBackground()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { viewModel.toggleDatePicker(false) },
                    confirmButton = {
                        Button(colors = ButtonColors(
                            containerColor = MediumBlue,
                            contentColor = Color.White,
                            disabledContentColor = MediumBlue,
                            disabledContainerColor = Color.White,
                        ),
                            onClick = {
                            val selectedDate = datePickerState.selectedDateMillis
                            if (selectedDate != null) {
                                calendar.timeInMillis = selectedDate
                                viewModel.saveSelectedDate(selectedDate)
                            }
                            viewModel.toggleDatePicker(false)
                            viewModel.toggleTimePicker(true)
                        }) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(
                        colors = DatePickerDefaults.colors(
                            headlineContentColor = MediumBlue,
                            selectedDayContentColor = Color.White,
                            selectedDayContainerColor = MediumBlue,
                            todayDateBorderColor = MediumBlue,

                    ),
                        state = datePickerState)
                }
            }

            if (showTimePicker) {
                TimePickerDialog(
                    onConfirm = {
                        Log.i("WeatherAlerts", "WeatherAlertsScreen: ${timePickerState.hour} ${timePickerState.minute}")
                        viewModel.toggleTimePicker(false)
                                },
                    onDismiss = {viewModel.toggleTimePicker(false)}
                ){
                    TimePicker(
                        state = timePickerState,
                    )
                }
            }
        }
    }
}


@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
object PresentSelectableDates: SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis()
    }
}