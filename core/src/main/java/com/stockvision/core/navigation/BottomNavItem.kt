package com.stockvision.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Market.route, "Home", Icons.Default.Home)
    object Portfolio : BottomNavItem(Screen.Portfolio.route, "Portfolio", Icons.Default.ShoppingCart)
    object Alerts : BottomNavItem(Screen.Alerts.route, "Alerts", Icons.Default.Notifications)
    object News : BottomNavItem(Screen.News.route, "News", Icons.Default.List)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}
