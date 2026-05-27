package com.stockvision.feature_chart.presentation

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.stockvision.core.components.LoadingScreen
import com.stockvision.domain.model.Candle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockChartScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StockChartViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(state.symbol) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                LoadingScreen()
            } else if (state.error != null) {
                Text("Error: ${state.error}")
            } else {
                CandleChart(candles = state.candles)
            }
        }
    }
}

@Composable
fun CandleChart(candles: List<Candle>) {
    AndroidView(
        factory = { context ->
            CandleStickChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    textColor = Color.WHITE
                    setDrawGridLines(false)
                }
                
                axisLeft.apply {
                    textColor = Color.WHITE
                    setDrawGridLines(true)
                }
                
                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = candles.mapIndexed { index, candle ->
                CandleEntry(
                    index.toFloat(),
                    candle.high,
                    candle.low,
                    candle.open,
                    candle.close
                )
            }
            
            val dataSet = CandleDataSet(entries, "Market Data").apply {
                shadowColor = Color.LTGRAY
                shadowWidth = 0.7f
                decreasingColor = Color.RED
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = Color.GREEN
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = Color.BLUE
                valueTextColor = Color.WHITE
                setDrawValues(false)
            }
            
            chart.data = CandleData(dataSet)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxWidth().height(400.dp)
    )
}
