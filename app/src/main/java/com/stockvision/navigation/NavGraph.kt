package com.stockvision.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stockvision.core.navigation.Screen
import com.stockvision.feature_market.presentation.MarketDashboardScreen
import com.stockvision.feature_chart.presentation.StockChartScreen
import com.stockvision.feature_ai.presentation.AIRecommendationScreen
import com.stockvision.feature_portfolio.presentation.PortfolioScreen
import com.stockvision.feature_alerts.presentation.AlertsScreen
import com.stockvision.feature_news.presentation.NewsScreen
import com.stockvision.presentation.auth.LoginScreen
import com.stockvision.presentation.premium.PaywallScreen

import com.stockvision.presentation.profile.ProfileScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Market.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.Market.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Paywall.route) {
            PaywallScreen(
                onDismiss = { navController.popBackStack() },
                onSubscribe = { /* Handle Billing */ }
            )
        }
        composable(Screen.Market.route) {
            MarketDashboardScreen(
                onNavigateToChart = { symbol ->
                    navController.navigate(Screen.Chart.createRoute(symbol))
                }
            )
        }
        composable(Screen.Portfolio.route) {
            PortfolioScreen()
        }
        composable(Screen.Alerts.route) {
            AlertsScreen()
        }
        composable(Screen.News.route) {
            NewsScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Chart.route) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
            Column {
                StockChartScreen(
                    onBack = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                )
                AIRecommendationScreen(symbol = symbol)
            }
        }
    }
}
