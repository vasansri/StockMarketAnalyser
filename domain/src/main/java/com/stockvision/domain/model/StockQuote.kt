package com.stockvision.domain.model

data class StockQuote(
    val symbol: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val previousClose: Double,
    val volume: Long
)
