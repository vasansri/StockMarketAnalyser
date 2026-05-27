package com.stockvision.feature_news.presentation

import androidx.lifecycle.viewModelScope
import com.stockvision.core.base.BaseViewModel
import com.stockvision.core.util.ResultState
import com.stockvision.domain.model.NewsArticle
import com.stockvision.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class NewsState(
    val isLoading: Boolean = false,
    val articles: List<NewsArticle> = emptyList(),
    val error: String? = null,
    val selectedCategory: String = "technology"
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : BaseViewModel<NewsState>(NewsState()) {

    init {
        loadNews(uiState.value.selectedCategory)
    }

    fun loadNews(category: String) {
        newsRepository.getNews(category).onEach { result ->
            when (result) {
                is ResultState.Loading -> updateState { it.copy(isLoading = true, error = null) }
                is ResultState.Success -> updateState { it.copy(isLoading = false, articles = result.data) }
                is ResultState.Error -> updateState { it.copy(isLoading = false, error = result.message) }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun onCategorySelected(category: String) {
        updateState { it.copy(selectedCategory = category) }
        loadNews(category)
    }
}
