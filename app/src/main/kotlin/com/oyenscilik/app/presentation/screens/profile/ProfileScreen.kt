package com.oyenscilik.app.presentation.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.oyenscilik.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    
    // Animation
    val infiniteTransition = rememberInfiniteTransition(label = "profile")
    val starPulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ), label = "starPulse"
    )
    
    if (showEditDialog) {
        EditNameDialog(
            currentName = uiState.childName,
            onDismiss = { showEditDialog = false },
            onConfirm = { newName ->
                viewModel.updateChildName(newName)
                showEditDialog = false
            }
        )
    }
    
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Purple)
            }
        } else {
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
                    name = uiState.childName,
                    level = uiState.currentLevel,
                    levelTitle = uiState.levelTitle,
                    onEditClick = { 
                        editName = uiState.childName
                        showEditDialog = true 
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // ===== Level Progress =====
                LevelProgressCard(
                    currentLevel = uiState.currentLevel,
                    levelTitle = uiState.levelTitle,
                    totalStars = uiState.totalStars,
                    starsToNextLevel = uiState.starsToNextLevel,
                    starPulse = starPulse
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // ===== Achievements Section =====
                AchievementsSection(achievements = uiState.achievements)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // ===== Stats Grid =====
                StatsGridCard(
                    totalStars = uiState.totalStars,
                    streak = uiState.streak,
                    daysActive = uiState.daysActive,
                    totalLessons = uiState.totalLessons
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // ===== Learning Progress =====
                LearningProgressCard(
                    lettersProgress = uiState.lettersProgress,
                    numbersProgress = uiState.numbersProgress,
                    animalsProgress = uiState.animalsProgress
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // ===== Activity Calendar =====
                val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                ActivityCalendarCard(
                    days = days,
                    activity = uiState.weeklyActivity
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
}

@Composable
private fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✏️ Edit Nama",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Anak") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = { onConfirm(name) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple)
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementsSection(achievements: List<Achievement>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏆 Pencapaian",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${achievements.count { it.isUnlocked }}/${achievements.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Orange
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(achievements) { achievement ->
                    AchievementItem(achievement = achievement)
                }
            }
        }
    }
}

@Composable
private fun AchievementItem(achievement: Achievement) {
    val scale by animateFloatAsState(
        targetValue = if (achievement.isUnlocked) 1f else 0.9f,
        label = "achievementScale"
    )
    
    Column(
        modifier = Modifier
            .width(80.dp)
            .scale(scale)
            .alpha(if (achievement.isUnlocked) 1f else 0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(
                    if (achievement.isUnlocked)
                        Brush.linearGradient(listOf(Purple, Orange))
                    else
                        Brush.linearGradient(listOf(Color.Gray.copy(0.3f), Color.Gray.copy(0.3f)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = achievement.emoji, fontSize = 28.sp)
            if (achievement.isUnlocked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(GreenSuccess),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = achievement.title,
            style = MaterialTheme.typography.labelSmall,
            color = if (achievement.isUnlocked) TextPrimary else TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
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
    val levels = listOf(
        Triple(1, 0, 49),
        Triple(2, 50, 149),
        Triple(3, 150, 299),
        Triple(4, 300, 499),
        Triple(5, 500, 999999)
    )
    
    val currentLevelInfo = levels.find { it.first == currentLevel }
    val nextLevelInfo = levels.find { it.first == currentLevel + 1 }
    
    val progressInLevel = if (nextLevelInfo != null && currentLevelInfo != null) {
        val starsInCurrentLevel = totalStars - currentLevelInfo.second
        val starsNeeded = nextLevelInfo.second - currentLevelInfo.second
        (starsInCurrentLevel.toFloat() / starsNeeded).coerceIn(0f, 1f)
    } else {
        1f
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.scale(starPulse)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
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
                if (starsToNextLevel > 0) {
                    Text(
                        text = "$starsToNextLevel ⭐ lagi ke Lv.${currentLevel + 1}",
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
            
            ProgressItem("🔤", "Huruf", lettersProgress.first, lettersProgress.second, Purple)
            Spacer(modifier = Modifier.height(8.dp))
            ProgressItem("🔢", "Angka", numbersProgress.first, numbersProgress.second, Orange)
            Spacer(modifier = Modifier.height(8.dp))
            ProgressItem("🦁", "Hewan", animalsProgress.first, animalsProgress.second, GreenSuccess)
        }
    }
}

@Composable
private fun ProgressItem(emoji: String, title: String, progress: Int, total: Int, color: Color) {
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
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                Text(text = "$progress/$total", style = MaterialTheme.typography.labelMedium, color = color)
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = (progress.toFloat() / total).coerceIn(0f, 1f),
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun ActivityCalendarCard(days: List<String>, activity: List<Boolean>) {
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
                    CalendarDayItem(day, activity.getOrNull(index) ?: false, index == 3)
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
                isActive -> Text("✓", color = GreenSuccess, fontWeight = FontWeight.Bold)
                isToday -> Text("•", color = Purple, fontSize = 20.sp)
                else -> Text("−", color = TextSecondary.copy(alpha = 0.5f))
            }
        }
    }
}
