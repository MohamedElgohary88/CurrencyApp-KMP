package com.elgohary.currencyapp.data.model

sealed class CurrencyType(val code: CurrencyCode) {
    data class Source(val currencyCode: CurrencyCode): CurrencyType(currencyCode)
    data class Target(val currencyCode: CurrencyCode): CurrencyType(currencyCode)
    data object None: CurrencyType(CurrencyCode.USD)
}