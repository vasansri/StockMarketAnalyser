package com.stockvision.sdk.di

import com.stockvision.sdk.StockVisionSDK
import com.stockvision.sdk.StockVisionSDKImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SDKModule {

    @Binds
    @Singleton
    abstract fun bindStockVisionSDK(
        impl: StockVisionSDKImpl
    ): StockVisionSDK
}
