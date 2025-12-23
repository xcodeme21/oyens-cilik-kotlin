package com.oyenscilik.app.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = TextOnPrimary,
    primaryContainer = PurpleSoft,
    onPrimaryContainer = PurpleDark,
    
    secondary = Orange,
    onSecondary = TextOnPrimary,
    secondaryContainer = OrangeSoft,
    onSecondaryContainer = OrangeDark,
    
    tertiary = Yellow,
    onTertiary = TextPrimary,
    tertiaryContainer = YellowSoft,
    onTertiaryContainer = TextPrimary,
    
    error = RedError,
    onError = TextOnPrimary,
    errorContainer = RedLight,
    onErrorContainer = RedError,
    
    background = BackgroundCream,
    onBackground = TextPrimary,
    
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    
    outline = TextLight,
    outlineVariant = SurfaceGray,
)

@Composable
fun OyensCilikTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
