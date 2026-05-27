package com.stockvision.data.di

import android.content.Context
import androidx.room.Room
import com.stockvision.data.local.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            "stock_vision_db"
        ).build()
    }

    @Provides
    fun provideHoldingDao(db: StockDatabase) = db.holdingDao()

    @Provides
    fun provideAlertDao(db: StockDatabase) = db.alertDao()
}
