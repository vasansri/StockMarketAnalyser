package com.stockvision.sdk

import com.stockvision.core.util.ResultState
import com.stockvision.domain.engine.AIRecommendationEngine
import com.stockvision.domain.model.*
import com.stockvision.domain.repository.PortfolioRepository
import com.stockvision.domain.repository.StockRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockVisionSDKImpl @Inject constructor(
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val aiEngine: AIRecommendationEngine
) : StockVisionSDK {

    override fun getMarketQuote(symbol: String): Flow<StockQuote> {
        return stockRepository.getStockQuote(symbol)
            .filterIsInstance<ResultState.Success<StockQuote>>()
            .map { it.data }
    }

    override fun getHistoricalData(symbol: String, interval: String): Flow<List<Candle>> {
        return stockRepository.getHistoricalCandles(symbol, 0, 0)
            .filterIsInstance<ResultState.Success<List<Candle>>>()
            .map { it.data }
    }

    override fun analyzeStock(symbol: String, candles: List<Candle>): AIRecommendation {
        return aiEngine.generateRecommendation(symbol, candles)
    }

    override fun getPortfolioSummary(): Flow<PortfolioAnalytics> {
        return portfolioRepository.getHoldings().map { holdings ->
            val invested = holdings.sumOf { it.investedValue }
            val current = holdings.sumOf { it.currentValue }
            val pl = current - invested
            val dailyChange = if (invested != 0.0) (pl / invested) * 100 else 0.0
            
            PortfolioAnalytics(
                totalInvested = invested,
                totalCurrentValue = current,
                overallProfitLoss = pl,
                dailyChangePercent = dailyChange
            )
        }
    }

    override fun trackHoldings(): Flow<List<Holding>> {
        return portfolioRepository.getHoldings()
    }
}
