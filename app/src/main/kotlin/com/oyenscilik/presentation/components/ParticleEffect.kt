package com.oyenscilik.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float
)

@Composable
fun ParticleEffect(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    particleColors: List<Color> = listOf(
        Color(0xFFFFC107), // Yellow
        Color(0xFFFF9500), // Orange
        Color(0xFFE91E63), // Pink
        Color(0xFF9B59D6), // Purple
        Color(0xFF2ECC71)  // Green
    )
) {
    val particles = remember { mutableStateListOf<Particle>() }
    var frameCount by remember { mutableStateOf(0) }

    LaunchedEffect(isActive) {
        if (isActive) {
            // Generate particles
            repeat(30) {
                particles.add(
                    Particle(
                        x = Random.nextFloat() * 1000f,
                        y = Random.nextFloat() * 1000f,
                        vx = (Random.nextFloat() - 0.5f) * 10f,
                        vy = (Random.nextFloat() - 0.5f) * 10f - 5f,
                        color = particleColors.random(),
                        size = Random.nextFloat() * 10f + 5f
                    )
                )
            }
        }
    }

    LaunchedEffect(frameCount) {
        if (isActive && particles.isNotEmpty()) {
            kotlinx.coroutines.delay(16) // ~60fps
            particles.forEach { particle ->
                particle.x += particle.vx
                particle.y += particle.vy
                particle.vy += 0.3f // Gravity
            }
            particles.removeAll { it.y > 2000f }
            frameCount++
            
            if (particles.isEmpty()) {
                frameCount = 0
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawCircle(
                color = particle.color,
                radius = particle.size,
                center = Offset(particle.x, particle.y)
            )
        }
    }
}

@Composable
fun StarsDisplay(
    stars: Int,
    maxStars: Int = 3,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0f) }
    
    LaunchedEffect(stars) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(maxStars) { index ->
            Text(
                text = if (index < stars) "⭐" else "☆",
                fontSize = 32.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = if (index < stars) scale.value else 1f
                    scaleY = if (index < stars) scale.value else 1f
                }
            )
        }
    }
}
