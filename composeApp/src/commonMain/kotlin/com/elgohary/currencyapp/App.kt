package com.elgohary.currencyapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.elgohary.currencyapp.presentation.screens.HomeScreen
import com.elgohary.currencyapp.ui.theme.DarkColors
import com.elgohary.currencyapp.ui.theme.LightColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val colors = if (!isSystemInDarkTheme()) LightColors else DarkColors

    MaterialTheme(colorScheme = colors) {
        Navigator(HomeScreen())
    }
}