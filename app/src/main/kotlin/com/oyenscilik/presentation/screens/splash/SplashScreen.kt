package com.oyenscilik.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyenscilik.presentation.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit
) {
    // Bouncing animation for mascot
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    // Scale in animation
    var isVisible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale_in"
    )

    LaunchedEffect(Unit) {
        isVisible = true
        delay(2500)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F0),
                        Color(0xFFFFE8D6),
                        Color(0xFFFFF0E5)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        ) {
            // Mascot with bounce animation
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .offset(y = bounce.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
                    .shadow(16.dp, CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                KidOrange.copy(alpha = 0.9f),
                                KidOrangeDark
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Cute cat emoji as placeholder
                Text(
                    text = "🐱",
                    fontSize = 80.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name with styled text
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = KidOrange,
                            fontWeight = FontWeight.Black,
                            fontSize = 42.sp
                        )
                    ) {
                        append("Oyens")
                    }
                    append(" ")
                    withStyle(
                        SpanStyle(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp
                        )
                    ) {
                        append("Cilik")
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(KidOrange.copy(0.2f), KidPink.copy(0.2f))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "✨ Belajar Sambil Bermain ✨",
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Loading indicator at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            val dots by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(900, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "loading_dots"
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = if (dots.toInt() == index) KidOrange else KidOrange.copy(0.3f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
