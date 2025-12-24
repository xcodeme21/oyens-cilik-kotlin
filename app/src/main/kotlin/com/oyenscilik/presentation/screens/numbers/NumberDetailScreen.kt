package com.oyenscilik.presentation.screens.numbers

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val AccentPurple = Color(0xFF667EEA)

@Composable
fun NumberDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigatePrev: (() -> Unit)? = null,
    onNavigateNext: (() -> Unit)? = null,
    viewModel: NumberDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val hasPrev = onNavigatePrev != null
    val hasNext = onNavigateNext != null

    val infiniteTransition = rememberInfiniteTransition(label = "number")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Cream, Color(0xFFFFF8F0), Peach.copy(0.2f))))
    ) {
        // Decorative circle
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 150.dp, y = 200.dp)
                .background(
                    brush = Brush.radialGradient(listOf(AccentPurple.copy(0.12f), Color.Transparent)),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", fontSize = 22.sp, color = TextDark)
                }

                Text(
                    text = "Belajar Angka",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text("🔢", fontSize = 28.sp)
            }

            when {
                uiState.isLoading -> {
                    // Skeleton Loading
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        // Number circle skeleton
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .shadow(24.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentPurple)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        // Word skeleton
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray.copy(0.3f))
                        )
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                        Text("${uiState.error}", color = Color(0xFFFF6B6B), fontSize = 16.sp)
                    }
                }
                uiState.number != null -> {
                    val number = uiState.number!!

                    Spacer(modifier = Modifier.height(40.dp))

                    // Main content with navigation arrows
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left arrow
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(if (hasPrev) 12.dp else 0.dp, CircleShape)
                                .clip(CircleShape)
                                .background(if (hasPrev) Color.White else Color.Transparent)
                                .clickable(enabled = hasPrev) { onNavigatePrev?.invoke() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "◀",
                                fontSize = 24.sp,
                                color = if (hasPrev) AccentPurple else Color.Gray.copy(0.3f)
                            )
                        }

                        // Main number card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                                .offset(y = (-bounce).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .shadow(24.dp, CircleShape, ambientColor = AccentPurple.copy(0.2f))
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.linearGradient(listOf(AccentPurple, Color(0xFF764BA2)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${number.number}",
                                    fontSize = 88.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Right arrow
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(if (hasNext) 12.dp else 0.dp, CircleShape)
                                .clip(CircleShape)
                                .background(if (hasNext) Color.White else Color.Transparent)
                                .clickable(enabled = hasNext) { onNavigateNext?.invoke() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "▶",
                                fontSize = 24.sp,
                                color = if (hasNext) AccentPurple else Color.Gray.copy(0.3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // TTS button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF667EEA), Color(0xFF764BA2)))
                            )
                            .clickable { viewModel.toggleAudio() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.isPlaying) "🔊 Playing..." else "🔊 Dengarkan",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Word card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Dibaca", fontSize = 14.sp, color = Color(0xFF636E72))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = number.word,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Counting examples
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "Contoh Berhitung",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                repeat(minOf(number.number, 10)) {
                                    Text(
                                        text = "⭐",
                                        fontSize = 28.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
