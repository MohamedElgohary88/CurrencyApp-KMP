package com.elgohary.currencyapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.elgohary.currencyapp.presentation.component.AmountInput
import com.elgohary.currencyapp.presentation.component.CurrencyInputs
import com.elgohary.currencyapp.presentation.component.HomeHeader
import com.elgohary.currencyapp.presentation.theme.surfaceColor
import com.elgohary.currencyapp.presentation.viewmodels.HomeUiEvent
import com.elgohary.currencyapp.presentation.viewmodels.HomeViewModel

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val rateStatus by viewModel.rateStatus
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency
        var amount by rememberSaveable { mutableStateOf(0.0) }

        Column(
            modifier = Modifier.fillMaxSize().background(surfaceColor)
        ) {
            HomeHeader(
                status = rateStatus,
                onRatesRefresh = {
                    viewModel.sendEvent(
                        HomeUiEvent.RefreshRates
                    )
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            CurrencyInputs(
                source = sourceCurrency,
                target = targetCurrency,
                onSwitchClick = {}
            )
            Spacer(modifier = Modifier.height(24.dp))
            AmountInput(amount = amount, onAmountChange = { amount = it })
        }
    }
}