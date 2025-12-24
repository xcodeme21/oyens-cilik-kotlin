package com.oyenscilik.presentation.screens.animals

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Cream = Color(0xFFFFFBF5)
private val Peach = Color(0xFFFFE5D9)
private val TextDark = Color(0xFF2D3436)

data class AnimalItem(
    val id: Int,
    val name: String,
    val emoji: String
)

@Composable
fun AnimalsScreen(
    onNavigateToAnimal: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val animals = listOf(
        AnimalItem(1, "Singa", "🦁"),
        AnimalItem(2, "Gajah", "🐘"),
        AnimalItem(3, "Jerapah", "🦒"),
        AnimalItem(4, "Monyet", "🐵"),
        AnimalItem(5, "Zebra", "🦓"),
        AnimalItem(6, "Harimau", "🐯"),
        AnimalItem(7, "Beruang", "🐻"),
        AnimalItem(8, "Kucing", "🐱"),
        AnimalItem(9, "Anjing", "🐶"),
        AnimalItem(10, "Kelinci", "🐰"),
        AnimalItem(11, "Burung", "🐦"),
        AnimalItem(12, "Ikan", "🐟"),
        AnimalItem(13, "Kura-kura", "🐢"),
        AnimalItem(14, "Buaya", "🐊"),
        AnimalItem(15, "Lumba-lumba", "🐬")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Cream, Color(0xFFFFF8F0), Peach.copy(0.2f))))
    ) {
        // Decorative bg
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 180.dp, y = 300.dp)
                .background(
                    brush = Brush.radialGradient(listOf(Color(0xFF11998E).copy(0.15f), Color.Transparent)),
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
                    Text(
                        text = "Mengenal Hewan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text("${animals.size} Jenis Hewan", fontSize = 13.sp, color = Color(0xFF636E72))
                }

                Text("🦁", fontSize = 32.sp)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(animals) { index, animal ->
                    PremiumAnimalCard(
                        animal = animal,
                        index = index,
                        onClick = { onNavigateToAnimal(animal.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumAnimalCard(
    animal: AnimalItem,
    index: Int,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )

    val gradients = listOf(
        listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
        listOf(Color(0xFFFF8C42), Color(0xFFFF6B35)),
        listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
        listOf(Color(0xFFFF6B6B), Color(0xFFFF8E53)),
        listOf(Color(0xFF4FACFE), Color(0xFF00F2FE)),
        listOf(Color(0xFFA770EF), Color(0xFFCF8BF3))
    )

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay((index * 35).toLong())
        isVisible = true
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(initialScale = 0.8f) + fadeIn()
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(0.9f)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = gradients[index % gradients.size][0].copy(0.2f))
                .clip(RoundedCornerShape(20.dp))
                .background(brush = Brush.linearGradient(gradients[index % gradients.size]))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPressed = true
                    onClick()
                }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(animal.emoji, fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = animal.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
