package com.stockvision.feature_market.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stockvision.core.components.GlassCard
import com.stockvision.core.navigation.Screen
import com.stockvision.core.theme.GreenPositive
import com.stockvision.core.theme.RedNegative
import com.stockvision.core.theme.StockVisionTheme
import com.stockvision.core.theme.Surface
import kotlinx.coroutines.delay

@Composable
fun MarketDashboardScreen(
    onNavigateToChart: (String) -> Unit,
    viewModel: MarketDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    MarketDashboardContent(state = state, onNavigateToChart = onNavigateToChart)
}

@Composable
fun MarketDashboardContent(
    state: MarketDashboardState,
    onNavigateToChart: (String) -> Unit
) {
    Scaffold(
        topBar = {
            DashboardHeader(userName = state.userName)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { MarketIndices(state) }
            item { PortfolioSummary(state) }
            item { SectionHeader("Trending Stocks") }
            item { TrendingStocksRow(state.trendingStocks, onNavigateToChart) }
            item { SectionHeader("Top Gainers & Losers") }
            item { GainersLosersSection(state, onNavigateToChart) }
            item { AIRecommendationsSection() }
            item { NewsFeedSection() }
        }
    }
}

@Composable
fun DashboardHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Connection Indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(GreenPositive, CircleShape)
                )
            }
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { },
                modifier = Modifier.clip(CircleShape).background(Surface)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            IconButton(
                onClick = { },
                modifier = Modifier.clip(CircleShape).background(Surface)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            }
        }
    }
}

@Composable
fun MarketIndices(state: MarketDashboardState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IndexCard(
            name = "NIFTY 50",
            value = state.niftyValue,
            change = state.niftyChange,
            history = state.niftyHistory,
            modifier = Modifier.weight(1f)
        )
        IndexCard(
            name = "SENSEX",
            value = state.sensexValue,
            change = state.sensexChange,
            history = state.sensexHistory,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun IndexCard(name: String, value: String, change: String, history: List<Double>, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(name, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(change, style = MaterialTheme.typography.labelSmall, color = GreenPositive)
            Spacer(modifier = Modifier.height(8.dp))
            MiniChart(data = history, color = GreenPositive)
        }
    }
}

@Composable
fun MiniChart(data: List<Double>, color: Color) {
    Canvas(modifier = Modifier.fillMaxWidth().height(30.dp)) {
        if (data.size < 2) return@Canvas
        
        val max = data.maxOrNull()?.toFloat() ?: 0f
        val min = data.minOrNull()?.toFloat() ?: 0f
        val range = (max - min).coerceAtLeast(1f)
        
        val path = androidx.compose.ui.graphics.Path().apply {
            data.forEachIndexed { index, value ->
                val x = (index.toFloat() / (data.size - 1)) * size.width
                val y = size.height - ((value.toFloat() - min) / range * size.height)
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = color,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
fun PortfolioSummary(state: MarketDashboardState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1A237E), Color(0xFF0D47A1))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text("Total Portfolio Value", color = Color.LightGray)
                Text(
                    state.portfolioValue,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = GreenPositive, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(state.portfolioChange, color = GreenPositive, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TrendingStocksRow(stocks: List<StockUiModel>, onStockClick: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stocks) { stock ->
            StockMiniCard(stock, onClick = { onStockClick(stock.symbol) })
        }
    }
}

@Composable
fun StockMiniCard(stock: StockUiModel, onClick: () -> Unit) {
    var isBlinking by remember { mutableStateOf(false) }
    
    LaunchedEffect(stock.lastUpdated) {
        if (stock.lastUpdated > 0) {
            isBlinking = true
            delay(300)
            isBlinking = false
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isBlinking) {
            if (stock.isPositive) GreenPositive.copy(alpha = 0.2f) else RedNegative.copy(alpha = 0.2f)
        } else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "blink"
    )

    GlassCard(
        modifier = Modifier
            .width(140.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(stock.symbol, fontWeight = FontWeight.Bold)
            Text(stock.name, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stock.price, style = MaterialTheme.typography.labelLarge)
            Text(stock.change, color = if (stock.isPositive) GreenPositive else RedNegative, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun GainersLosersSection(state: MarketDashboardState, onStockClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        state.topGainers.forEach { stock ->
            StockListItem(stock, onClick = { onStockClick(stock.symbol) })
        }
        state.topLosers.forEach { stock ->
            StockListItem(stock, onClick = { onStockClick(stock.symbol) })
        }
    }
}

@Composable
fun StockListItem(stock: StockUiModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(stock.symbol, fontWeight = FontWeight.Bold)
            Text(stock.name, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(stock.price, fontWeight = FontWeight.Bold)
            Text(stock.change, color = if (stock.isPositive) GreenPositive else RedNegative)
        }
    }
}

@Composable
fun AIRecommendationsSection() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨ AI Insights", fontWeight = FontWeight.Bold, color = Color(0xFFBB86FC))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Based on your portfolio, consider increasing exposure to Renewable Energy sector as market trends suggest a 15% upside.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun NewsFeedSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Market News", fontWeight = FontWeight.Bold)
        repeat(2) {
            NewsItem()
        }
    }
}

@Composable
fun NewsItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
        )
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
            Text("US markets end higher as inflation data meets expectations", maxLines = 2, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text("2 hours ago", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E12)
@Composable
fun DashboardPreview() {
    StockVisionTheme {
        MarketDashboardContent(
            state = MarketDashboardState(
                userName = "Alex Trader",
                niftyHistory = listOf(22000.0, 22010.0, 22005.0, 22030.0, 22040.0),
                sensexHistory = listOf(72600.0, 72620.0, 72610.0, 72640.0, 72643.0),
                trendingStocks = listOf(
                    StockUiModel("AAPL", "Apple Inc.", "$175.00", "+1.5%", true),
                    StockUiModel("TSLA", "Tesla, Inc.", "$210.00", "-2.3%", false)
                ),
                topGainers = listOf(
                    StockUiModel("GOOGL", "Alphabet Inc.", "$145.00", "+3.2%", true)
                ),
                topLosers = listOf(
                    StockUiModel("MSFT", "Microsoft Corp.", "$400.00", "-1.1%", false)
                )
            ),
            onNavigateToChart = {}
        )
    }
}
