package com.oyenscilik.presentation.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.oyenscilik.presentation.theme.*
import kotlin.random.Random

fun Modifier.kidBackgroundPattern(): Modifier = this.composed {
    background(
        brush = Brush.verticalGradient(
            colors = listOf(WarmCream, LavenderBlush, LightCyan)
        )
    ).drawBehind {
        val colors = listOf(
            KidOrange.copy(alpha = 0.1f),
            KidPurple.copy(alpha = 0.1f),
            KidGreen.copy(alpha = 0.1f),
            KidBlue.copy(alpha = 0.1f)
        )

        repeat(12) {
            drawCircle(
                color = colors.random(),
                radius = Random.nextFloat() * 80f + 30f,
                center = Offset(
                    x = Random.nextFloat() * size.width,
                    y = Random.nextFloat() * size.height
                )
            )
        }
    }
}
