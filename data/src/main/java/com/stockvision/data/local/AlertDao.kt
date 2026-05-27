package com.stockvision.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("UPDATE alerts SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateAlertStatus(id: Int, isEnabled: Boolean)

    @Query("DELETE FROM alerts WHERE id = :id")
    suspend fun deleteAlert(id: Int)
}
