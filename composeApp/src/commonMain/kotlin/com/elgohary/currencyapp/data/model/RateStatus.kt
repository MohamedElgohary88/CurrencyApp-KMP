package com.elgohary.currencyapp.data.model

import androidx.compose.ui.graphics.Color
import com.elgohary.currencyapp.presentation.theme.freshColor

enum class RateStatus(
    val title: String,
    val color: Color
) {
    Idle(title = "Rates", color = Color.White),
    Fresh(title = "Fresh rates", color = freshColor),
    Stale(title = "Fresh rates", color = freshColor)
}