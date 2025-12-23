package com.oyenscilik.app.presentation.screens.numbers

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
fun NumberDetailScreen(
    numberId: Int,
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
    val numberAudioUrls = remember {
        mapOf<Int, String?>(
            // Will be populated from backend
            // Example: 1 to "numbers/1.mp3"
        )
    }
    
    val numbers = remember {
        listOf(
            "Nol", "Satu", "Dua", "Tiga", "Empat", "Lima",
            "Enam", "Tujuh", "Delapan", "Sembilan", "Sepuluh",
            "Sebelas", "Dua Belas", "Tiga Belas", "Empat Belas", "Lima Belas",
            "Enam Belas", "Tujuh Belas", "Delapan Belas", "Sembilan Belas", "Dua Puluh"
        )
    }
    
    val currentWord = numbers.getOrNull(numberId) ?: "Nol"
    val colors = listOf(Orange, Purple, GreenSuccess, BlueInfo, Yellow)
    val currentColor = colors[numberId % colors.size]
    val isCurrentlyPlaying = isPlaying && currentAudioId == "number_$numberId"
    
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
    LaunchedEffect(numberId) {
        viewModel.recordLessonCompleted("number", numberId)
    }
    
    fun speakNumber() {
        val audioUrl = numberAudioUrls[numberId]
        audioPlayerManager.playNumber(
            number = numberId,
            word = currentWord,
            audioUrl = audioUrl
        )
    }
    
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
            onDismiss = { showLoginDialog = false }
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
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        "Angka $numberId",
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
                
                // Big Number Display
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(32.dp))
                        .background(currentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = numberId.toString(),
                        fontSize = if (numberId >= 10) 80.sp else 120.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Number Word
                Text(
                    text = currentWord,
                    style = MaterialTheme.typography.displaySmall,
                    color = currentColor,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sound Button with pulse animation
                Button(
                    onClick = { speakNumber() },
                    modifier = Modifier
                        .height(56.dp)
                        .scale(soundButtonScale),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentlyPlaying) currentColor.copy(alpha = 0.8f) else currentColor
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
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Visual representation with emoji
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
                            text = "Mari Hitung:",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "⭐".repeat(minOf(numberId, 10)) + 
                                   if (numberId > 10) " +${numberId - 10}" else "",
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (numberId > 0) {
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
                    
                    if (numberId < 20) {
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
