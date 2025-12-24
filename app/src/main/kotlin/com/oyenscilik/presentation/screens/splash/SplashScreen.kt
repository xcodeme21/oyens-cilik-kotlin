package com.oyenscilik.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Premium 2025 Color Palette - Soft & Warm
val PremiumCream = Color(0xFFFFFBF5)
val PremiumPeach = Color(0xFFFFE5D9)
val PremiumOrange = Color(0xFFFF8C42)
val PremiumCoral = Color(0xFFFF6B6B)
val PremiumLavender = Color(0xFFE8E0FF)
val PremiumMint = Color(0xFFD0F0E4)
val PremiumSky = Color(0xFFE3F2FD)
val PremiumText = Color(0xFF2D3436)
val PremiumSubtext = Color(0xFF636E72)

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    
    // Gentle floating
    val float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    // Scale entrance
    var isVisible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        isVisible = true
        delay(2500)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PremiumCream,
                        Color(0xFFFFF8F0),
                        PremiumPeach.copy(0.3f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-200).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(PremiumOrange.copy(0.15f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 120.dp, y = 250.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(PremiumLavender.copy(0.4f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        ) {
            // Mascot
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .offset(y = (-float).dp)
                    .shadow(
                        elevation = 30.dp,
                        shape = CircleShape,
                        ambientColor = PremiumOrange.copy(0.3f),
                        spotColor = PremiumOrange.copy(0.2f)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFAA5C),
                                PremiumOrange
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🐱", fontSize = 90.sp)
            }

            Spacer(modifier = Modifier.height(48.dp))

            // App name
            Text(
                text = "Oyens Cilik",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Belajar Sambil Bermain",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = PremiumSubtext
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Loading dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { index ->
                    val dotAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot$index"
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(PremiumOrange.copy(dotAlpha), CircleShape)
                    )
                }
            }
        }
    }
}
