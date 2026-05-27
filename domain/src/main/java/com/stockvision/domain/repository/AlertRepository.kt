package com.stockvision.domain.repository

import com.stockvision.domain.model.Alert
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAlerts(): Flow<List<Alert>>
    suspend fun addAlert(alert: Alert)
    suspend fun toggleAlert(id: Int, isEnabled: Boolean)
    suspend fun deleteAlert(id: Int)
}
