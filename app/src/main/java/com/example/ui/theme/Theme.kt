package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LumaDarkColorScheme = darkColorScheme(
    primary = CyanLuma,
    onPrimary = PitchBlack,
    primaryContainer = CardDarkSurface,
    onPrimaryContainer = TextSlateWhite,
    secondary = BlueLuma,
    onSecondary = PitchBlack,
    secondaryContainer = UserBubbleColor,
    onSecondaryContainer = TextSlateWhite,
    tertiary = PurpleLuma,
    background = PitchBlack,
    onBackground = TextSlateWhite,
    surface = DeepDarkBg,
    onSurface = TextSlateWhite,
    surfaceVariant = CardDarkSurface,
    onSurfaceVariant = TextMutedGray,
    outline = CardDarkBorder,
    error = Color(0xFFEF4444)
)

@Composable
fun LumaTheme(
    darkTheme: Boolean = true, // Always default to pitch dark theme as requested
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LumaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
