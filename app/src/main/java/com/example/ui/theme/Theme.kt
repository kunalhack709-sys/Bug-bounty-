package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = TerminalBlack,
    primaryContainer = MatrixGreen,
    onPrimaryContainer = NeonGreen,
    secondary = CyberCyan,
    onSecondary = TerminalBlack,
    tertiary = WarningOrange,
    background = TerminalBlack,
    onBackground = TextPrimary,
    surface = TerminalDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = TerminalCardSurface,
    onSurfaceVariant = TextSecondary,
    error = CriticalRed,
    onError = TextPrimary
)

@Composable
fun BugBountyTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
