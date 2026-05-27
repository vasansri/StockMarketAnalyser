package com.stockvision.data.repository

import com.stockvision.core.util.ErrorHandler
import com.stockvision.core.util.ResultState
import com.stockvision.data.remote.StockApi
import com.stockvision.data.remote.dto.toDomain
import com.stockvision.domain.model.NewsArticle
import com.stockvision.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: StockApi
) : NewsRepository {

    override fun getNews(category: String): Flow<ResultState<List<NewsArticle>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = api.getNewsAndSentiment(topics = category, apiKey = "DEMO")
            val domainArticles = response.feed?.map { it.toDomain() } ?: emptyList()
            emit(ResultState.Success(domainArticles))
        } catch (e: Exception) {
            emit(ResultState.Error(ErrorHandler.handleError(e), e))
        }
    }
}
