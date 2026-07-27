package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldPrimaryLight,
    onPrimary = Color.White,
    secondary = TerracottaGoldLight,
    onSecondary = Color.White,
    tertiary = DeepSlate,
    background = DarkBackground,
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    secondary = TerracottaGold,
    onSecondary = Color.White,
    tertiary = DeepSlate,
    background = LightBackground,
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = DeepSlate,
    onSurface = DeepSlate,
    onSurfaceVariant = Color(0xFF64748B),
    outlineVariant = Color(0xFFE2E8F0)
  )

@Composable
fun BomaTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Disable dynamic colors to keep brand consistency
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  BomaTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}

