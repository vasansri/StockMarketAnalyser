package com.stockvision.feature_portfolio.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stockvision.core.components.GlassCard
import com.stockvision.core.theme.GreenPositive
import com.stockvision.core.theme.RedNegative
import com.stockvision.core.theme.Surface
import com.stockvision.domain.model.Holding

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Portfolio",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        PortfolioSummaryCard(state)

        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Asset Allocation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        AllocationPieChart(state.holdings)

        Spacer(modifier = Modifier.height(24.dp))

        Text("My Holdings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(state.holdings) { holding ->
                HoldingItem(holding)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PortfolioSummaryCard(state: PortfolioState) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Total Current Value", color = Color.Gray, fontSize = 14.sp)
            Text(
                "₹${String.format("%.2f", state.totalCurrentValue)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Total Invested", color = Color.Gray, fontSize = 12.sp)
                    Text("₹${String.format("%.2f", state.totalInvested)}", fontWeight = FontWeight.Medium)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total P&L", color = Color.Gray, fontSize = 12.sp)
                    val plColor = if (state.totalProfitLoss >= 0) GreenPositive else RedNegative
                    Text(
                        "${if (state.totalProfitLoss >= 0) "+" else ""}₹${String.format("%.2f", state.totalProfitLoss)} (${String.format("%.2f", state.totalProfitLossPercentage)}%)",
                        color = plColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AllocationPieChart(holdings: List<Holding>) {
    val total = holdings.sumOf { it.currentValue }
    if (total == 0.0) return

    val colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow, Color.Green, Color.Blue, Color.Red)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.size(120.dp)) {
            var startAngle = -90f
            holdings.forEachIndexed { index, holding ->
                val sweepAngle = (holding.currentValue / total).toFloat() * 360f
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Butt)
                )
                startAngle += sweepAngle
            }
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            holdings.take(4).forEachIndexed { index, holding ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(colors[index % colors.size], RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(holding.symbol, fontSize = 12.sp, color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun HoldingItem(holding: Holding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(holding.symbol, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("${holding.quantity} shares", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            val plColor = if (holding.profitLoss >= 0) GreenPositive else RedNegative
            Text("₹${String.format("%.2f", holding.currentValue)}", fontWeight = FontWeight.Bold)
            Text(
                "${if (holding.profitLoss >= 0) "+" else ""}₹${String.format("%.2f", holding.profitLoss)}",
                color = plColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
