package com.stockvision.domain.repository

import com.stockvision.core.util.ResultState
import com.stockvision.domain.model.Candle
import com.stockvision.domain.model.StockQuote
import com.stockvision.domain.model.StockTicker
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getStockQuote(symbol: String): Flow<ResultState<StockQuote>>
    fun getLiveStockPrice(symbols: List<String>): Flow<StockTicker>
    fun getHistoricalCandles(symbol: String, from: Long, to: Long): Flow<ResultState<List<Candle>>>
}
