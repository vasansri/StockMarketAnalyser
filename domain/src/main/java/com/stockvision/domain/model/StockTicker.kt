package com.stockvision.domain.model

data class StockTicker(
    val symbol: String,
    val price: Double,
    val timestamp: Long
)
