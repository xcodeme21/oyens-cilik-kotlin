package com.oyenscilik.presentation.screens.letters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oyenscilik.domain.model.Letter
import com.oyenscilik.presentation.theme.*

@Composable
fun LettersScreen(
    onNavigateToLetter: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: LettersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F0),
                        Color(0xFFFFFAF5)
                    )
                )
            )
    ) {
        // Compact header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title dengan icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🔤",
                    fontSize = 24.sp
                )
                Column {
                    Text(
                        text = "Belajar ABC",
                        fontSize = 20.sp,
                        color = KidOrange,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "26 Huruf",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = KidOrange)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(uiState.letters) { index, letter ->
                    var isVisible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay((index * 40).toLong())
                        isVisible = true
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ) + fadeIn()
                    ) {
                        LetterCard(
                            letter = letter,
                            onClick = { onNavigateToLetter(letter.id) },
                            colorIndex = index % 8
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LetterCard(
    letter: Letter,
    onClick: () -> Unit,
    colorIndex: Int
) {
    // More color variety
    val gradientColors = remember(colorIndex) {
        when (colorIndex) {
            0 -> listOf(KidOrange, KidYellow)
            1 -> listOf(KidPurple, KidPink)
            2 -> listOf(KidGreen, KidCyan)
            3 -> listOf(KidBlue, KidSkyBlue)
            4 -> listOf(KidPink, KidCoral)
            5 -> listOf(KidYellow, KidOrange)
            6 -> listOf(KidCyan, KidGreen)
            else -> listOf(KidSkyBlue, KidBlue)
        }
    }

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_press"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(gradientColors),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        // Decorative circle background
        Box(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    CircleShape
                )
        )
        
        Text(
            text = letter.letter,
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}
