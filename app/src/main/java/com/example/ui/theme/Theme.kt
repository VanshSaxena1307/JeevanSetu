package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MintBreezeColorScheme = lightColorScheme(
    primary = MintDeep,
    onPrimary = Color.White,
    primaryContainer = MintLight,
    onPrimaryContainer = MintDeep,
    secondary = MintPrimary,
    onSecondary = Color.White,
    secondaryContainer = MintVeryLight,
    onSecondaryContainer = MintDeep,
    tertiary = StatusWarning,
    onTertiary = Color.White,
    tertiaryContainer = StatusWarningBg,
    onTertiaryContainer = StatusWarning,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSubtle,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderLight,
    error = StatusDanger,
    onError = Color.White,
    errorContainer = StatusDangerBg,
    onErrorContainer = StatusCritical
)

@Composable
fun JeevanSetuTheme(
    darkTheme: Boolean = false, // Strictly enforce Mint Breeze Light theme as active default
    content: @Composable () -> Unit,
) {
    // Mint Breeze is a dedicated, calm, trustworthy light emergency preparedness design system
    MaterialTheme(
        colorScheme = MintBreezeColorScheme,
        typography = Typography,
        content = content
    )
}
