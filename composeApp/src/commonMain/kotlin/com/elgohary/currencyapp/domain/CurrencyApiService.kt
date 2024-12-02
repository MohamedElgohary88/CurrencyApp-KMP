package com.elgohary.currencyapp.domain

import com.elgohary.currencyapp.domain.model.Currency

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>
}