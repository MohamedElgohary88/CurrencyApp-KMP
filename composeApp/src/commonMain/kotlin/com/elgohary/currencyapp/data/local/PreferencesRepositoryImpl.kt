package com.elgohary.currencyapp.data.local

import com.elgohary.currencyapp.domain.repository.PreferencesRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalSettingsApi::class)
class PreferencesRepositoryImpl(
    private val settings: Settings,
    private val clock: Clock = Clock.System // Allows testing with custom clocks
) : PreferencesRepository {

    companion object {
        private const val TIME_STAMP_KEY = "lastUpdatedTime"
        private const val DEFAULT_TIMESTAMP = 0L // Avoid magic numbers
    }

    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    /**
     * Updates the last synchronization time in settings.
     *
     * @param lastUpdatedTime The time to save as the last sync, as an Instant.
     */
    override suspend fun saveLastUpdated(lastUpdatedTime: Instant) {
        try {
            flowSettings.putLong(
                key = TIME_STAMP_KEY,
                value = lastUpdatedTime.toEpochMilliseconds()
            )
        } catch (e: Exception) {
            // Log or handle error as necessary
            throw IllegalStateException("Failed to save last sync time", e)
        }
    }

    /**
     * Checks whether the data is still fresh (e.g., updated within 24 hours).
     *
     * @param currentTimestamp The current timestamp to compare against, as milliseconds.
     * @return true if the data is fresh; false otherwise.
     */
    override suspend fun isDataFresh(currentTimestamp: Long): Boolean {
        val savedTimeStamp = flowSettings.getLong(
            key = TIME_STAMP_KEY,
            defaultValue = DEFAULT_TIMESTAMP
        )

        if (savedTimeStamp == DEFAULT_TIMESTAMP) return false

        val currentInstant = Instant.fromEpochMilliseconds(currentTimestamp)
        val savedInstant = Instant.fromEpochMilliseconds(savedTimeStamp)

        return isSameDay(savedInstant, currentInstant)
    }

    /**
     * Checks if two Instants fall on the same calendar day.
     */
    private fun isSameDay(savedInstant: Instant, currentInstant: Instant): Boolean {
        val savedDateTime = savedInstant.toLocalDateTime(clock.systemZone)
        val currentDateTime = currentInstant.toLocalDateTime(clock.systemZone)

        return savedDateTime.date == currentDateTime.date
    }

    private val Clock.systemZone: TimeZone
        get() = TimeZone.currentSystemDefault()
}
