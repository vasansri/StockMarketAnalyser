package com.stockvision.data.repository

import com.stockvision.data.local.HoldingDao
import com.stockvision.data.local.HoldingEntity
import com.stockvision.domain.model.Holding
import com.stockvision.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val holdingDao: HoldingDao
) : PortfolioRepository {

    override fun getHoldings(): Flow<List<Holding>> {
        return holdingDao.getAllHoldings().map { entities ->
            entities.map { entity ->
                // In a real app, currentPrice would come from an API or cache
                Holding(
                    id = entity.id,
                    symbol = entity.symbol,
                    name = entity.name,
                    quantity = entity.quantity,
                    averagePrice = entity.averagePrice,
                    currentPrice = entity.averagePrice * 1.1 // Mocking a 10% profit
                )
            }
        }
    }

    override suspend fun addHolding(symbol: String, name: String, quantity: Int, averagePrice: Double) {
        holdingDao.insertHolding(
            HoldingEntity(
                symbol = symbol,
                name = name,
                quantity = quantity,
                averagePrice = averagePrice
            )
        )
    }

    override suspend fun deleteHolding(id: Int) {
        holdingDao.deleteHolding(id)
    }
}
