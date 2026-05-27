package com.stockvision.data.repository

import com.google.gson.Gson
import com.stockvision.core.network.WebSocketManager
import com.stockvision.core.util.ResultState
import com.stockvision.core.util.ErrorHandler
import com.stockvision.data.remote.StockApi
import com.stockvision.data.remote.dto.toDomain
import com.stockvision.domain.model.Candle
import com.stockvision.domain.model.StockQuote
import com.stockvision.domain.model.StockTicker
import com.stockvision.domain.repository.StockRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val webSocketManager: WebSocketManager,
    private val gson: Gson
) : StockRepository {
    
    override fun getStockQuote(symbol: String): Flow<ResultState<StockQuote>> = flow {
        emit(ResultState.Loading)
        try {
            val response = api.getStockQuote(symbol = symbol, apiKey = "DEMO")
            val domainModel = response.globalQuote.toDomain()
            emit(ResultState.Success(domainModel))
        } catch (e: Exception) {
            val errorMessage = ErrorHandler.handleError(e)
            emit(ResultState.Error(errorMessage, e))
        }
    }

    override fun getHistoricalCandles(symbol: String, from: Long, to: Long): Flow<ResultState<List<Candle>>> = flow {
        emit(ResultState.Loading)
        // Dummy data for demonstration
        val candles = (0 until 50).map { i ->
            Candle(
                high = 150f + (0..10).random().toFloat(),
                low = 140f - (0..10).random().toFloat(),
                open = 145f,
                close = 148f,
                timestamp = System.currentTimeMillis() - (50 - i) * 3600000
            )
        }
        emit(ResultState.Success(candles))
    }

    override fun getLiveStockPrice(symbols: List<String>): Flow<StockTicker> {
        // Connect to a public WebSocket domain and port (Example: Finnhub or a custom socket server)
        // You can use the URL version:
         webSocketManager.connect("wss://ws.finnhub.io?token=d8bg1apr01qu2eqh5hlgd8bg1apr01qu2eqh5hm0")
        
        // Or use the domain and port version for custom servers:
//        webSocketManager.connect(host = "ws.stockvision.com", port = 8080, useSsl = true)
        
        // Subscribe to symbols
        symbols.forEach { symbol ->
//            println("webSocketManager 1 "+symbol)
            webSocketManager.sendMessage("{\"type\":\"subscribe\",\"symbol\":\"$symbol\"}")
        }

        return webSocketManager.messages
            .mapNotNull { message ->
                try {
//                    println("webSocketManager 2 "+message)
                    // Simple parsing for demo
                    // Expecting something like: {"data":[{"p":123,"s":"AAPL","t":12345}],"type":"trade"}
                    val data = gson.fromJson(message, WebSocketData::class.java)
                    data.trades?.firstOrNull()?.let { trade ->
                        StockTicker(trade.symbol, trade.price, trade.timestamp)
                    }
                } catch (e: Exception) {
                    null
                }
            }
    }

    private data class WebSocketData(val trades: List<TradeDto>?, val type: String)
    private data class TradeDto(val p: Double, val s: String, val t: Long) {
        val price get() = p
        val symbol get() = s
        val timestamp get() = t
    }
}
