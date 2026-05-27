package com.stockvision.feature_portfolio.presentation

import androidx.lifecycle.viewModelScope
import com.stockvision.core.base.BaseViewModel
import com.stockvision.domain.model.Holding
import com.stockvision.domain.usecase.AddHoldingUseCase
import com.stockvision.domain.usecase.GetHoldingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PortfolioState(
    val isLoading: Boolean = false,
    val holdings: List<Holding> = emptyList(),
    val totalInvested: Double = 0.0,
    val totalCurrentValue: Double = 0.0,
    val totalProfitLoss: Double = 0.0,
    val totalProfitLossPercentage: Double = 0.0
)

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getHoldingsUseCase: GetHoldingsUseCase,
    private val addHoldingUseCase: AddHoldingUseCase
) : BaseViewModel<PortfolioState>(PortfolioState()) {

    init {
        observeHoldings()
        addDummyData()
    }

    private fun addDummyData() {
        viewModelScope.launch {
            // Check if already has data to avoid duplication on every init
            // For demo, we just add if empty
            delay(500) // Small delay to let initial flow emit
            if (uiState.value.holdings.isEmpty()) {
                addHoldingUseCase("RELIANCE", "Reliance Industries", 10, 2500.0)
                addHoldingUseCase("TCS", "Tata Consultancy Services", 5, 3800.0)
                addHoldingUseCase("HDFCBANK", "HDFC Bank Ltd", 20, 1450.0)
            }
        }
    }

    private fun observeHoldings() {
        getHoldingsUseCase().onEach { holdings ->
            val invested = holdings.sumOf { it.investedValue }
            val current = holdings.sumOf { it.currentValue }
            val pl = current - invested
            val plPercentage = if (invested != 0.0) (pl / invested) * 100 else 0.0

            updateState { 
                it.copy(
                    holdings = holdings,
                    totalInvested = invested,
                    totalCurrentValue = current,
                    totalProfitLoss = pl,
                    totalProfitLossPercentage = plPercentage
                )
            }
        }.launchIn(viewModelScope)
    }
}
