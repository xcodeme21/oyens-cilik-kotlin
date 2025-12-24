package com.oyenscilik.presentation.screens.numbers

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val AccentPurple = Color(0xFF667EEA)

data class NumberInfo(
    val number: Int,
    val word: String
)

val numberDetails = listOf(
    NumberInfo(0, "Nol"),
    NumberInfo(1, "Satu"),
    NumberInfo(2, "Dua"),
    NumberInfo(3, "Tiga"),
    NumberInfo(4, "Empat"),
    NumberInfo(5, "Lima"),
    NumberInfo(6, "Enam"),
    NumberInfo(7, "Tujuh"),
    NumberInfo(8, "Delapan"),
    NumberInfo(9, "Sembilan"),
    NumberInfo(10, "Sepuluh"),
    NumberInfo(11, "Sebelas"),
    NumberInfo(12, "Dua Belas"),
    NumberInfo(13, "Tiga Belas"),
    NumberInfo(14, "Empat Belas"),
    NumberInfo(15, "Lima Belas"),
    NumberInfo(16, "Enam Belas"),
    NumberInfo(17, "Tujuh Belas"),
    NumberInfo(18, "Delapan Belas"),
    NumberInfo(19, "Sembilan Belas"),
    NumberInfo(20, "Dua Puluh")
)

@Composable
fun NumberDetailScreen(
    numberId: Int,
    onNavigateBack: () -> Unit,
    onNavigatePrev: () -> Unit = {},
    onNavigateNext: () -> Unit = {}
) {
    var currentId by remember { mutableStateOf(numberId) }
    val numberInfo = numberDetails.getOrNull(currentId) ?: numberDetails[0]
    val hasPrev = currentId > 0
    val hasNext = currentId < numberDetails.size - 1
    
    val infiniteTransition = rememberInfiniteTransition(label = "number")
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
                .offset(x = (-80).dp, y = 200.dp)
                .background(
                    brush = Brush.radialGradient(listOf(AccentPurple.copy(0.12f), Color.Transparent)),
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
                    text = "Angka ${numberInfo.number}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text("🔢", fontSize = 28.sp)
            }

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
                            if (hasPrev) currentId--
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "◀",
                        fontSize = 24.sp,
                        color = if (hasPrev) AccentPurple else Color.Gray.copy(0.3f)
                    )
                }

                // Main number card
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
                            .shadow(24.dp, CircleShape, ambientColor = AccentPurple.copy(0.2f))
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(listOf(AccentPurple, Color(0xFF764BA2)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${numberInfo.number}",
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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
                        .clickable(enabled = hasNext) {
                            if (hasNext) currentId++
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        fontSize = 24.sp,
                        color = if (hasNext) AccentPurple else Color.Gray.copy(0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Word card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = numberInfo.word,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Dibaca: ${numberInfo.word.uppercase()}",
                        fontSize = 14.sp,
                        color = Color(0xFF636E72)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Count examples
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Contoh Menghitung", fontSize = 14.sp, color = Color(0xFF636E72))
                    Spacer(modifier = Modifier.height(12.dp))
                    if (numberInfo.number == 0) {
                        Text("(kosong)", fontSize = 20.sp, color = Color.Gray)
                    } else {
                        Text(
                            text = "⭐".repeat(minOf(numberInfo.number, 10)),
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center
                        )
                        if (numberInfo.number > 10) {
                            Text(
                                text = "⭐".repeat(numberInfo.number - 10),
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Position indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${currentId + 1} / ${numberDetails.size}",
                    fontSize = 14.sp,
                    color = Color(0xFF636E72)
                )
            }
        }
    }
}
