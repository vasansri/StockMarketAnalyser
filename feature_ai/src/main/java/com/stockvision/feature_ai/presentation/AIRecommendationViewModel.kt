package com.stockvision.feature_ai.presentation

import androidx.lifecycle.viewModelScope
import com.stockvision.core.base.BaseViewModel
import com.stockvision.core.util.ResultState
import com.stockvision.domain.engine.AIRecommendationEngine
import com.stockvision.domain.model.AIRecommendation
import com.stockvision.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class AIRecommendationState(
    val isLoading: Boolean = false,
    val recommendation: AIRecommendation? = null,
    val error: String? = null
)

@HiltViewModel
class AIRecommendationViewModel @Inject constructor(
    private val repository: StockRepository,
    private val aiEngine: AIRecommendationEngine
) : BaseViewModel<AIRecommendationState>(AIRecommendationState()) {

    fun getRecommendation(symbol: String) {
        repository.getHistoricalCandles(symbol, 0, 0).onEach { result ->
            when (result) {
                is ResultState.Loading -> updateState { it.copy(isLoading = true) }
                is ResultState.Success -> {
                    val rec = aiEngine.generateRecommendation(symbol, result.data)
                    updateState { it.copy(isLoading = false, recommendation = rec) }
                }
                is ResultState.Error -> updateState { it.copy(isLoading = false, error = result.message) }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}
