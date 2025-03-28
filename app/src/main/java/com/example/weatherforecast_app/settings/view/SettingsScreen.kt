package com.example.weatherforecast_app.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherforecast_app.settings.viewmodel.SettingsViewModel
import com.example.weatherforecast_app.ui.theme.gradientBackground
import com.example.weatherforecast_app.utils.LanguageHelper

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val selectedLanguage by viewModel.language.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(selectedLanguage) {
        LanguageHelper.setAppLocale(context, selectedLanguage)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        SettingItem(
            "Language",
            selectedLanguage,
            options = listOf("English", "Arabic", "System Default"),
            onOptionSelected = {
                viewModel.updateLanguage(it)
            }
        )

        HorizontalDivider()

        SettingItem(
            "Language",
            selectedLanguage,
            options = listOf("English", "Arabic", "System Default"),
            onOptionSelected = {

            }
        )

        HorizontalDivider()
        SettingItem(
            "Language",
            selectedLanguage,
            options = listOf("English", "Arabic", "System Default"),
            onOptionSelected = {

            }
        )


    }
}


@Composable
fun SettingItem(name: String,
                selectedText: String,
                options: List<String>,
                onOptionSelected: (String) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        Arrangement.SpaceEvenly
    ){
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge
        )
        CustomDropdownMenu(
            selectedText,
            options,
            onOptionSelected
        )
    }
}
@Composable
fun CustomDropdownMenu(
    selectedText: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(onClick = { expanded = true }) {
        Text(selectedText)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "CHOICES"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                },
                text = { Text(option) }
            )
        }
    }
}


@Preview
@Composable
fun Preview(){
    CustomDropdownMenu("Lang", listOf("english, arabic default"), {})
}