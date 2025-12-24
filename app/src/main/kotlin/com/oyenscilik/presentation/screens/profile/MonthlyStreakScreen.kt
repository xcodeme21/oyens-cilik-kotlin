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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val TextGray = Color(0xFF636E72)
private val AccentOrange = Color(0xFFFF8C42)
private val AccentGreen = Color(0xFF11998E)

@Composable
fun MonthlyStreakScreen(
    onNavigateBack: () -> Unit
) {
    val currentStreak = 7
    val longestStreak = 12
    val totalActiveDays = 15
    val targetDays = 20
    val achievementPercent = (totalActiveDays * 100) / targetDays
    val completedDates = listOf(1, 2, 3, 5, 6, 7, 8, 10, 12, 13, 14, 15, 18, 20, 22)
    
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val today = LocalDate.now().dayOfMonth

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Cream, Color(0xFFFFF8F0), Peach.copy(0.2f))))
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 150.dp, y = 200.dp)
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

                Text("Streak Bulanan", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)

                Text("📅", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Progress card
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
                    // Progress circle
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shadow(16.dp, CircleShape, ambientColor = AccentOrange.copy(0.2f))
                                .clip(CircleShape)
                                .background(AccentOrange.copy(0.1f))
                        )
                        Box(
                            modifier = Modifier
                                .size((140 * achievementPercent / 100).dp.coerceAtLeast(50.dp))
                                .shadow(8.dp, CircleShape)
                                .background(brush = Brush.radialGradient(listOf(AccentOrange, Color(0xFFFF6B35))), shape = CircleShape)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$totalActiveDays", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Text("/ $targetDays", fontSize = 14.sp, color = TextGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val (badge, badgeText) = when {
                        achievementPercent >= 100 -> "🏆" to "Champion!"
                        achievementPercent >= 75 -> "⭐" to "Hebat!"
                        achievementPercent >= 50 -> "💪" to "Lanjutkan!"
                        else -> "🌱" to "Semangat!"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(AccentOrange.copy(0.15f))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(badge, fontSize = 22.sp)
                            Text(badgeText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AccentOrange)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StreakStatCard(Modifier.weight(1f), "🔥", "$currentStreak", "Streak")
                StreakStatCard(Modifier.weight(1f), "🎯", "$longestStreak", "Terbaik")
                StreakStatCard(Modifier.weight(1f), "📊", "$achievementPercent%", "Progress")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Calendar
            Text("Kalender Bulan Ini", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("S", "S", "R", "K", "J", "S", "M").forEach { day ->
                            Text(day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, color = TextGray)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    val firstDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value % 7)
                    val rows = ((firstDayOfWeek + daysInMonth + 6) / 7)

                    repeat(rows) { row ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            repeat(7) { col ->
                                val dayIndex = row * 7 + col - firstDayOfWeek + 1
                                if (dayIndex in 1..daysInMonth) {
                                    val isCompleted = dayIndex in completedDates
                                    val isToday = dayIndex == today
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isCompleted -> AccentGreen
                                                    isToday -> AccentOrange.copy(0.3f)
                                                    else -> Color.Transparent
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "$dayIndex",
                                            fontSize = 12.sp,
                                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCompleted) Color.White else TextDark
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(AccentGreen, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Selesai", fontSize = 11.sp, color = TextGray)
                Spacer(modifier = Modifier.width(20.dp))
                Box(modifier = Modifier.size(10.dp).background(AccentOrange.copy(0.5f), CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hari Ini", fontSize = 11.sp, color = TextGray)
            }
        }
    }
}

@Composable
fun StreakStatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 22.sp)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text(label, fontSize = 10.sp, color = TextGray)
        }
    }
}
