package com.stockvision.feature_alerts.presentation

import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.stockvision.core.base.BaseViewModel
import com.stockvision.domain.model.Alert
import com.stockvision.domain.model.AlertType
import com.stockvision.domain.repository.AlertRepository
import com.stockvision.feature_alerts.worker.AlertWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class AlertsState(
    val alerts: List<Alert> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val alertRepository: AlertRepository,
    private val workManager: WorkManager
) : BaseViewModel<AlertsState>(AlertsState()) {

    init {
        observeAlerts()
        scheduleAlertWorker()
    }

    private fun observeAlerts() {
        alertRepository.getAlerts().onEach { alerts ->
            updateState { it.copy(alerts = alerts) }
        }.launchIn(viewModelScope)
    }

    private fun scheduleAlertWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val alertRequest = PeriodicWorkRequestBuilder<AlertWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "AlertWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            alertRequest
        )
    }

    fun addPriceAlert(symbol: String, price: Double, isAbove: Boolean) {
        viewModelScope.launch {
            val type = if (isAbove) AlertType.PRICE_ABOVE else AlertType.PRICE_BELOW
            alertRepository.addAlert(
                Alert(symbol = symbol, type = type, targetValue = price)
            )
        }
    }

    fun toggleAlert(id: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            alertRepository.toggleAlert(id, isEnabled)
        }
    }

    fun deleteAlert(id: Int) {
        viewModelScope.launch {
            alertRepository.deleteAlert(id)
        }
    }
}
