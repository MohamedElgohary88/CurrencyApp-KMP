package com.elgohary.currencyapp.data.remote

import com.elgohary.currencyapp.data.model.ApiResponse
import com.elgohary.currencyapp.data.model.Currency
import com.elgohary.currencyapp.data.model.CurrencyCode
import com.elgohary.currencyapp.data.model.toRealmObject
import com.elgohary.currencyapp.data.remote.api.CurrencyApiService
import com.elgohary.currencyapp.domain.repository.PreferencesRepository
import com.elgohary.currencyapp.util.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val preferences: PreferencesRepository
) : CurrencyApiService {

    companion object {
        private const val BASE_URL = "https://api.currencyapi.com/v3/latest"
        private const val API_KEY = "cur_live_ElcOuck38W3hx88HCt0vv9aG1gkF41EhqluPwiJT"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
        install(DefaultRequest) {
            headers {
                append("apikey", API_KEY)
            }
        }
    }

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(BASE_URL)

            if (response.status.isSuccess()) {
                // Deserialize the response
                val apiResponse = response.body<ApiResponse>()

                // Filter supported currencies
                val supportedCurrencies = CurrencyCode.entries.map { it.name }.toSet()
                val availableCurrencyDtos = apiResponse.data.values.filter { it.code in supportedCurrencies }

                // Map CurrencyDto to Currency (RealmObject)
                val availableCurrencies = availableCurrencyDtos.map { it.toRealmObject() }

                // Save the last updated time
                preferences.saveLastUpdated(Instant.parse(apiResponse.meta.lastUpdatedAt))

                RequestState.Success(data = availableCurrencies)
            } else {
                RequestState.Error("HTTP error: ${response.status.description}")
            }
        } catch (e: Exception) {
            RequestState.Error("Failed to fetch exchange rates: ${e.message}")
        }
    }

}
