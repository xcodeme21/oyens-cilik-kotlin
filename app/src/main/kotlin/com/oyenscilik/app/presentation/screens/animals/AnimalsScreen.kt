package com.oyenscilik.app.presentation.screens.animals

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.app.presentation.theme.*

data class AnimalData(
    val id: Int,
    val name: String,
    val nameEn: String,
    val emoji: String,
    val description: String,
    val funFact: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimal: (Int) -> Unit,
    onNavigateToQuiz: () -> Unit
) {
    val animals = remember {
        listOf(
            AnimalData(1, "Kucing", "Cat", "🐱", "Hewan peliharaan yang lucu", "Tidur 16 jam/hari!", AnimalCardOrange),
            AnimalData(2, "Anjing", "Dog", "🐕", "Sahabat terbaik manusia", "Dengar 4x lebih jauh!", AnimalCardBlue),
            AnimalData(3, "Gajah", "Elephant", "🐘", "Hewan darat terbesar", "Ingatan super kuat!", AnimalCardGreen),
            AnimalData(4, "Singa", "Lion", "🦁", "Raja hutan", "Tidur 20 jam/hari!", AnimalCardYellow),
            AnimalData(5, "Harimau", "Tiger", "🐯", "Kucing terbesar", "Belang unik!", AnimalCardOrange),
            AnimalData(6, "Jerapah", "Giraffe", "🦒", "Hewan paling tinggi", "Lidah 50 cm!", AnimalCardYellow),
            AnimalData(7, "Kuda Nil", "Hippopotamus", "🦛", "Suka di air", "Tahan napas 5 menit!", AnimalCardBlue),
            AnimalData(8, "Zebra", "Zebra", "🦓", "Kuda belang", "Pola unik!", AnimalCardGreen),
            AnimalData(9, "Kelinci", "Rabbit", "🐰", "Berbulu lembut", "Lompat 1 meter!", AnimalCardPink),
            AnimalData(10, "Lumba-lumba", "Dolphin", "🐬", "Mamalia laut cerdas", "Tidur mata terbuka!", AnimalCardBlue),
            AnimalData(11, "Penguin", "Penguin", "🐧", "Burung tak bisa terbang", "Minum air laut!", AnimalCardBlue),
            AnimalData(12, "Burung Hantu", "Owl", "🦉", "Aktif malam hari", "Putar kepala 270°!", AnimalCardYellow),
            AnimalData(13, "Buaya", "Crocodile", "🐊", "Reptil besar", "Sejak zaman dinosaurus!", AnimalCardGreen),
            AnimalData(14, "Kura-kura", "Turtle", "🐢", "Bawa rumah di punggung", "Hidup 100+ tahun!", AnimalCardGreen),
            AnimalData(15, "Monyet", "Monkey", "🐵", "Lincah dan cerdas", "Kenal wajah di cermin!", AnimalCardOrange)
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundCream, BackgroundLight)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        "Tebak Hewan 🦁",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    // Quiz Button
                    FilledTonalButton(
                        onClick = onNavigateToQuiz,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = GreenSuccess,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Quiz, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Main Quiz")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            Text(
                text = "Pilih hewan untuk belajar!",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animals) { animal ->
                    AnimalCard(
                        animal = animal,
                        onClick = { onNavigateToAnimal(animal.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimalCard(
    animal: AnimalData,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = animal.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = animal.emoji,
                    fontSize = 36.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = animal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = animal.nameEn,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = animal.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}
