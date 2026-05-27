package com.stockvision.data.di

import android.content.Context
import com.google.gson.Gson
import com.stockvision.core.network.ApiKeyInterceptor
import com.stockvision.core.network.NetworkInterceptor
import com.stockvision.data.remote.StockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        networkInterceptor: NetworkInterceptor,
        apiKeyInterceptor: ApiKeyInterceptor
    ): OkHttpClient {
        val cache = Cache(File(context.cacheDir, "http_cache"), 10 * 1024 * 1024) // 10 MB
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(networkInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideStockApi(okHttpClient: OkHttpClient): StockApi {
        return Retrofit.Builder()
            .baseUrl(StockApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StockApi::class.java)
    }
}
