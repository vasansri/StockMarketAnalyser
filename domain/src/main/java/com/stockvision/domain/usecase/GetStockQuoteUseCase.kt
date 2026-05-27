package com.stockvision.domain.usecase

import com.stockvision.core.util.ResultState
import com.stockvision.domain.model.StockQuote
import com.stockvision.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStockQuoteUseCase @Inject constructor(
    private val repository: StockRepository
) {
    operator fun invoke(symbol: String): Flow<ResultState<StockQuote>> {
        return repository.getStockQuote(symbol)
    }
}
