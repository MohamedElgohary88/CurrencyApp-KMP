package com.elgohary.currencyapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform