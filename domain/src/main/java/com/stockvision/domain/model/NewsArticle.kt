package com.stockvision.domain.model

data class NewsArticle(
    val title: String,
    val summary: String,
    val url: String,
    val imageUrl: String?,
    val source: String,
    val timePublished: String,
    val sentiment: SentimentType,
    val sentimentScore: Float
)

enum class SentimentType {
    BULLISH, BEARISH, NEUTRAL
}
