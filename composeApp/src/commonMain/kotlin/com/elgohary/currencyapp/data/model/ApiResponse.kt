package com.elgohary.currencyapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val meta: MetaData,
    val data: Map<String, CurrencyDto>
)

@Serializable
data class MetaData(
    @SerialName("last_updated_at")
    val lastUpdatedAt: String
)
