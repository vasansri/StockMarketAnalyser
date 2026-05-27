package com.stockvision.domain.model

data class Holding(
    val id: Int = 0,
    val symbol: String,
    val name: String,
    val quantity: Int,
    val averagePrice: Double,
    val currentPrice: Double,
    val investedValue: Double = quantity * averagePrice,
    val currentValue: Double = quantity * currentPrice,
    val profitLoss: Double = currentValue - investedValue,
    val profitLossPercentage: Double = if (investedValue != 0.0) (profitLoss / investedValue) * 100 else 0.0
)
