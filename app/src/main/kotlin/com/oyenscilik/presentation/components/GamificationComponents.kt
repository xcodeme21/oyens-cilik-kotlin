package com.oyenscilik.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

// ==================== STARS REWARD ANIMATION ====================

@Composable
fun StarsRewardAnimation(
    starsCount: Int,
    onAnimationEnd: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(true) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 1.2f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        isPlaying = false
        kotlinx.coroutines.delay(300)
        onAnimationEnd()
    }
    
    if (isPlaying || scale > 0.01f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(scale)
            ) {
                // Stars burst
                Row {
                    repeat(minOf(starsCount, 5)) {
                        Text("⭐", fontSize = 48.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "+$starsCount Bintang!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107)
                )
                
                Text(
                    "Hebat sekali! 🎉",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

// ==================== STREAK COUNTER WITH FIRE ====================

@Composable
fun StreakCounter(
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fire")
    
    val fireScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fireScale"
    )
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF6B35), Color(0xFFFF8C42))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            "🔥",
            fontSize = 24.sp,
            modifier = Modifier.scale(fireScale)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            "$streakDays hari",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ==================== LEVEL UP CELEBRATION ====================

@Composable
fun LevelUpCelebration(
    newLevel: Int,
    levelTitle: String,
    onDismiss: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "scale"
    )
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        isVisible = false
        kotlinx.coroutines.delay(300)
        onDismiss()
    }
    
    if (isVisible || scale > 0.01f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.scale(scale)
            ) {
                Text("🎊", fontSize = 72.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "LEVEL UP!",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFFFD700), Color(0xFFFF8C42))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$newLevel",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    levelTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Terus belajar ya! 💪",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// ==================== CONFETTI EFFECT ====================

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
    val velocityY: Float
)

@Composable
fun ConfettiEffect(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isPlaying) return
    
    val particles = remember {
        List(50) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * -0.5f,
                color = listOf(
                    Color(0xFFFF6B6B),
                    Color(0xFFFFD93D),
                    Color(0xFF6BCB77),
                    Color(0xFF4D96FF),
                    Color(0xFFFF8C42),
                    Color(0xFFBF6BFF)
                ).random(),
                size = Random.nextFloat() * 10 + 5,
                rotation = Random.nextFloat() * 360,
                velocityY = Random.nextFloat() * 3 + 2
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        ),
        label = "time"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val yPos = ((particle.y + time * particle.velocityY) % 1.5f) * size.height
            val xPos = particle.x * size.width
            
            rotate(particle.rotation + time * 180, pivot = Offset(xPos, yPos)) {
                drawRect(
                    color = particle.color,
                    topLeft = Offset(xPos - particle.size / 2, yPos - particle.size / 2),
                    size = androidx.compose.ui.geometry.Size(particle.size, particle.size)
                )
            }
        }
    }
}

// ==================== ANIMATED PROGRESS BAR ====================

@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
    backgroundColor: Color = Color(0xFFE0E0E0),
    progressColors: List<Color> = listOf(Color(0xFFFF8C42), Color(0xFFFFAA5C))
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "progress"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(
                    brush = Brush.horizontalGradient(progressColors),
                    shape = RoundedCornerShape(height / 2)
                )
        )
    }
}

// ==================== DAILY CHALLENGE CARD ====================

@Composable
fun DailyChallengeCard(
    title: String,
    description: String,
    progress: Int,
    target: Int,
    reward: Int,
    isCompleted: Boolean,
    onClaim: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isCompleted) Color(0xFFF0FFF0) else Color.White)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isCompleted) Color(0xFF4CAF50) else Color(0xFFFFF0E6),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isCompleted) "✓" else "🎯",
                    fontSize = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                
                Text(
                    description,
                    fontSize = 12.sp,
                    color = Color(0xFF636E72)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                AnimatedProgressBar(
                    progress = progress.toFloat() / target,
                    height = 6.dp,
                    progressColors = if (isCompleted) 
                        listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                    else 
                        listOf(Color(0xFFFF8C42), Color(0xFFFFAA5C))
                )
                
                Text(
                    "$progress / $target",
                    fontSize = 10.sp,
                    color = Color(0xFF636E72)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Reward
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⭐", fontSize = 20.sp)
                Text(
                    "+$reward",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107)
                )
            }
        }
    }
}
