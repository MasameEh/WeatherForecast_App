package com.example.weatherforecast_app.favorites.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherforecast_app.ui.theme.gradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navigation: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigation.invoke() },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Location")
            } },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite Locations",
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
                .padding(innerPadding).fillMaxSize()
                .gradientBackground()
        ) {
            Text(text = "FavoritesScreen", color = Color.White)
        }
    }

}