package com.stockvision.data.remote

import com.stockvision.data.remote.dto.AlphaVantageQuoteResponse
import com.stockvision.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    
    @GET("query")
    suspend fun getStockQuote(
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): AlphaVantageQuoteResponse

    @GET("query")
    suspend fun getNewsAndSentiment(
        @Query("function") function: String = "NEWS_SENTIMENT",
        @Query("tickers") tickers: String? = null,
        @Query("topics") topics: String? = null,
        @Query("apikey") apiKey: String
    ): NewsResponseDto

    companion object {
        const val BASE_URL = "https://www.alphavantage.co/"
    }
}
