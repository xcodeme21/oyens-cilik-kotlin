package com.oyenscilik.presentation.screens.letters

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.oyenscilik.presentation.components.KidButton
import com.oyenscilik.presentation.components.kidBackgroundPattern
import com.oyenscilik.presentation.theme.*

@Composable
fun LetterDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: LetterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .kidBackgroundPattern()
    ) {
        // Top bar with back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            KidButton(
                text = "🔙",
                onClick = onNavigateBack,
                mainColor = KidPink,
                modifier = Modifier.size(56.dp)
            )
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = KidOrange)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ups! ${uiState.error}",
                        color = KidRed,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            uiState.letter != null -> {
                LetterDetailContent(
                    letter = uiState.letter!!,
                    isPlaying = uiState.isPlaying,
                    onPlayAudio = { viewModel.toggleAudio() }
                )
            }
        }
    }
}

@Composable
fun LetterDetailContent(
    letter: Letter,
    isPlaying: Boolean,
    onPlayAudio: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large animated letter card
        Box(
            modifier = Modifier
                .size(280.dp)
                .graphicsLayer { translationY = bounce }
                .shadow(16.dp, RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(KidOrange, KidYellow)
                    ),
                    shape = RoundedCornerShape(32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = letter.letter,
                    fontSize = 120.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = letter.lowercase,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Audio button
        KidButton(
            text = if (isPlaying) "🔊 Playing..." else "🔊 Dengarkan",
            onClick = onPlayAudio,
            mainColor = KidPurple,
            modifier = Modifier.fillMaxWidth(0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Example word section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contoh Kata",
                    style = MaterialTheme.typography.titleMedium,
                    color = KidPurple,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Example word image placeholder
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(KidOrange.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📷",
                        fontSize = 48.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = letter.exampleWord,
                    fontSize = 32.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Encouragement message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(KidGreen, KidBlue)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🎉 Kamu Hebat! Terus Belajar Ya! 🌟",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
