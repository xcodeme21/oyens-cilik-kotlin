package com.oyenscilik.app.presentation.screens.letters

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.app.presentation.theme.*

data class LetterItem(
    val id: Int,
    val letter: String,
    val example: String,
    val emoji: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LettersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLetter: (Int) -> Unit
) {
    val letters = remember {
        val colors = listOf(
            Color(0xFFFF6B6B), Color(0xFFFFE66D), Color(0xFF4ECDC4), 
            Color(0xFF45B7D1), Color(0xFFFF8C42), Color(0xFFA855F7),
            Color(0xFF6BCB77), Color(0xFFFF6B9D), Color(0xFF4D96FF)
        )
        val examples = mapOf(
            "A" to Pair("Apel", "🍎"), "B" to Pair("Bola", "⚽"), 
            "C" to Pair("Cacing", "🪱"), "D" to Pair("Domba", "🐑"),
            "E" to Pair("Elang", "🦅"), "F" to Pair("Foto", "📷"), 
            "G" to Pair("Gajah", "🐘"), "H" to Pair("Harimau", "🐯"),
            "I" to Pair("Ikan", "🐟"), "J" to Pair("Jeruk", "🍊"), 
            "K" to Pair("Kucing", "🐱"), "L" to Pair("Lebah", "🐝"),
            "M" to Pair("Mangga", "🥭"), "N" to Pair("Nanas", "🍍"), 
            "O" to Pair("Orang", "👤"), "P" to Pair("Pisang", "🍌"),
            "Q" to Pair("Quran", "📖"), "R" to Pair("Rusa", "🦌"), 
            "S" to Pair("Singa", "🦁"), "T" to Pair("Tikus", "🐭"),
            "U" to Pair("Ular", "🐍"), "V" to Pair("Vas", "🏺"), 
            "W" to Pair("Wortel", "🥕"), "X" to Pair("Xilofon", "🎵"),
            "Y" to Pair("Yoyo", "🪀"), "Z" to Pair("Zebra", "🦓")
        )
        ('A'..'Z').mapIndexed { index, char ->
            val data = examples[char.toString()] ?: Pair("", "")
            LetterItem(
                id = index + 1,
                letter = char.toString(),
                example = data.first,
                emoji = data.second,
                color = colors[index % colors.size]
            )
        }
    }
    
    // Background animation
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val sparkle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "sparkle"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8E1),
                        Color(0xFFFFECB3),
                        Color(0xFFFFF3E0)
                    )
                )
            )
    ) {
        // Floating decorations
        Text("✨", fontSize = 20.sp, modifier = Modifier.offset(30.dp, 120.dp).rotate(sparkle))
        Text("🌟", fontSize = 24.sp, modifier = Modifier.offset(320.dp, 200.dp).rotate(-sparkle * 0.5f))
        Text("⭐", fontSize = 18.sp, modifier = Modifier.offset(280.dp, 100.dp).rotate(sparkle * 0.7f))
        
        Column(modifier = Modifier.fillMaxSize()) {
            // Fun Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Purple, Orange).map { it.copy(alpha = 0.1f) }
                        )
                    )
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Purple
                            )
                        }
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Belajar Huruf",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    brush = Brush.horizontalGradient(listOf(Purple, Orange))
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("🔤", fontSize = 28.sp)
                        }
                        Text(
                            text = "Ketuk huruf untuk belajar! 👆",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Letters Grid with staggered animation
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(letters) { index, letter ->
                    FunLetterCard(
                        letter = letter,
                        index = index,
                        onClick = { onNavigateToLetter(letter.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FunLetterCard(
    letter: LetterItem,
    index: Int,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    // Continuous wiggle animation - different for each card
    val infiniteTransition = rememberInfiniteTransition(label = "card_$index")
    val wiggle by infiniteTransition.animateFloat(
        initialValue = -3f, targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(400 + (index % 5) * 50, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(index * 30)
        ),
        label = "wiggle"
    )
    
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(600 + (index % 3) * 100, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(index * 50)
        ),
        label = "bounce"
    )
    
    Card(
        modifier = Modifier
            .aspectRatio(0.85f)
            .scale(scale)
            .rotate(wiggle * 0.5f)
            .offset(y = (-bounce).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = letter.color),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Shine effect
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight()
                    .offset(x = (-10).dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Letter
                Text(
                    text = letter.letter,
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
                
                // Emoji
                Text(
                    text = letter.emoji,
                    fontSize = 24.sp,
                    modifier = Modifier.offset(y = bounce.dp * 0.3f)
                )
                
                // Example word
                Text(
                    text = letter.example,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}
