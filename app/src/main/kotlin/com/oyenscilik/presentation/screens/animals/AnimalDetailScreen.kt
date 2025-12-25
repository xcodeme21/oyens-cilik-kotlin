package com.oyenscilik.presentation.screens.animals

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.oyenscilik.presentation.screens.animals.AnimalDetailViewModel

private val LightGray = Color(0xFFF5F7FA)
private val DarkText = Color(0xFF1A1F36)
private val LightText = Color(0xFF6E7787)
private val AccentColor = Color(0xFF4A7DFF)

@Composable
fun AnimalDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigatePrev: () -> Unit,
    onNavigateNext: () -> Unit,
    viewModel: AnimalDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val animal = uiState.animal

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
    ) {
        if (uiState.isLoading) {
            // Skeleton loading
            AnimalDetailSkeleton()
        } else if (animal != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    // Large image
                    AsyncImage(
                        model = animal.imageUrl,
                        contentDescription = animal.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Top bar with back and nav buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NavButton(icon = "←", onClick = onNavigateBack)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            NavButton(icon = "‹", onClick = onNavigatePrev)
                            NavButton(icon = "›", onClick = onNavigateNext)
                        }
                    }

                    // Bookmark button (top right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp)
                            .offset(y = 350.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { viewModel.toggleAudio() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (uiState.isPlaying) "🔊" else "🎵",
                                fontSize = 24.sp
                            )
                        }
                    }
                }

                // Content card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-30).dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Animal name
                        Column {
                            Text(
                                text = animal.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = "🌍 ${animal.nameEn}",
                                fontSize = 16.sp,
                                color = LightText
                            )
                        }

                        // Info cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                title = "Fun Fact",
                                value = "🎯",
                                color = Color(0xFFE8F5E8)
                            )
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                title = "Learn More",
                                value = "📚",
                                color = Color(0xFFE8F0FF)
                            )
                            InfoCard(
                                modifier = Modifier.weight(1f),
                                title = "Share",
                                value = "💫",
                                color = Color(0xFFFFF4E0)
                            )
                        }

                        // Description section
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Tentang",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = animal.description,
                                fontSize = 15.sp,
                                color = LightText,
                                lineHeight = 24.sp
                            )
                        }

                        // Fun fact section
                        if (animal.funFact.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFF4E0))
                                    .padding(16.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "💡 Tahukah Kamu?",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DarkText
                                    )
                                    Text(
                                        text = animal.funFact,
                                        fontSize = 14.sp,
                                        color = LightText,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NavButton(icon: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.9f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            color = DarkText,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                fontSize = 24.sp
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = LightText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnimalDetailSkeleton() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Image skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color(0xFFE0E0E0))
        )
        
        // Content skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-30).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Name skeleton
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                )
                
                // Info cards skeleton
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE0E0E0))
                        )
                    }
                }
                
                // Description skeleton
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                }
            }
        }
    }
}
