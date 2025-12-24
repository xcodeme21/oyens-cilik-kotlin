package com.oyenscilik.presentation.screens.letters

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oyenscilik.domain.model.Letter

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val AccentOrange = Color(0xFFFF8C42)

@Composable
fun LetterDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigatePrev: (() -> Unit)? = null,
    onNavigateNext: (() -> Unit)? = null,
    viewModel: LetterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val letters = ('A'..'Z').toList()
    
    var currentLetterId by remember { mutableStateOf(uiState.letter?.id ?: 1) }
    
    LaunchedEffect(uiState.letter) {
        uiState.letter?.let { currentLetterId = it.id }
    }
    
    val hasPrev = currentLetterId > 1
    val hasNext = currentLetterId < 26

    val infiniteTransition = rememberInfiniteTransition(label = "letter")
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
                    brush = Brush.radialGradient(listOf(AccentOrange.copy(0.12f), Color.Transparent)),
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
                    text = "Belajar Huruf",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text("🔤", fontSize = 28.sp)
            }

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AccentOrange)
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                        Text("${uiState.error}", color = Color(0xFFFF6B6B), fontSize = 16.sp)
                    }
                }
                uiState.letter != null -> {
                    val letter = uiState.letter!!

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
                                .clickable(enabled = hasPrev) {
                                    if (hasPrev) {
                                        onNavigatePrev?.invoke()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "◀",
                                fontSize = 24.sp,
                                color = if (hasPrev) AccentOrange else Color.Gray.copy(0.3f)
                            )
                        }

                        // Main letter card
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
                                    .shadow(24.dp, CircleShape, ambientColor = AccentOrange.copy(0.2f))
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.linearGradient(listOf(AccentOrange, Color(0xFFFF6B35)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = letter.letter,
                                        fontSize = 72.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = letter.lowercase,
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White.copy(0.8f)
                                    )
                                }
                            }
                        }

                        // Right arrow
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(if (hasNext) 12.dp else 0.dp, CircleShape)
                                .clip(CircleShape)
                                .background(if (hasNext) Color.White else Color.Transparent)
                                .clickable(enabled = hasNext) {
                                    if (hasNext) {
                                        onNavigateNext?.invoke()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "▶",
                                fontSize = 24.sp,
                                color = if (hasNext) AccentOrange else Color.Gray.copy(0.3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Audio button
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

                    // Example word
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
                            Text("Contoh Kata", fontSize = 14.sp, color = Color(0xFF636E72))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = letter.exampleWord,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Position indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${currentLetterId} / 26",
                            fontSize = 14.sp,
                            color = Color(0xFF636E72)
                        )
                    }
                }
            }
        }
    }
}
