package com.jinlite.workers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NavyPrimary = Color(0xFF1565C0)
val NavyDark = Color(0xFF0D47A1)
val AmberAccent = Color(0xFFFF8F00)
val SurfaceLight = Color(0xFFFFFFFF)
val BackgroundLight = Color(0xFFF4F6F9)
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF616161)
val RedUrgent = Color(0xFFD32F2F)
val GreenActive = Color(0xFF2E7D32)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    secondary = AmberAccent,
    onSecondary = Color.White,
    background = BackgroundLight,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    error = RedUrgent
)

@Composable
fun JinliteWorkersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
