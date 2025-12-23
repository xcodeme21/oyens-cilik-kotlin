package com.oyenscilik.app.presentation.screens.letters

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oyenscilik.app.data.audio.AudioPlayerManager
import com.oyenscilik.app.presentation.components.LoginRequiredDialog
import com.oyenscilik.app.presentation.theme.*
import com.oyenscilik.app.presentation.viewmodel.LessonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LetterDetailScreen(
    letterId: Int,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
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
    // TODO: Fetch from API when content endpoint is ready
    val letterAudioUrls = remember {
        mapOf<Int, String?>(
            // Will be populated from backend
            // Example: 1 to "letters/a.mp3"
        )
    }
    
    val letters = remember {
        mapOf(
            1 to Pair("A", "Apel"), 2 to Pair("B", "Bola"), 3 to Pair("C", "Cacing"),
            4 to Pair("D", "Domba"), 5 to Pair("E", "Elang"), 6 to Pair("F", "Foto"),
            7 to Pair("G", "Gajah"), 8 to Pair("H", "Harimau"), 9 to Pair("I", "Ikan"),
            10 to Pair("J", "Jeruk"), 11 to Pair("K", "Kucing"), 12 to Pair("L", "Lebah"),
            13 to Pair("M", "Mangga"), 14 to Pair("N", "Nanas"), 15 to Pair("O", "Orang"),
            16 to Pair("P", "Pisang"), 17 to Pair("Q", "Quran"), 18 to Pair("R", "Rusa"),
            19 to Pair("S", "Singa"), 20 to Pair("T", "Tikus"), 21 to Pair("U", "Ular"),
            22 to Pair("V", "Vas"), 23 to Pair("W", "Wortel"), 24 to Pair("X", "Xilofon"),
            25 to Pair("Y", "Yoyo"), 26 to Pair("Z", "Zebra")
        )
    }
    
    val currentLetter = letters[letterId] ?: Pair("A", "Apel")
    val colors = listOf(Purple, Orange, GreenSuccess, BlueInfo, Yellow)
    val currentColor = colors[(letterId - 1) % colors.size]
    val isCurrentlyPlaying = isPlaying && currentAudioId == "letter_${currentLetter.first}"
    
    // Animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
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
    
    // Record progress when viewing this lesson
    LaunchedEffect(letterId) {
        viewModel.recordLessonCompleted("letter", letterId)
    }
    
    fun speakLetter() {
        val audioUrl = letterAudioUrls[letterId]
        audioPlayerManager.playLetter(
            letter = currentLetter.first,
            exampleWord = currentLetter.second,
            audioUrl = audioUrl
        )
    }
    
    // Handle navigation with login check
    fun handleNavigateNext() {
        if (shouldShowLogin && !isLoggedIn) {
            showLoginDialog = true
        } else {
            onNavigateNext()
        }
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
            onDismiss = {
                showLoginDialog = false
            }
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(currentColor.copy(alpha = 0.1f), BackgroundLight)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        "Huruf ${currentLetter.first}",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                
                // Big Letter Display
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(32.dp))
                        .background(currentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentLetter.first,
                            fontSize = 120.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Lowercase
                Text(
                    text = currentLetter.first.lowercase(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Medium,
                    color = currentColor
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sound Button
                Button(
                    onClick = { speakLetter() },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = currentColor)
                ) {
                    Icon(
                        Icons.Default.VolumeUp,
                        contentDescription = "Dengar Suara",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dengar Suara", style = MaterialTheme.typography.labelLarge)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Example Word Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Contoh Kata:",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${currentLetter.first} untuk ${currentLetter.second}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Word with highlighted first letter
                        Row {
                            Text(
                                text = currentLetter.second.first().toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = currentColor,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentLetter.second.drop(1),
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Previous Button
                    if (letterId > 1) {
                        OutlinedButton(
                            onClick = onNavigatePrevious,
                            modifier = Modifier.height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sebelumnya")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    
                    // Next Button
                    if (letterId < 26) {
                        Button(
                            onClick = { handleNavigateNext() },
                            modifier = Modifier.height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = currentColor)
                        ) {
                            Text("Lanjut")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                        }
                    }
                }
            }
        }
    }
}
