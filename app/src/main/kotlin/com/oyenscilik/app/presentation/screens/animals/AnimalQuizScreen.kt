package com.oyenscilik.app.presentation.screens.animals

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.app.presentation.theme.*
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

data class QuizAnimal(
    val name: String,
    val emoji: String,
    val hint: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalQuizScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    val animals = remember {
        listOf(
            QuizAnimal("KUCING", "🐱", "Hewan lucu berbulu"),
            QuizAnimal("ANJING", "🐕", "Sahabat manusia"),
            QuizAnimal("GAJAH", "🐘", "Hewan besar dengan belalai"),
            QuizAnimal("SINGA", "🦁", "Raja hutan"),
            QuizAnimal("HARIMAU", "🐯", "Kucing belang besar"),
            QuizAnimal("ZEBRA", "🦓", "Kuda belang hitam putih"),
            QuizAnimal("KELINCI", "🐰", "Telinga panjang, suka wortel"),
            QuizAnimal("KURA", "🐢", "Bawa rumah di punggung"),
            QuizAnimal("MONYET", "🐵", "Suka pisang, pandai memanjat")
        )
    }
    
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedLetters by remember { mutableStateOf(listOf<Int>()) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var showCelebration by remember { mutableStateOf(false) }
    
    val currentAnimal = animals[currentQuestionIndex]
    val letterSlots = remember(currentQuestionIndex) {
        // Create letter slots with some hidden
        val name = currentAnimal.name
        val hiddenIndices = name.indices.shuffled().take(name.length / 2 + 1).toSet()
        name.mapIndexed { index, char ->
            LetterSlot(char.toString(), isHidden = index in hiddenIndices)
        }
    }
    
    val availableLetters = remember(currentQuestionIndex) {
        // Create a pool of letters including correct ones and some random
        val correctLetters = letterSlots.filter { it.isHidden }.map { it.letter }
        val randomLetters = ('A'..'Z').map { it.toString() }.shuffled().take(9 - correctLetters.size)
        (correctLetters + randomLetters).shuffled()
    }
    
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("id", "ID")
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
    
    fun checkAnswer(): Boolean {
        val hiddenSlots = letterSlots.filter { it.isHidden }
        if (selectedLetters.size != hiddenSlots.size) return false
        
        var correctCount = 0
        var selectedIndex = 0
        letterSlots.forEach { slot ->
            if (slot.isHidden) {
                if (selectedIndex < selectedLetters.size) {
                    if (availableLetters[selectedLetters[selectedIndex]] == slot.letter) {
                        correctCount++
                    }
                    selectedIndex++
                }
            }
        }
        return correctCount == hiddenSlots.size
    }
    
    fun nextQuestion() {
        if (currentQuestionIndex < animals.size - 1) {
            currentQuestionIndex++
            selectedLetters = listOf()
            isCorrect = null
            showCelebration = false
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GreenLight, BackgroundLight)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Level ${currentQuestionIndex + 1}")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("⭐ $score", style = MaterialTheme.typography.labelLarge)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        tts?.speak(currentAnimal.name, TextToSpeech.QUEUE_FLUSH, null, null)
                    }) {
                        Icon(Icons.Default.VolumeUp, "Dengar", tint = Orange)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animal Emoji Card
                Card(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentAnimal.emoji, fontSize = 64.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Hint
                Text(
                    text = currentAnimal.hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Letter slots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var hiddenIndex = 0
                    letterSlots.forEach { slot ->
                        if (slot.isHidden) {
                            val letterIndex = selectedLetters.getOrNull(hiddenIndex)
                            val letter = letterIndex?.let { availableLetters[it] } ?: "_"
                            LetterSlotBox(
                                letter = letter,
                                isHidden = true,
                                isCorrect = isCorrect
                            )
                            hiddenIndex++
                        } else {
                            LetterSlotBox(
                                letter = slot.letter,
                                isHidden = false,
                                isCorrect = null
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Available Letters Grid
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (row in 0..2) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (col in 0..2) {
                                val index = row * 3 + col
                                if (index < availableLetters.size) {
                                    LetterButton(
                                        letter = availableLetters[index],
                                        isSelected = index in selectedLetters,
                                        onClick = {
                                            if (isCorrect == null) {
                                                selectedLetters = if (index in selectedLetters) {
                                                    selectedLetters - index
                                                } else {
                                                    selectedLetters + index
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Check / Next Button
                Button(
                    onClick = {
                        if (isCorrect == null) {
                            val correct = checkAnswer()
                            isCorrect = correct
                            if (correct) {
                                score += 10
                                showCelebration = true
                                tts?.speak("Benar! Hebat!", TextToSpeech.QUEUE_FLUSH, null, null)
                            } else {
                                tts?.speak("Coba lagi ya!", TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        } else if (isCorrect == true) {
                            nextQuestion()
                        } else {
                            selectedLetters = listOf()
                            isCorrect = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (isCorrect) {
                            true -> GreenSuccess
                            false -> RedError
                            null -> Purple
                        }
                    )
                ) {
                    Text(
                        text = when (isCorrect) {
                            true -> "Lanjut →"
                            false -> "Coba Lagi"
                            null -> "Cek Jawaban"
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        
        // Celebration overlay
        AnimatedVisibility(
            visible = showCelebration,
            enter = fadeIn() + scaleIn()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { showCelebration = false },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 80.sp)
                    Text(
                        "BENAR!",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "+10 ⭐",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Yellow
                    )
                }
            }
        }
    }
}

@Composable
private fun LetterSlotBox(
    letter: String,
    isHidden: Boolean,
    isCorrect: Boolean?
) {
    val backgroundColor = when {
        isCorrect == true && isHidden -> GreenSuccess
        isCorrect == false && isHidden -> RedError
        isHidden -> SurfaceWhite
        else -> Color.Transparent
    }
    
    val textColor = when {
        isCorrect != null && isHidden -> Color.White
        isHidden -> TextPrimary
        else -> TextPrimary
    }
    
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun LetterButton(
    letter: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(if (isSelected) GreenSuccess else SurfaceGray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TextPrimary
        )
    }
}

private data class LetterSlot(
    val letter: String,
    val isHidden: Boolean
)
