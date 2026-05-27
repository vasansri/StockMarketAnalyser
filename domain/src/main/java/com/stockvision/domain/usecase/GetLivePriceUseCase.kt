package com.stockvision.domain.usecase

import com.stockvision.domain.model.StockTicker
import com.stockvision.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLivePriceUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke(symbols: List<String>): Flow<StockTicker> {
        return repository.getLiveStockPrice(symbols)
    }
}
