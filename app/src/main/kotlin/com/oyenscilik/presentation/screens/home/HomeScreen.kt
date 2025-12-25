package com.oyenscilik.presentation.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

// Premium 2025 Colors
private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val TextGray = Color(0xFF636E72)

@Composable
fun HomeScreen(
    onNavigateToLetters: () -> Unit,
    onNavigateToNumbers: () -> Unit,
    onNavigateToAnimals: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Cream, Color(0xFFFFF8F0), Peach.copy(0.2f))
                )
            )
    ) {
        // Decorative bg circles
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = (-80).dp, y = (-50).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFF8C42).copy(0.12f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 250.dp, y = 400.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE8E0FF).copy(0.5f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Halo! 👋",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Ayo kita belajar hari ini",
                        fontSize = 15.sp,
                        color = TextGray
                    )
                }


Text("🐱", fontSize = 32.sp)

            }

            Spacer(modifier = Modifier.height(36.dp))

            // Menu cards - Grid style
            Text(
                text = "Pilih Pelajaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Letters - Large featured card
            PremiumMenuCard(
                title = "Belajar Huruf",
                subtitle = "A sampai Z",
                gradient = listOf(Color(0xFFFF8C42), Color(0xFFFF6B35)),
                modifier = Modifier.fillMaxWidth().height(140.dp),
                delay = 100,
                onClick = onNavigateToLetters
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Numbers + Animals row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumMenuCard(
                    title = "Angka",
                    subtitle = "0 - 20",
                    gradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
                    modifier = Modifier.weight(1f).height(150.dp),
                    delay = 200,
                    onClick = onNavigateToNumbers
                )

                PremiumMenuCard(
                    title = "Hewan",
                    subtitle = "15+ Jenis",
                    gradient = listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
                    modifier = Modifier.weight(1f).height(150.dp),
                    delay = 300,
                    onClick = onNavigateToAnimals
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PremiumMenuCard(
    title: String,
    subtitle: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    delay: Int = 0,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(initialScale = 0.9f)
    ) {
        Box(
            modifier = modifier
                .graphicsLayer { 
                    scaleX = scale
                    scaleY = scale 
                }
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = gradient[0].copy(0.2f),
                    spotColor = gradient[0].copy(0.15f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(brush = Brush.linearGradient(gradient))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPressed = true
                    onClick()
                }
                .padding(20.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.White.copy(0.85f)
                )
            }

            // Decorative circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(Color.White.copy(0.15f), CircleShape)
            )
        }
    }
}

@Composable
fun PremiumStatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3436)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF636E72)
            )
        }
    }
}
