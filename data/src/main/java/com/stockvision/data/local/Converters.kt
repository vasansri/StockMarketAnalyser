package com.stockvision.data.local

import androidx.room.TypeConverter
import com.stockvision.domain.model.AlertType

class Converters {
    @TypeConverter
    fun fromAlertType(value: AlertType): String = value.name

    @TypeConverter
    fun toAlertType(value: String): AlertType = AlertType.valueOf(value)
}
