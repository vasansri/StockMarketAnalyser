package com.stockvision.domain.model

data class Candle(
    val high: Float,
    val low: Float,
    val open: Float,
    val close: Float,
    val timestamp: Long
)
