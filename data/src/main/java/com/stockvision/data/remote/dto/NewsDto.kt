package com.stockvision.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.stockvision.domain.model.NewsArticle
import com.stockvision.domain.model.SentimentType

data class NewsResponseDto(
    val items: String?, // Alpha Vantage uses "feed"
    val feed: List<NewsArticleDto>?
)

data class NewsArticleDto(
    val title: String?,
    val summary: String?,
    val url: String?,
    @SerializedName("banner_image")
    val bannerImage: String?,
    val source: String?,
    @SerializedName("time_published")
    val timePublished: String?,
    @SerializedName("overall_sentiment_label")
    val sentimentLabel: String?,
    @SerializedName("overall_sentiment_score")
    val sentimentScore: Float?
)

fun NewsArticleDto.toDomain(): NewsArticle {
    return NewsArticle(
        title = title ?: "",
        summary = summary ?: "",
        url = url ?: "",
        imageUrl = bannerImage,
        source = source ?: "",
        timePublished = timePublished ?: "",
        sentiment = when (sentimentLabel) {
            "Bullish", "Somewhat Bullish" -> SentimentType.BULLISH
            "Bearish", "Somewhat Bearish" -> SentimentType.BEARISH
            else -> SentimentType.NEUTRAL
        },
        sentimentScore = sentimentScore ?: 0f
    )
}
