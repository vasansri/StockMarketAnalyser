package com.stockvision.presentation.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockvision.core.components.GlassCard
import com.stockvision.core.theme.GreenPositive

@Composable
fun PaywallScreen(
    onDismiss: () -> Unit,
    onSubscribe: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF000000))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            Text(
                "StockVision Pro",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                "Unlock the full potential of your trading",
                color = Color.LightGray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PremiumFeatureRow("Advanced Technical Indicators")
            PremiumFeatureRow("Real-time AI Trade Signals")
            PremiumFeatureRow("Unlimited Price Alerts")
            PremiumFeatureRow("Ad-free Experience")

            Spacer(modifier = Modifier.height(40.dp))

            PricingCard(
                title = "Monthly",
                price = "₹999/mo",
                onClick = { onSubscribe("monthly_pro") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            PricingCard(
                title = "Yearly (Best Value)",
                price = "₹7,999/yr",
                isPopular = true,
                onClick = { onSubscribe("yearly_pro") }
            )
        }
    }
}

@Composable
fun PremiumFeatureRow(feature: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Check, contentDescription = null, tint = GreenPositive)
        Spacer(modifier = Modifier.width(12.dp))
        Text(feature, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun PricingCard(
    title: String,
    price: String,
    isPopular: Boolean = false,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = Color.White)
                Text(price, color = GreenPositive)
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = if (isPopular) GreenPositive else Color.DarkGray)
            ) {
                Text("Select", color = Color.White)
            }
        }
    }
}
