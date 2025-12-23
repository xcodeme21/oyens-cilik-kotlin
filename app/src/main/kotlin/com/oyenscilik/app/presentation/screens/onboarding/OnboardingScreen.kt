package com.oyenscilik.app.presentation.screens.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oyenscilik.app.presentation.theme.*
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000),
        label = "alpha"
    )
    
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(800),
        label = "scale"
    )
    
    LaunchedEffect(Unit) {
        delay(100)
        startAnimation = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundCream,
                        BackgroundLight
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Mascot/Illustration Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .scale(scaleAnim)
                    .alpha(alphaAnim),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for mascot - in real app, use actual image
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ABC Characters placeholder
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LetterBubble("A", Purple)
                        LetterBubble("B", Orange)
                        LetterBubble("C", GreenSuccess)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Cat emoji placeholder
                    Text(
                        text = "🐱",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                        )
                    )
                }
            }
            
            // Welcome Text
            Column(
                modifier = Modifier.alpha(alphaAnim),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Halo! Selamat datang\ndi Oyens Cilik!",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Aplikasi belajar dengan eksperimen bermain untuk usia 2-6 tahun yang dilengkapi dengan karakter kucing favorit dan permainan menarik.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Buttons
            Column(
                modifier = Modifier.alpha(alphaAnim),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple
                    )
                ) {
                    Text(
                        text = "Daftar Sekarang",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Divider with OR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(TextLight)
                    )
                    Text(
                        text = "  atau  ",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextLight
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(TextLight)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Purple
                    )
                ) {
                    Text(
                        text = "Sudah punya akun? Masuk",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LetterBubble(letter: String, color: Color) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(color, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
    }
}
