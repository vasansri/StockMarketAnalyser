package com.stockvision.domain.model

enum class Signal {
    BUY, SELL, HOLD
}

data class AIRecommendation(
    val symbol: String,
    val signal: Signal,
    val confidence: Float,
    val riskScore: Int,
    val trendPrediction: String,
    val technicalAnalysis: String,
    val rsiValue: Float,
    val macdSignal: String
)
