package com.elgohary.currencyapp.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import com.elgohary.currencyapp.data.remote.api.CurrencyApiServiceImpl

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            CurrencyApiServiceImpl().getLatestExchangeRates()
        }
    }
}