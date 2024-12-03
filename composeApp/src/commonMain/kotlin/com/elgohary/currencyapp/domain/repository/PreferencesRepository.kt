package com.elgohary.currencyapp.domain.repository

import kotlinx.datetime.Instant

interface PreferencesRepository {
    suspend fun saveLastUpdated(lastUpdatedTime: Instant)
    suspend fun isDataFresh(currentTimestamp: Long): Boolean
}
