package com.oyenscilik.presentation.screens.animals

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val AccentGreen = Color(0xFF11998E)
private val ComicYellow = Color(0xFFFFD93D)
private val ComicPink = Color(0xFFFF6B9D)

@Composable
fun AnimalDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigatePrev: (() -> Unit)? = null,
    onNavigateNext: (() -> Unit)? = null,
    viewModel: AnimalDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val hasPrev = onNavigatePrev != null
    val hasNext = onNavigateNext != null

    val infiniteTransition = rememberInfiniteTransition(label = "animal")
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
        // Decorative shapes - comic style
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = 300.dp)
                .background(
                    brush = Brush.radialGradient(listOf(ComicYellow.copy(0.15f), Color.Transparent)),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = 250.dp, y = 500.dp)
                .background(
                    brush = Brush.radialGradient(listOf(ComicPink.copy(0.12f), Color.Transparent)),
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
                    text = "Mengenal Hewan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text("🦁", fontSize = 28.sp)
            }

            when {
                uiState.isLoading -> {
                    // Skeleton Loading
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(60.dp))
                        // Image skeleton
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .shadow(24.dp, RoundedCornerShape(40.dp))
                                .clip(RoundedCornerShape(40.dp))
                                .background(Color.LightGray.copy(0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentGreen)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        // Name skeleton
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray.copy(0.3f))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Description skeleton
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.LightGray.copy(0.3f))
                        )
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                        Text("${uiState.error}", color = Color(0xFFFF6B6B), fontSize = 16.sp)
                    }
                }
                uiState.animal != null -> {
                    val animal = uiState.animal!!

                    Spacer(modifier = Modifier.height(40.dp))

                    // Main content with navigation
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
                                color = if (hasPrev) AccentGreen else Color.Gray.copy(0.3f)
                            )
                        }

                        // Main animal image - COMIC STYLE
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                                .offset(y = (-bounce).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .shadow(
                                        elevation = 24.dp,
                                        shape = RoundedCornerShape(40.dp),
                                        ambientColor = ComicYellow.copy(0.3f)
                                    )
                                    .clip(RoundedCornerShape(40.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(ComicYellow.copy(0.8f), ComicPink.copy(0.7f))
                                        )
                                    )
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val context = LocalContext.current
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(animal.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = animal.name,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
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
                                color = if (hasNext) AccentGreen else Color.Gray.copy(0.3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Name card - COMIC BUBBLE STYLE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(30.dp))
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = animal.name,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = animal.nameEn,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = AccentGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // TTS Button - COMIC STYLE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(16.dp, RoundedCornerShape(32.dp))
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                                )
                            )
                            .clickable { viewModel.toggleAudio() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (uiState.isPlaying) "🔊 Sedang Berbicara..." else "🔊 Dengarkan Cerita",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Description - COMIC PANEL STYLE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(20.dp, RoundedCornerShape(28.dp))
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(Color(0xFFFFF5E1), Color(0xFFFFE4B5))
                                )
                            )
                            .padding(28.dp)
                    ) {
                        Column {
                            Text(
                                text = "💬 Fakta Menarik",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B4513)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = animal.description,
                                fontSize = 18.sp,
                                lineHeight = 26.sp,
                                color = TextDark,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "🌟 ${animal.funFact}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFFF6B35),
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
