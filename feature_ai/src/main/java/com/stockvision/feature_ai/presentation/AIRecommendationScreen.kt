package com.stockvision.feature_ai.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stockvision.core.components.GlassCard
import com.stockvision.core.components.LoadingScreen
import com.stockvision.core.theme.GreenPositive
import com.stockvision.core.theme.RedNegative
import com.stockvision.domain.model.AIRecommendation
import com.stockvision.domain.model.Signal

@Composable
fun AIRecommendationScreen(
    symbol: String,
    viewModel: AIRecommendationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.getRecommendation(symbol)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "AI Analysis: $symbol",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            LoadingScreen()
        } else {
            state.recommendation?.let { recommendation ->
                RecommendationCard(recommendation)
                Spacer(modifier = Modifier.height(24.dp))
                ConfidenceMeter(recommendation.confidence)
                Spacer(modifier = Modifier.height(24.dp))
                TechnicalDetails(recommendation)
            }
        }
    }
}

@Composable
fun RecommendationCard(rec: AIRecommendation) {
    val color = when (rec.signal) {
        Signal.BUY -> GreenPositive
        Signal.SELL -> RedNegative
        Signal.HOLD -> Color.Gray
    }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Recommendation", color = Color.Gray, fontSize = 14.sp)
                Text(
                    rec.signal.name,
                    color = color,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }
            RiskBadge(rec.riskScore)
        }
    }
}

@Composable
fun RiskBadge(score: Int) {
    val color = if (score < 4) GreenPositive else if (score < 7) Color.Yellow else RedNegative
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text("Risk: $score/10", color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ConfidenceMeter(confidence: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = confidence,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "confidence"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(150.dp)) {
                drawArc(
                    color = Color.DarkGray,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    brush = Brush.sweepGradient(listOf(Color.Magenta, Color.Cyan)),
                    startAngle = 135f,
                    sweepAngle = 270f * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${(confidence * 100).toInt()}%", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Confidence", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun TechnicalDetails(rec: AIRecommendation) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        DetailRow("Trend", rec.trendPrediction)
        DetailRow("Indicators", rec.technicalAnalysis)
        DetailRow("MACD", rec.macdSignal)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
