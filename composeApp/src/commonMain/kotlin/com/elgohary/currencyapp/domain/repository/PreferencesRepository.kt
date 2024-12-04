package com.elgohary.currencyapp.domain.repository

import com.elgohary.currencyapp.data.model.CurrencyCode
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface PreferencesRepository {
    suspend fun saveLastUpdated(lastUpdatedTime: Instant)
    suspend fun isDataFresh(currentTimestamp: Long): Boolean
    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    fun readSourceCurrencyCode(): Flow<CurrencyCode>
    fun readTargetCurrencyCode(): Flow<CurrencyCode>
}
