package com.stockvision.domain.repository

import com.stockvision.domain.model.Holding
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getHoldings(): Flow<List<Holding>>
    suspend fun addHolding(symbol: String, name: String, quantity: Int, averagePrice: Double)
    suspend fun deleteHolding(id: Int)
}
