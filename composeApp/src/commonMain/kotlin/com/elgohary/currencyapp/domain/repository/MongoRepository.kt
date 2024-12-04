package com.elgohary.currencyapp.domain.repository

import com.elgohary.currencyapp.data.model.Currency
import com.elgohary.currencyapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoRepository {
    fun configureTheRealm()
    suspend fun insertCurrencyData(currency: Currency)
    fun readCurrencyData(): Flow<RequestState<List<Currency>>>
    suspend fun cleanUp()
}