package com.stockvision.data.di

import com.stockvision.data.repository.AlertRepositoryImpl
import com.stockvision.data.repository.NewsRepositoryImpl
import com.stockvision.data.repository.PortfolioRepositoryImpl
import com.stockvision.data.repository.StockRepositoryImpl
import com.stockvision.domain.repository.AlertRepository
import com.stockvision.domain.repository.NewsRepository
import com.stockvision.domain.repository.PortfolioRepository
import com.stockvision.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository

    @Binds
    @Singleton
    abstract fun bindPortfolioRepository(
        portfolioRepositoryImpl: PortfolioRepositoryImpl
    ): PortfolioRepository

    @Binds
    @Singleton
    abstract fun bindAlertRepository(
        alertRepositoryImpl: AlertRepositoryImpl
    ): AlertRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}
