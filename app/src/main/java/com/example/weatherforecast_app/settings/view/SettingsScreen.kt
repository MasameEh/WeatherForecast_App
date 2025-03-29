package com.example.weatherforecast_app.settings.view


import androidx.annotation.StringRes
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import com.example.weatherforecast_app.R
import com.example.weatherforecast_app.settings.viewmodel.SettingsViewModel
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.utils.LanguageHelper

var i = 0

private const val TAG = "SettingsScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    var selectedLanguage by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        selectedLanguage = viewModel.getLanguagePref() ?: "System Default"
        LanguageHelper.setAppLocale(context, selectedLanguage)
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
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            SettingItem(
                name = stringResource(R.string.lang),
                options = listOf(
                    stringResource(R.string.en),
                    stringResource(R.string.ar),
                    stringResource(R.string.system_def)
                ),
                selectedOption = selectedLanguage,
                onOptionSelected = { newLanguage ->
                    selectedLanguage = newLanguage
                    viewModel.updateLanguage(newLanguage)
                    LanguageHelper.setAppLocale(context, selectedLanguage)
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun SettingItem(
    name: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    label = { Text(text = option, color = Color.White) },
                )
            }
        }
    }
}

@Composable
fun CustomDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column{
        OutlinedButton(
            onClick = { expanded = true }
        ) {
            Text(
                text = selectedOption,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Language options",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = option,
                            color = if (option == selectedOption) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                HorizontalDivider()
            }

        }
    }
}

@StringRes
fun getLanguageResource(languageCode: String): Int {
    return when (languageCode) {
        "English" -> R.string.en
        "Arabic" -> R.string.ar
        else -> R.string.system_def
    }
}