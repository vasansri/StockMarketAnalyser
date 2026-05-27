package com.stockvision.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stockvision.domain.model.AlertType

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbol: String,
    val type: AlertType,
    val targetValue: Double,
    val isEnabled: Boolean,
    val createdAt: Long
)
