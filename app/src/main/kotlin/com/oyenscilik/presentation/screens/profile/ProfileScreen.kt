package com.oyenscilik.presentation.screens.profile

import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val TextGray = Color(0xFF636E72)
private val AccentOrange = Color(0xFFFF8C42)

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStreak: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: com.oyenscilik.presentation.viewmodel.ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.childProfile
    
    val userName = profile?.name ?: "Anak Pintar"
    val level = profile?.level ?: 1
    val levelTitle = profile?.levelTitle ?: "Pemula"
    val totalStars = profile?.totalStars ?: 0
    val streak = profile?.currentStreak ?: 0
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Logout dialog
    if (showLogoutDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.4f))
                .clickable { showLogoutDialog = false },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .shadow(24.dp, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(28.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("👋", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Mau Keluar?", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Yakin mau keluar dari akun?", fontSize = 14.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF5F5F5))
                                .clickable { showLogoutDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Batal", fontWeight = FontWeight.Medium, color = TextGray)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(brush = Brush.horizontalGradient(listOf(Color(0xFFFF6B6B), Color(0xFFEE5A5A))))
                                .clickable { 
                                    showLogoutDialog = false
                                    onLogout()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Keluar", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Cream, Color(0xFFFFF8F0), Peach.copy(0.2f))))
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = 80.dp)
                .background(
                    brush = Brush.radialGradient(listOf(AccentOrange.copy(0.1f), Color.Transparent)),
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
                Box(modifier = Modifier.size(48.dp)) // Spacer to balance layout

                Text("Profil Saya", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF0F0))
                        .clickable { showLogoutDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚪", fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Avatar card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .shadow(20.dp, CircleShape, ambientColor = AccentOrange.copy(0.2f))
                                .clip(CircleShape)
                                .background(brush = Brush.linearGradient(listOf(Color(0xFFFFAA5C), AccentOrange))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🐱", fontSize = 60.sp)
                        }
                        // Level badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(36.dp)
                                .shadow(8.dp, CircleShape)
                                .background(Color(0xFF667EEA), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$level", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(userName, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("Level $level • $levelTitle", fontSize = 14.sp, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(Modifier.weight(1f), "⭐", "$totalStars", "Bintang", listOf(Color(0xFFFFC107), Color(0xFFFF9800)))
                ProfileStatCard(Modifier.weight(1f), "🔥", "$streak", "Streak", listOf(AccentOrange, Color(0xFFFF6B35)), onClick = onNavigateToStreak)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress
            Text("Progress Belajar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(16.dp))

            ProfileProgressBar("🔤", "Huruf", 18, 26, AccentOrange)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileProgressBar("🔢", "Angka", 12, 21, Color(0xFF667EEA))
            Spacer(modifier = Modifier.height(10.dp))
            ProfileProgressBar("🦁", "Hewan", 8, 15, Color(0xFF11998E))

            Spacer(modifier = Modifier.height(28.dp))

            // Streak banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .clickable { onNavigateToStreak() }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("📅 Streak Bulanan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text("Lihat kalender aktivitas", fontSize = 12.sp, color = TextGray)
                    }
                    Text("→", fontSize = 24.sp, color = AccentOrange)
                }
            }
        }
    }
}

@Composable
fun ProfileStatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String,
    gradient: List<Color>,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .height(110.dp)
            .shadow(16.dp, RoundedCornerShape(20.dp), ambientColor = gradient[0].copy(0.2f))
            .clip(RoundedCornerShape(20.dp))
            .background(brush = Brush.linearGradient(gradient))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(icon, fontSize = 28.sp)
            Column {
                Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(label, fontSize = 12.sp, color = Color.White.copy(0.85f))
            }
        }
    }
}

@Composable
fun ProfileProgressBar(
    icon: String,
    title: String,
    progress: Int,
    total: Int,
    color: Color
) {
    val percent = progress.toFloat() / total.toFloat()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2D3436))
                    Text("$progress/$total", fontSize = 14.sp, color = color, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color.copy(0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percent)
                            .fillMaxHeight()
                            .background(color, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}
