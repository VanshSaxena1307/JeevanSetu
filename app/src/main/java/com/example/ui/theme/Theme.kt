package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = JeevanBrandGreen,
    onPrimary = Color.White,
    primaryContainer = JeevanGreenBg,
    onPrimaryContainer = JeevanBrandGreen,
    secondary = JeevanWaterBlue,
    onSecondary = Color.Black,
    secondaryContainer = JeevanCard,
    onSecondaryContainer = Color.White,
    tertiary = JeevanBatteryAmber,
    background = JeevanBg,
    onBackground = Color(0xFFF8FAFC),
    surface = JeevanSurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = JeevanCard,
    onSurfaceVariant = JeevanTextMuted,
    error = JeevanRedBorder,
    onError = Color.White
)

private val LightColorScheme = DarkColorScheme // Emergency apps maintain tactical dark battery-saving canvas

@Composable
fun JeevanSetuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // For disaster situations, we prefer high contrast dark canvas to maximize battery saving and night usability
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

