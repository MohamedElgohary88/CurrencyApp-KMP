package com.elgohary.currencyapp.data.remote.api

import com.elgohary.currencyapp.util.RequestState
import com.elgohary.currencyapp.data.model.Currency

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>
}