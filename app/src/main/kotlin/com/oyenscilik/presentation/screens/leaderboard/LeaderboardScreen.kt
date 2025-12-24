package com.oyenscilik.presentation.screens.leaderboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
private val Gold = Color(0xFFFFC107)
private val Silver = Color(0xFFB0BEC5)
private val Bronze = Color(0xFFCD7F32)

data class LeaderboardItem(
    val rank: Int,
    val name: String,
    val stars: Int,
    val level: Int,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit
) {
    val leaderboardData = listOf(
        LeaderboardItem(1, "Anak Hebat", 520, 5),
        LeaderboardItem(2, "Bintang Kecil", 485, 4),
        LeaderboardItem(3, "Si Pintar", 450, 4),
        LeaderboardItem(4, "Jagoan", 380, 4),
        LeaderboardItem(5, "Kucing Pintar", 156, 3, isCurrentUser = true),
        LeaderboardItem(6, "Pelajar Giat", 140, 3),
        LeaderboardItem(7, "Anak Rajin", 120, 2)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Cream, Color(0xFFFFF8F0), Peach.copy(0.2f))))
    ) {
        // Decorative circle
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = 150.dp)
                .background(
                    brush = Brush.radialGradient(listOf(Gold.copy(0.15f), Color.Transparent)),
                    shape = CircleShape
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
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

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Peringkat", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("Top Players", fontSize = 13.sp, color = TextGray)
                }

                Text("🏆", fontSize = 32.sp)
            }

            // Podium
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                if (leaderboardData.size >= 2) PremiumPodium(leaderboardData[1], 90, "🥈", Silver)
                if (leaderboardData.isNotEmpty()) PremiumPodium(leaderboardData[0], 120, "🥇", Gold)
                if (leaderboardData.size >= 3) PremiumPodium(leaderboardData[2], 70, "🥉", Bronze)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(leaderboardData.drop(3)) { _, item ->
                    PremiumRankCard(item = item)
                }
            }
        }
    }
}

@Composable
fun PremiumPodium(
    item: LeaderboardItem,
    height: Int,
    medal: String,
    color: Color
) {
    Column(
        modifier = Modifier.width(95.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(medal, fontSize = 36.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .shadow(12.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(brush = Brush.verticalGradient(listOf(color, color.copy(0.7f)))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⭐ ${item.stars}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Lvl ${item.level}", fontSize = 11.sp, color = Color.White.copy(0.85f))
            }
        }
    }
}

@Composable
fun PremiumRankCard(item: LeaderboardItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (item.isCurrentUser) 16.dp else 8.dp, RoundedCornerShape(16.dp), ambientColor = if (item.isCurrentUser) AccentOrange.copy(0.2f) else Color.Black.copy(0.05f))
            .clip(RoundedCornerShape(16.dp))
            .background(if (item.isCurrentUser) Color(0xFFFFF5EB) else Color.White)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (item.isCurrentUser) AccentOrange else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text("${item.rank}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (item.isCurrentUser) Color.White else TextGray)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name + if (item.isCurrentUser) " (Kamu)" else "", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Level ${item.level}", fontSize = 12.sp, color = TextGray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⭐", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("${item.stars}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gold)
            }
        }
    }
}
