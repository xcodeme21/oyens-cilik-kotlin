package com.oyenscilik.presentation.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.presentation.components.AnimatedCard
import com.oyenscilik.presentation.theme.*

@Composable
fun HomeScreen(
    onNavigateToLetters: () -> Unit,
    onNavigateToNumbers: () -> Unit,
    onNavigateToAnimals: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F0),
                        Color(0xFFFFF5EB),
                        Color(0xFFFFF0E5)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Compact header dengan mascot dan greeting
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mascot icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(4.dp, CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(KidOrange.copy(0.9f), KidOrangeDark)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐱", fontSize = 28.sp)
            }

            // Greeting text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Halo! 👋",
                    fontSize = 18.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Mau belajar apa?",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu cards dengan aspect ratio lebih compact
        val menuItems = listOf(
            MenuData(
                emoji = "🔤",
                title = "ABC",
                subtitle = "Belajar Huruf",
                colors = listOf(KidOrange, KidOrangeDark),
                onClick = onNavigateToLetters
            ),
            MenuData(
                emoji = "🔢",
                title = "123",
                subtitle = "Belajar Angka",
                colors = listOf(KidBlue, KidBlueDark),
                onClick = onNavigateToNumbers
            ),
            MenuData(
                emoji = "🦁",
                title = "Hewan",
                subtitle = "Dunia Satwa",
                colors = listOf(KidGreen, KidGreenDark),
                onClick = onNavigateToAnimals
            )
        )

        menuItems.forEachIndexed { index, menu ->
            var isVisible by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay((index * 120).toLong())
                isVisible = true
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.slideInHorizontally(
                    initialOffsetX = { if (index % 2 == 0) -it else it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + androidx.compose.animation.fadeIn()
            ) {
                AnimatedCard(
                    onClick = menu.onClick,
                    gradientColors = menu.colors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Icon circle
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.25f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = menu.emoji,
                                    fontSize = 32.sp
                                )
                            }

                            // Text
                            Column {
                                Text(
                                    text = menu.title,
                                    fontSize = 24.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = menu.subtitle,
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Arrow
                        Text(
                            text = "▶",
                            fontSize = 24.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Motivasi banner yang lebih compact
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(KidPink.copy(0.3f), KidPurple.copy(0.3f))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✨ Semangat Belajar! ✨",
                fontSize = 14.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

private data class MenuData(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val colors: List<Color>,
    val onClick: () -> Unit
)
