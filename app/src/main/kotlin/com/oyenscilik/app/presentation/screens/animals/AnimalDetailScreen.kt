package com.oyenscilik.app.presentation.screens.animals

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.oyenscilik.app.data.audio.AudioPlayerManager
import com.oyenscilik.app.presentation.components.LoginRequiredDialog
import com.oyenscilik.app.presentation.theme.*
import com.oyenscilik.app.presentation.viewmodel.LessonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(
    animalId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: LessonViewModel = hiltViewModel(),
    audioPlayerManager: AudioPlayerManager
) {
    var showLoginDialog by remember { mutableStateOf(false) }
    
    val shouldShowLogin by viewModel.shouldShowLogin.collectAsState(initial = false)
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val isPlaying by audioPlayerManager.isPlaying.collectAsState()
    val currentAudioId by audioPlayerManager.currentAudioId.collectAsState()
    
    // Audio URLs from backend - null means fallback to TTS
    val animalAudioUrls = remember {
        mapOf<Int, String?>(
            // Will be populated from backend
            // Example: 1 to "animals/kucing.mp3"
        )
    }
    
    val animals = remember {
        mapOf(
            1 to AnimalDetail("Kucing", "Cat", "🐱", "Kucing adalah hewan peliharaan yang lucu dan menggemaskan.", "Kucing bisa tidur sampai 16 jam sehari!", AnimalCardOrange),
            2 to AnimalDetail("Anjing", "Dog", "🐕", "Anjing adalah sahabat terbaik manusia yang setia.", "Anjing bisa mendengar suara 4 kali lebih jauh dari manusia!", AnimalCardBlue),
            3 to AnimalDetail("Gajah", "Elephant", "🐘", "Gajah adalah hewan darat terbesar di dunia.", "Gajah bisa mengingat teman-temannya selama bertahun-tahun!", AnimalCardGreen),
            4 to AnimalDetail("Singa", "Lion", "🦁", "Singa sering disebut sebagai raja hutan.", "Singa jantan bisa tidur sampai 20 jam sehari!", AnimalCardYellow),
            5 to AnimalDetail("Harimau", "Tiger", "🐯", "Harimau adalah kucing terbesar di dunia.", "Setiap harimau memiliki pola belang yang unik!", AnimalCardOrange),
            6 to AnimalDetail("Jerapah", "Giraffe", "🦒", "Jerapah adalah hewan paling tinggi di dunia.", "Lidah jerapah bisa sepanjang 50 cm!", AnimalCardYellow),
            7 to AnimalDetail("Kuda Nil", "Hippopotamus", "🦛", "Kuda nil menghabiskan sebagian besar waktunya di air.", "Kuda nil bisa menahan napas sampai 5 menit!", AnimalCardBlue),
            8 to AnimalDetail("Zebra", "Zebra", "🦓", "Zebra memiliki belang hitam putih yang indah.", "Tidak ada dua zebra yang memiliki pola belang yang sama!", AnimalCardGreen),
            9 to AnimalDetail("Kelinci", "Rabbit", "🐰", "Kelinci adalah hewan berbulu lembut dengan telinga panjang.", "Kelinci bisa melompat setinggi 1 meter!", AnimalCardPink),
            10 to AnimalDetail("Lumba-lumba", "Dolphin", "🐬", "Lumba-lumba adalah mamalia laut yang sangat cerdas.", "Lumba-lumba tidur dengan satu mata terbuka!", AnimalCardBlue),
            11 to AnimalDetail("Penguin", "Penguin", "🐧", "Penguin adalah burung yang tidak bisa terbang tapi pandai berenang.", "Penguin bisa minum air laut!", AnimalCardBlue),
            12 to AnimalDetail("Burung Hantu", "Owl", "🦉", "Burung hantu aktif di malam hari dan memiliki mata besar.", "Burung hantu bisa memutar kepalanya hampir 270 derajat!", AnimalCardYellow),
            13 to AnimalDetail("Buaya", "Crocodile", "🐊", "Buaya adalah reptil besar yang hidup di sungai.", "Buaya sudah ada sejak zaman dinosaurus!", AnimalCardGreen),
            14 to AnimalDetail("Kura-kura", "Turtle", "🐢", "Kura-kura membawa rumahnya di punggungnya.", "Beberapa kura-kura bisa hidup lebih dari 100 tahun!", AnimalCardGreen),
            15 to AnimalDetail("Monyet", "Monkey", "🐵", "Monyet adalah hewan yang sangat lincah dan cerdas.", "Monyet bisa mengenali wajahnya di cermin!", AnimalCardOrange)
        )
    }
    
    val animal = animals[animalId] ?: animals[1]!!
    val isCurrentlyPlaying = isPlaying && currentAudioId == "animal_${animal.name}"
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // Sound button pulse animation when playing
    val soundButtonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCurrentlyPlaying) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "soundButtonScale"
    )
    
    // Clean up audio when leaving
    DisposableEffect(Unit) {
        onDispose {
            audioPlayerManager.stop()
        }
    }
    
    // Record progress when viewing this lesson and check login requirement
    LaunchedEffect(animalId) {
        viewModel.recordLessonCompleted("animal", animalId)
    }
    
    // Show login dialog if needed when entering this screen
    LaunchedEffect(shouldShowLogin, isLoggedIn) {
        if (shouldShowLogin && !isLoggedIn) {
            showLoginDialog = true
        }
    }
    
    fun speakAnimal() {
        val audioUrl = animalAudioUrls[animalId]
        audioPlayerManager.playAnimal(
            name = animal.name,
            nameEn = animal.nameEn,
            audioUrl = audioUrl
        )
    }
    
    // Login Required Dialog
    if (showLoginDialog) {
        LoginRequiredDialog(
            onLoginClick = {
                showLoginDialog = false
                onNavigateToLogin()
            },
            onRegisterClick = {
                showLoginDialog = false
                onNavigateToRegister()
            },
            onDismiss = { showLoginDialog = false }
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(animal.color, BackgroundLight)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(animal.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Big Emoji
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = animal.emoji,
                        fontSize = 100.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Name
                Text(
                    text = animal.name,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Text(
                    text = animal.nameEn,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sound Button with pulse animation
                Button(
                    onClick = { speakAnimal() },
                    modifier = Modifier
                        .height(56.dp)
                        .scale(soundButtonScale),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentlyPlaying) Orange.copy(alpha = 0.8f) else Orange
                    )
                ) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Dengar Suara",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isCurrentlyPlaying) "Sedang Diputar..." else "Dengar Suara",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "📖 Tentang ${animal.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = animal.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Fun Fact Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = YellowSoft)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "💡 Fakta Menarik",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = animal.funFact,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private data class AnimalDetail(
    val name: String,
    val nameEn: String,
    val emoji: String,
    val description: String,
    val funFact: String,
    val color: Color
)
