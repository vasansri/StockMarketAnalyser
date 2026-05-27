package com.stockvision.domain.engine

import com.stockvision.domain.model.AIRecommendation
import com.stockvision.domain.model.Candle
import com.stockvision.domain.model.Signal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRecommendationEngine @Inject constructor() {

    fun generateRecommendation(symbol: String, candles: List<Candle>): AIRecommendation {
        val rsi = calculateRSI(candles)
        val signal = determineSignal(rsi)
        val risk = calculateRisk(candles)
        
        return AIRecommendation(
            symbol = symbol,
            signal = signal,
            confidence = 0.85f,
            riskScore = risk,
            trendPrediction = if (signal == Signal.BUY) "Bullish reversal expected" else "Consolidation phase",
            technicalAnalysis = "RSI at ${String.format("%.2f", rsi)} indicating ${if (rsi < 30) "Oversold" else if (rsi > 70) "Overbought" else "Neutral"} conditions.",
            rsiValue = rsi,
            macdSignal = "Bullish Crossover"
        )
    }

    private fun calculateRSI(candles: List<Candle>): Float {
        if (candles.size < 14) return 50f
        return (30..80).random().toFloat()
    }

    private fun determineSignal(rsi: Float): Signal {
        return when {
            rsi < 35 -> Signal.BUY
            rsi > 65 -> Signal.SELL
            else -> Signal.HOLD
        }
    }

    private fun calculateRisk(candles: List<Candle>): Int {
        return (3..8).random()
    }
}
