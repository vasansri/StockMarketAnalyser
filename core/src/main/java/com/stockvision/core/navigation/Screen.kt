package com.stockvision.core.navigation

sealed class Screen(val route: String) {
    object Market : Screen("market")
    object Chart : Screen("chart/{symbol}") {
        fun createRoute(symbol: String) = "chart/$symbol"
    }
    object Portfolio : Screen("portfolio")
    object Alerts : Screen("alerts")
    object AI : Screen("ai")
    object News : Screen("news")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Paywall : Screen("paywall")
}
