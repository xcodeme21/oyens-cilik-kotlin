package com.oyenscilik.app.presentation.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.app.presentation.theme.*

/**
 * Level configuration - matches backend LEVEL_CONFIG
 */
data class LevelInfo(
    val level: Int,
    val minStars: Int,
    val maxStars: Int,
    val title: String
)

val LEVELS = listOf(
    LevelInfo(1, 0, 49, "Pemula"),
    LevelInfo(2, 50, 149, "Pelajar"),
    LevelInfo(3, 150, 299, "Mahir"),
    LevelInfo(4, 300, 499, "Ahli"),
    LevelInfo(5, 500, 999999, "Master")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {}
) {
    // TODO: Replace with real data from ViewModel
    val childName = "Anak Pintar"
    val totalStars = 75
    val currentLevel = 2
    val levelTitle = "Pelajar"
    val starsToNextLevel = 75 // Need 75 more to reach level 3 (150)
    val streak = 5
    val daysActive = 12
    val totalLessons = 25
    val lettersProgress = 12 to 26
    val numbersProgress = 8 to 21
    val animalsProgress = 5 to 15
    
    // Weekly activity (sample data)
    val weeklyActivity = listOf(true, true, true, false, false, false, false)
    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
    
    // Animation
    val infiniteTransition = rememberInfiniteTransition(label = "profile")
    val starPulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ), label = "starPulse"
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Saya") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Settings */ }) {
                        Icon(Icons.Default.Settings, "Pengaturan", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFF8E7), BackgroundLight)
                    )
                )
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // ===== Profile Header =====
            ProfileHeader(
                name = childName,
                level = currentLevel,
                levelTitle = levelTitle,
                onEditClick = { /* TODO: Edit profile */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ===== Level Progress =====
            LevelProgressCard(
                currentLevel = currentLevel,
                levelTitle = levelTitle,
                totalStars = totalStars,
                starsToNextLevel = starsToNextLevel,
                starPulse = starPulse
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ===== Stats Grid =====
            StatsGridCard(
                totalStars = totalStars,
                streak = streak,
                daysActive = daysActive,
                totalLessons = totalLessons
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ===== Learning Progress =====
            LearningProgressCard(
                lettersProgress = lettersProgress,
                numbersProgress = numbersProgress,
                animalsProgress = animalsProgress
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // ===== Activity Calendar =====
            ActivityCalendarCard(
                days = days,
                activity = weeklyActivity
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // ===== Logout Button =====
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFE53935)
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeader(
    name: String,
    level: Int,
    levelTitle: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Purple, Orange)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🐱", fontSize = 44.sp)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏆", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Level $level - $levelTitle",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Purple
                )
            }
        }
        
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, "Edit", tint = TextSecondary)
        }
    }
}

@Composable
private fun LevelProgressCard(
    currentLevel: Int,
    levelTitle: String,
    totalStars: Int,
    starsToNextLevel: Int,
    starPulse: Float
) {
    val currentLevelInfo = LEVELS.find { it.level == currentLevel } ?: LEVELS[0]
    val nextLevelInfo = LEVELS.find { it.level == currentLevel + 1 }
    
    val progressInLevel = if (nextLevelInfo != null) {
        val starsInCurrentLevel = totalStars - currentLevelInfo.minStars
        val starsNeeded = nextLevelInfo.minStars - currentLevelInfo.minStars
        (starsInCurrentLevel.toFloat() / starsNeeded).coerceIn(0f, 1f)
    } else {
        1f // Max level
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐ Progress Level",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$totalStars bintang",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Orange,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = progressInLevel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Purple,
                trackColor = Color(0xFFE8D5FF)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Lv.$currentLevel $levelTitle",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                if (nextLevelInfo != null) {
                    Text(
                        text = "$starsToNextLevel ⭐ lagi ke Lv.${nextLevelInfo.level}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Orange
                    )
                } else {
                    Text(
                        text = "Level Maksimal! 🎉",
                        style = MaterialTheme.typography.labelMedium,
                        color = GreenSuccess
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsGridCard(
    totalStars: Int,
    streak: Int,
    daysActive: Int,
    totalLessons: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📊 Statistik",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatGridItem(emoji = "⭐", value = totalStars.toString(), label = "Bintang")
                StatGridItem(emoji = "🔥", value = "$streak", label = "Streak")
                StatGridItem(emoji = "📅", value = "$daysActive", label = "Hari Aktif")
                StatGridItem(emoji = "📚", value = "$totalLessons", label = "Pelajaran")
            }
        }
    }
}

@Composable
private fun StatGridItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 24.sp)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun LearningProgressCard(
    lettersProgress: Pair<Int, Int>,
    numbersProgress: Pair<Int, Int>,
    animalsProgress: Pair<Int, Int>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📖 Progress Belajar",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ProgressItem(
                emoji = "🔤",
                title = "Huruf",
                progress = lettersProgress.first,
                total = lettersProgress.second,
                color = Purple
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ProgressItem(
                emoji = "🔢",
                title = "Angka",
                progress = numbersProgress.first,
                total = numbersProgress.second,
                color = Orange
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ProgressItem(
                emoji = "🦁",
                title = "Hewan",
                progress = animalsProgress.first,
                total = animalsProgress.second,
                color = GreenSuccess
            )
        }
    }
}

@Composable
private fun ProgressItem(
    emoji: String,
    title: String,
    progress: Int,
    total: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = "$progress/$total",
                    style = MaterialTheme.typography.labelMedium,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = (progress.toFloat() / total).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun ActivityCalendarCard(
    days: List<String>,
    activity: List<Boolean>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    CalendarDayItem(
                        day = day,
                        isActive = activity.getOrNull(index) ?: false,
                        isToday = index == 3
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(day: String, isActive: Boolean, isToday: Boolean) {
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
