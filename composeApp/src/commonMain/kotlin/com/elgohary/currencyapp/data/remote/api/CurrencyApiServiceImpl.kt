package com.elgohary.currencyapp.data.remote.api

import com.elgohary.currencyapp.domain.CurrencyApiService
import com.elgohary.currencyapp.domain.RequestState
import com.elgohary.currencyapp.domain.model.ApiResponse
import com.elgohary.currencyapp.domain.model.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl : CurrencyApiService {

    companion object {
        const val BASE_URL = "https://api.currencyapi.com/v3/latest?apikey"
        const val API_KEY = "cur_live_ElcOuck38W3hx88HCt0vv9aG1gkF41EhqluPwiJT"
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
            if (response.status.value == 200) {
                println("API Response: " + response.body<String>())
                val apiResponse = Json.decodeFromString<ApiResponse>(response.body())
                RequestState.Success(data = apiResponse.data.values.toList())
            } else {
                RequestState.Error(message = "Http error: " + response.status.description)
            }
        } catch (e: Exception) {
            RequestState.Error(e.message.toString())
        }
    }
}