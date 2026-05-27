package com.stockvision.domain.usecase

import com.stockvision.domain.model.Holding
import com.stockvision.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHoldingsUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    operator fun invoke(): Flow<List<Holding>> = repository.getHoldings()
}

class AddHoldingUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(symbol: String, name: String, quantity: Int, averagePrice: Double) {
        repository.addHolding(symbol, name, quantity, averagePrice)
    }
}
