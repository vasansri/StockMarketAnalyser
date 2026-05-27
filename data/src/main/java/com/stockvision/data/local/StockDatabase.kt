package com.stockvision.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [StockEntity::class, HoldingEntity::class, AlertEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun holdingDao(): HoldingDao
    abstract fun alertDao(): AlertDao
}
