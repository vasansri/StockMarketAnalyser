package com.stockvision.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stockvision.data.remote.StockApi
import com.stockvision.data.remote.dto.toDomain
import com.stockvision.domain.model.NewsArticle
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val api: StockApi,
    private val category: String
) : PagingSource<Int, NewsArticle>() {

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        return try {
            val page = params.key ?: 1
            // Alpha Vantage NEWS_SENTIMENT doesn't support pagination easily in FREE tier,
            // but we'll structure it for future expansion.
            val response = api.getNewsAndSentiment(topics = category, apiKey = "DEMO")
            val articles = response.feed?.map { it.toDomain() } ?: emptyList()
            
            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
