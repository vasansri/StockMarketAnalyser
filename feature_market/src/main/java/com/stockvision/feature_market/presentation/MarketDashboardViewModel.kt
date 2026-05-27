package com.stockvision.feature_market.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.stockvision.core.base.BaseViewModel
import com.stockvision.core.util.ResultState
import com.stockvision.domain.usecase.GetLivePriceUseCase
import com.stockvision.domain.usecase.GetStockQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@Immutable
data class MarketDashboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userName: String = "John Doe",
    val niftyValue: String = "22,040.70",
    val niftyChange: String = "+120.50 (0.55%)",
    val niftyHistory: List<Double> = emptyList(),
    val sensexValue: String = "72,643.43",
    val sensexChange: String = "+450.20 (0.62%)",
    val sensexHistory: List<Double> = emptyList(),
    val trendingStocks: List<StockUiModel> = emptyList(),
    val topGainers: List<StockUiModel> = emptyList(),
    val topLosers: List<StockUiModel> = emptyList(),
    val portfolioValue: String = "₹12,45,000.00",
    val portfolioChange: String = "+₹24,500.00 (2.01%)",
    val livePrices: Map<String, Double> = emptyMap()
)

@Immutable
data class StockUiModel(
    val symbol: String,
    val name: String,
    val price: String,
    val change: String,
    val isPositive: Boolean,
    val lastUpdated: Long = 0,
    val priceHistory: List<Double> = emptyList()
)

@HiltViewModel
class MarketDashboardViewModel @Inject constructor(
    private val getStockQuoteUseCase: GetStockQuoteUseCase,
    private val getLivePriceUseCase: GetLivePriceUseCase
) : BaseViewModel<MarketDashboardState>(MarketDashboardState()) {

    init {
        loadMarketQuote("AAPL")
        startLivePriceUpdates(listOf("AAPL", "TSLA", "GOOGL", "NIFTY", "SENSEX"))
    }

    private fun startLivePriceUpdates(symbols: List<String>) {
        getLivePriceUseCase(symbols).onEach { ticker ->
            updateState { state ->
                when (ticker.symbol) {
                    "NIFTY" -> {
                        val newHistory = (state.niftyHistory + ticker.price).takeLast(20)
                        state.copy(
                            niftyValue = "₹${ticker.price}",
                            niftyHistory = newHistory
                        )
                    }
                    "SENSEX" -> {
                        val newHistory = (state.sensexHistory + ticker.price).takeLast(20)
                        state.copy(
                            sensexValue = "₹${ticker.price}",
                            sensexHistory = newHistory
                        )
                    }
                    else -> {
                        val updatedTrending = state.trendingStocks.map { stock ->
                            if (stock.symbol == ticker.symbol) {
                                val newHistory = (stock.priceHistory + ticker.price).takeLast(20)
                                stock.copy(
                                    price = "₹${ticker.price}",
                                    lastUpdated = System.currentTimeMillis(),
                                    priceHistory = newHistory
                                )
                            } else stock
                        }
                        state.copy(
                            livePrices = state.livePrices + (ticker.symbol to ticker.price),
                            trendingStocks = updatedTrending
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadMarketQuote(symbol: String) {
        getStockQuoteUseCase(symbol).onEach { result ->
            when (result) {
                is ResultState.Loading -> {
                    updateState { it.copy(isLoading = true, error = null) }
                }
                is ResultState.Success -> {
                    val stock = result.data
                    // Map domain model to UI model
                    val uiStock = StockUiModel(
                        symbol = stock.symbol,
                        name = "Stock Name", // Ideally from another API or local DB
                        price = "₹${stock.price}",
                        change = "${stock.change} (${stock.changePercent}%)",
                        isPositive = stock.change >= 0
                    )
                    updateState { 
                        it.copy(
                            isLoading = false, 
                            trendingStocks = it.trendingStocks + uiStock 
                        ) 
                    }
                }
                is ResultState.Error -> {
                    updateState { it.copy(isLoading = false, error = result.message) }
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}
