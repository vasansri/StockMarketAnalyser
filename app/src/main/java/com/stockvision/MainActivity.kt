package com.stockvision

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.stockvision.core.components.StockVisionBottomBar
import com.stockvision.core.theme.StockVisionTheme
import com.stockvision.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.stockvision.core.navigation.Screen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            StockVisionTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                val showBottomBar = currentRoute != Screen.Login.route && currentRoute != Screen.Paywall.route

                Scaffold(
                    bottomBar = { 
                        if (showBottomBar) {
                            StockVisionBottomBar(navController = navController) 
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }

}
