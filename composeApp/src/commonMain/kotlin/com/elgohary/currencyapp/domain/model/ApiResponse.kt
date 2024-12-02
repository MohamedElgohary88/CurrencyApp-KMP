package com.elgohary.currencyapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val meta: MetaData,
    val data: Map<String, Currency>
)

@Serializable
data class MetaData(
    @SerialName("last_updated_at")
    val last_updated_at: String
)

@Serializable
data class Currency(
    val name: String,
    val code: String
)