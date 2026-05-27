package com.stockvision.feature_chart.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.stockvision.core.base.BaseViewModel
import com.stockvision.core.util.ResultState
import com.stockvision.domain.model.Candle
import com.stockvision.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class StockChartState(
    val isLoading: Boolean = false,
    val candles: List<Candle> = emptyList(),
    val error: String? = null,
    val symbol: String = ""
)

@HiltViewModel
class StockChartViewModel @Inject constructor(
    private val repository: StockRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<StockChartState>(StockChartState()) {

    init {
        val symbol = savedStateHandle.get<String>("symbol") ?: ""
        updateState { it.copy(symbol = symbol) }
        loadChartData(symbol)
    }

    private fun loadChartData(symbol: String) {
        repository.getHistoricalCandles(symbol, 0, 0).onEach { result ->
            when (result) {
                is ResultState.Loading -> updateState { it.copy(isLoading = true) }
                is ResultState.Success -> updateState { it.copy(isLoading = false, candles = result.data) }
                is ResultState.Error -> updateState { it.copy(isLoading = false, error = result.message) }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}
