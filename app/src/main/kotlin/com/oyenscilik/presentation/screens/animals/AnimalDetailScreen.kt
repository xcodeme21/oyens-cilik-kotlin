package com.oyenscilik.presentation.screens.animals

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

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)
private val AccentGreen = Color(0xFF11998E)

data class AnimalInfo(
    val id: Int,
    val name: String,
    val emoji: String,
    val sound: String,
    val fact: String
)

val animalDetails = listOf(
    AnimalInfo(1, "Singa", "🦁", "Aum!", "Raja hutan yang gagah"),
    AnimalInfo(2, "Gajah", "🐘", "Pruut!", "Hewan darat terbesar"),
    AnimalInfo(3, "Jerapah", "🦒", "Hmm!", "Lehernya paling panjang"),
    AnimalInfo(4, "Monyet", "🐵", "Uuk uuk!", "Suka makan pisang"),
    AnimalInfo(5, "Zebra", "🦓", "Hiik!", "Kulitnya belang-belang"),
    AnimalInfo(6, "Harimau", "🐯", "Rawr!", "Kucing besar yang kuat"),
    AnimalInfo(7, "Beruang", "🐻", "Grr!", "Suka makan madu"),
    AnimalInfo(8, "Kucing", "🐱", "Meong!", "Hewan peliharaan lucu"),
    AnimalInfo(9, "Anjing", "🐶", "Guk guk!", "Sahabat setia manusia"),
    AnimalInfo(10, "Kelinci", "🐰", "...", "Telinganya panjang"),
    AnimalInfo(11, "Burung", "🐦", "Cuit cuit!", "Bisa terbang di langit"),
    AnimalInfo(12, "Ikan", "🐟", "Blub blub!", "Tinggal di dalam air"),
    AnimalInfo(13, "Kura-kura", "🐢", "...", "Jalannya pelan-pelan"),
    AnimalInfo(14, "Buaya", "🐊", "Hiss!", "Reptil yang kuat"),
    AnimalInfo(15, "Lumba-lumba", "🐬", "Klik klik!", "Mamalia air yang pintar")
)

@Composable
fun AnimalDetailScreen(
    animalId: Int,
    onNavigateBack: () -> Unit
) {
    var currentId by remember { mutableStateOf(animalId) }
    val animalInfo = animalDetails.find { it.id == currentId } ?: animalDetails[0]
    val hasPrev = animalDetails.indexOfFirst { it.id == currentId } > 0
    val hasNext = animalDetails.indexOfFirst { it.id == currentId } < animalDetails.size - 1

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
        // Decorative circle
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = (-60).dp, y = 250.dp)
                .background(
                    brush = Brush.radialGradient(listOf(AccentGreen.copy(0.12f), Color.Transparent)),
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
                            val currentIndex = animalDetails.indexOfFirst { it.id == currentId }
                            if (currentIndex > 0) {
                                currentId = animalDetails[currentIndex - 1].id
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "◀",
                        fontSize = 24.sp,
                        color = if (hasPrev) AccentGreen else Color.Gray.copy(0.3f)
                    )
                }

                // Main animal card
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
                            .shadow(24.dp, CircleShape, ambientColor = AccentGreen.copy(0.2f))
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(listOf(AccentGreen, Color(0xFF38EF7D)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = animalInfo.emoji,
                            fontSize = 90.sp
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
                            val currentIndex = animalDetails.indexOfFirst { it.id == currentId }
                            if (currentIndex < animalDetails.size - 1) {
                                currentId = animalDetails[currentIndex + 1].id
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        fontSize = 24.sp,
                        color = if (hasNext) AccentGreen else Color.Gray.copy(0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Name card
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
                        text = animalInfo.name,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = animalInfo.fact,
                        fontSize = 14.sp,
                        color = Color(0xFF636E72),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sound card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(listOf(AccentGreen, Color(0xFF38EF7D)))
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔊 Suara Hewan", fontSize = 14.sp, color = Color.White.copy(0.8f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${animalInfo.sound}\"",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Position indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val currentIndex = animalDetails.indexOfFirst { it.id == currentId } + 1
                Text(
                    text = "$currentIndex / ${animalDetails.size}",
                    fontSize = 14.sp,
                    color = Color(0xFF636E72)
                )
            }
        }
    }
}
