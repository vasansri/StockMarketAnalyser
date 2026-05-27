package com.stockvision.data.repository

import com.stockvision.data.local.AlertDao
import com.stockvision.data.local.AlertEntity
import com.stockvision.domain.model.Alert
import com.stockvision.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val alertDao: AlertDao
) : AlertRepository {

    override fun getAlerts(): Flow<List<Alert>> {
        return alertDao.getAllAlerts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addAlert(alert: Alert) {
        alertDao.insertAlert(alert.toEntity())
    }

    override suspend fun toggleAlert(id: Int, isEnabled: Boolean) {
        alertDao.updateAlertStatus(id, isEnabled)
    }

    override suspend fun deleteAlert(id: Int) {
        alertDao.deleteAlert(id)
    }

    private fun AlertEntity.toDomain() = Alert(
        id = id,
        symbol = symbol,
        type = type,
        targetValue = targetValue,
        isEnabled = isEnabled,
        createdAt = createdAt
    )

    private fun Alert.toEntity() = AlertEntity(
        id = id,
        symbol = symbol,
        type = type,
        targetValue = targetValue,
        isEnabled = isEnabled,
        createdAt = createdAt
    )
}
