package com.stockvision.domain.repository

import com.stockvision.core.util.ResultState
import com.stockvision.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(category: String): Flow<ResultState<List<NewsArticle>>>
}
