package com.oyenscilik.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.presentation.theme.TextPrimary

@Composable
fun OyensMascot(
    modifier: Modifier = Modifier,
    isCelebrating: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_idle")
    
    // Idle bounce animation
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Celebration rotation
    val celebrationRotation by animateFloatAsState(
        targetValue = if (isCelebrating) 360f else 0f,
        animationSpec = tween(500, easing = LinearEasing),
        label = "celebration_rotation"
    )

    // Celebration scale
    val celebrationScale by animateFloatAsState(
        targetValue = if (isCelebrating) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "celebration_scale"
    )

    Text(
        text = "🐱",
        fontSize = 64.sp,
        modifier = modifier
            .size(80.dp)
            .graphicsLayer {
                translationY = if (!isCelebrating) bounce else 0f
                rotationZ = celebrationRotation
                scaleX = celebrationScale
                scaleY = celebrationScale
            }
    )
}

@Composable
fun SpeechBubble(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = message,
                fontSize = 14.sp,
                color = TextPrimary
            )
        }
    }
}
