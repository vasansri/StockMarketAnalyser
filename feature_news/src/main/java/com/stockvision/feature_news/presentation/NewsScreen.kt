package com.stockvision.feature_news.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.stockvision.core.components.GlassCard
import com.stockvision.core.theme.GreenPositive
import com.stockvision.core.theme.RedNegative
import com.stockvision.core.theme.Surface
import com.stockvision.domain.model.NewsArticle
import com.stockvision.domain.model.SentimentType

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val categories = listOf("technology", "financial_markets", "economy", "earnings")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Market News", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        CategoryTabs(
            categories = categories,
            selectedCategory = state.selectedCategory,
            onCategorySelected = { viewModel.onCategorySelected(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            NewsSkeletonList()
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}", color = Color.Red)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.articles) { article ->
                    NewsArticleItem(article)
                }
            }
        }
    }
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            val backgroundColor by animateColorAsState(
                if (isSelected) GreenPositive else Surface,
                label = "color"
            )
            val contentColor = if (isSelected) Color.Black else Color.White

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCategorySelected(category) },
                color = backgroundColor
            ) {
                Text(
                    text = category.replace("_", " ").capitalize(),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = contentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun NewsArticleItem(article: NewsArticle) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                SentimentBadge(article.sentiment)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SentimentBadge(sentiment: SentimentType) {
    val (color, text) = when (sentiment) {
        SentimentType.BULLISH -> GreenPositive to "Bullish"
        SentimentType.BEARISH -> RedNegative to "Bearish"
        SentimentType.NEUTRAL -> Color.Gray to "Neutral"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun NewsSkeletonList() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(5) {
            NewsSkeletonItem()
        }
    }
}

@Composable
fun NewsSkeletonItem() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).background(Color.DarkGray.copy(alpha = 0.5f)))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.width(60.dp).height(15.dp).background(Color.DarkGray.copy(alpha = 0.5f)))
                Box(modifier = Modifier.fillMaxWidth().height(20.dp).background(Color.DarkGray.copy(alpha = 0.5f)))
                Box(modifier = Modifier.width(100.dp).height(15.dp).background(Color.DarkGray.copy(alpha = 0.5f)))
            }
        }
    }
}
