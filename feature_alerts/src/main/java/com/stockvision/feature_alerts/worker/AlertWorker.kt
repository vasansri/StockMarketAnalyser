package com.stockvision.feature_alerts.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.stockvision.domain.model.AlertType
import com.stockvision.domain.repository.AlertRepository
import com.stockvision.domain.repository.StockRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class AlertWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val alertRepository: AlertRepository,
    private val stockRepository: StockRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val alerts = alertRepository.getAlerts().first().filter { it.isEnabled }
        
        for (alert in alerts) {
            try {
                val quoteResult = stockRepository.getStockQuote(alert.symbol).first()
                if (quoteResult is com.stockvision.core.util.ResultState.Success) {
                    val currentPrice = quoteResult.data.price
                    val shouldNotify = when (alert.type) {
                        AlertType.PRICE_ABOVE -> currentPrice >= alert.targetValue
                        AlertType.PRICE_BELOW -> currentPrice <= alert.targetValue
                        else -> false
                    }

                    if (shouldNotify) {
                        showNotification(alert.symbol, "Target reached: $currentPrice")
                        alertRepository.toggleAlert(alert.id, false)
                    }
                }
            } catch (e: Exception) {
            }
        }
        
        return Result.success()
    }

    private fun showNotification(symbol: String, message: String) {
        val channelId = "stock_alerts"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Stock Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Stock Alert: $symbol")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Using system icon for simplicity
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(symbol.hashCode(), notification)
    }
}
