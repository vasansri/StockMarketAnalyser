package com.stockvision.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.stockvision.domain.model.StockQuote

data class AlphaVantageQuoteResponse(
    @SerializedName("Global Quote")
    val globalQuote: GlobalQuoteDto
)

data class GlobalQuoteDto(
    @SerializedName("01. symbol")
    val symbol: String,
    @SerializedName("02. open")
    val open: String,
    @SerializedName("03. high")
    val high: String,
    @SerializedName("04. low")
    val low: String,
    @SerializedName("05. price")
    val price: String,
    @SerializedName("06. volume")
    val volume: String,
    @SerializedName("07. latest trading day")
    val latestTradingDay: String,
    @SerializedName("08. previous close")
    val previousClose: String,
    @SerializedName("09. change")
    val change: String,
    @SerializedName("10. change percent")
    val changePercent: String
)

fun GlobalQuoteDto.toDomain(): StockQuote {
    return StockQuote(
        symbol = symbol,
        price = price.toDoubleOrNull() ?: 0.0,
        open = open.toDoubleOrNull() ?: 0.0,
        high = high.toDoubleOrNull() ?: 0.0,
        low = low.toDoubleOrNull() ?: 0.0,
        volume = volume.toLongOrNull() ?: 0L,
        previousClose = previousClose.toDoubleOrNull() ?: 0.0,
        change = change.toDoubleOrNull() ?: 0.0,
        changePercent = changePercent.replace("%", "").toDoubleOrNull() ?: 0.0
    )
}
