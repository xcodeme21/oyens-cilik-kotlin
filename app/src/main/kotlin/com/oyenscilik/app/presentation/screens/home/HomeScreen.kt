package com.oyenscilik.app.presentation.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.app.presentation.theme.*

@Composable
fun HomeScreen(
    onNavigateToLetters: () -> Unit,
    onNavigateToNumbers: () -> Unit,
    onNavigateToAnimals: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "home")
    
    // Subtle wave animation for greeting
    val wave by infiniteTransition.animateFloat(
        initialValue = -10f, targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "wave"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8E7),
                        Color(0xFFFFEFD5),
                        BackgroundLight
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // ===== Compact Header =====
            CompactHeader(
                childName = "Anak Pintar",
                onProfileClick = onNavigateToProfile,
                wave = wave
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // ===== Stats Row =====
            StatsRow(
                stars = 45,
                streak = 5,
                level = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ===== Learning Grid (2 columns) =====
            Text(
                text = "Pilih Pelajaran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LearningCard(
                    title = "Huruf",
                    emoji = "🔤",
                    progress = 12,
                    total = 26,
                    gradient = listOf(Color(0xFFE8D5FF), Color(0xFFD8C4FF)),
                    accentColor = Purple,
                    onClick = onNavigateToLetters,
                    modifier = Modifier.weight(1f)
                )
                LearningCard(
                    title = "Angka",
                    emoji = "🔢",
                    progress = 8,
                    total = 21,
                    gradient = listOf(Color(0xFFFFE4CC), Color(0xFFFFD4B3)),
                    accentColor = Orange,
                    onClick = onNavigateToNumbers,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LearningCard(
                    title = "Hewan",
                    emoji = "🦁",
                    progress = 5,
                    total = 15,
                    gradient = listOf(Color(0xFFD4F5D4), Color(0xFFB8EBB8)),
                    accentColor = GreenSuccess,
                    onClick = onNavigateToAnimals,
                    modifier = Modifier.weight(1f)
                )
                LearningCard(
                    title = "Profil",
                    emoji = "👤",
                    progress = null,
                    total = null,
                    gradient = listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE)),
                    accentColor = BlueInfo,
                    onClick = onNavigateToProfile,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // ===== Weekly Activity Calendar =====
            WeeklyActivityCard()
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // ===== Mascot Mini Section =====
            MascotMiniSection()
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CompactHeader(
    childName: String,
    onProfileClick: () -> Unit,
    wave: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onProfileClick)
                .padding(end = 8.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Purple, Orange)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐱", fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(10.dp))
            
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Halo, $childName!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "👋",
                        fontSize = 18.sp,
                        modifier = Modifier.rotate(wave)
                    )
                }
                Text(
                    text = "Siap belajar hari ini?",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
        
        // Notification Bell (placeholder)
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifikasi",
                tint = TextSecondary
            )
        }
    }
}

@Composable
private fun StatsRow(stars: Int, streak: Int, level: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(emoji = "⭐", value = stars.toString(), label = "Bintang")
            VerticalDivider(modifier = Modifier.height(36.dp))
            StatItem(emoji = "🔥", value = "$streak hari", label = "Streak")
            VerticalDivider(modifier = Modifier.height(36.dp))
            StatItem(emoji = "🏆", value = "Lv.$level", label = "Level")
        }
    }
}

@Composable
private fun VerticalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(Color(0xFFE0E0E0))
    )
}

@Composable
private fun StatItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun LearningCard(
    title: String,
    emoji: String,
    progress: Int?,
    total: Int?,
    gradient: List<Color>,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )
    
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradient))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Emoji
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 28.sp)
                }
                
                // Title and Progress
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (progress != null && total != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        // Progress bar
                        LinearProgressIndicator(
                            progress = (progress.toFloat() / total).coerceIn(0f, 1f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = accentColor,
                            trackColor = Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$progress/$total",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor
                        )
                    }
                }
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun WeeklyActivityCard() {
    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
    val activity = listOf(true, true, true, false, false, false, false) // Sample data
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📅 Aktivitas Minggu Ini",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEachIndexed { index, day ->
                    DayActivityItem(
                        day = day,
                        isActive = activity[index],
                        isToday = index == 3 // Example: Thursday is today
                    )
                }
            }
        }
    }
}

@Composable
private fun DayActivityItem(day: String, isActive: Boolean, isToday: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            color = if (isToday) Purple else TextSecondary,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isActive -> GreenSuccess.copy(alpha = 0.2f)
                        isToday -> Purple.copy(alpha = 0.1f)
                        else -> Color(0xFFF5F5F5)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isActive -> Text(text = "✓", color = GreenSuccess, fontWeight = FontWeight.Bold)
                isToday -> Text(text = "•", color = Purple, fontSize = 20.sp)
                else -> Text(text = "−", color = TextSecondary.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun MascotMiniSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ), label = "bounce"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4E6)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🐱",
                fontSize = 48.sp,
                modifier = Modifier.offset(y = (-bounce).dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Oyens bilang:",
                    style = MaterialTheme.typography.labelMedium,
                    color = Orange
                )
                Text(
                    text = "\"Ayo lanjutkan belajar! Kamu hebat! 🌟\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
        }
    }
}
