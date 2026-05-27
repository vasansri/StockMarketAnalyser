package com.stockvision.sdk

import com.stockvision.domain.model.AIRecommendation
import com.stockvision.domain.model.Candle
import com.stockvision.domain.model.Holding
import com.stockvision.domain.model.StockQuote
import kotlinx.coroutines.flow.Flow

/**
 * Public Interface for the StockVision SDK.
 * This SDK provides high-level access to stock market analysis, AI recommendations, 
 * and portfolio management tools.
 */
interface StockVisionSDK {
    
    // --- Technical Analysis & Market Data ---
    fun getMarketQuote(symbol: String): Flow<StockQuote>
    fun getHistoricalData(symbol: String, interval: String): Flow<List<Candle>>
    
    // --- AI & Recommendations ---
    /**
     * Generates a trade signal (BUY/SELL/HOLD) based on technical indicators.
     */
    fun analyzeStock(symbol: String, candles: List<Candle>): AIRecommendation
    
    // --- Portfolio & Analytics ---
    fun getPortfolioSummary(): Flow<PortfolioAnalytics>
    fun trackHoldings(): Flow<List<Holding>>
}

data class PortfolioAnalytics(
    val totalInvested: Double,
    val totalCurrentValue: Double,
    val overallProfitLoss: Double,
    val dailyChangePercent: Double
)
