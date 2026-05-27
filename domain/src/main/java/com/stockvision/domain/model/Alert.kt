package com.stockvision.domain.model

enum class AlertType {
    PRICE_ABOVE, PRICE_BELOW, RSI_OVERBOUGHT, RSI_OVERSOLD, BREAKOUT
}

data class Alert(
    val id: Int = 0,
    val symbol: String,
    val type: AlertType,
    val targetValue: Double,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
