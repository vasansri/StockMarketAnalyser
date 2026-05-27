# StockVision Android SDK

A comprehensive SDK for integrating stock market analysis, AI trade signals, and portfolio analytics into any Android application.

## Features

- **Technical Analysis**: Real-time quotes and historical candlestick data.
- **AI Recommendation Engine**: Automated BUY/SELL/HOLD signals using RSI and MACD strategies.
- **Portfolio Analytics**: Track invested value, profit/loss, and asset allocation.
- **Alerts Engine**: Infrastructure for monitoring price movements.

## Integration

### 1. Add Dependency

Add the following to your `build.gradle`:

```gradle
dependencies {
    implementation project(":sdk")
}
```

### 2. Initialize with Hilt

The SDK is built with Hilt. Simply inject `StockVisionSDK` where needed:

```kotlin
@Inject
lateinit var stockSDK: StockVisionSDK
```

## Usage

### Analyze a Stock

```kotlin
val recommendation = stockSDK.analyzeStock("AAPL", historicalCandles)
println("Signal: ${recommendation.signal}")
```

### Get Real-time Portfolio Analytics

```kotlin
stockSDK.getPortfolioSummary().onEach { analytics ->
    updateUI(analytics.totalCurrentValue)
}.launchIn(lifecycleScope)
```

## Architecture

The SDK follows Clean Architecture principles, isolating market data logic from the UI components. It uses Kotlin Coroutines and Flows for efficient, reactive data streaming.
