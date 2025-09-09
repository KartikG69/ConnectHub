package com.connecthub.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

// Primary Colors
val CreamWhite = Color(0xFFFAF9F6)
val ForestGreen = Color(0xFF2D5F3F)
val ForestGreenLight = Color(0xFF3A7A4F)
val Charcoal = Color(0xFF1A1A1A)
val CharcoalLight = Color(0xFF2A2A2A)

// Basic Colors
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Gray = Color(0xFF999999)
val LightGray = Color(0xFFE0E0E0)
val BackgroundGray = Color(0xFFF5F5F5)

// Status Colors
val Error = Color(0xFFBA1A1A)
val Success = Color(0xFF4CAF50)
val Warning = Color(0xFFF57C00)
val Info = Color(0xFF1976D2)

// Social Media Colors
val FacebookBlue = Color(0xFF1877F2)
val GoogleGray = Color(0xFF4285F4)

// Gradients
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(ForestGreen, ForestGreenLight),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

val HeroGradient = Brush.verticalGradient(
    colors = listOf(
        ForestGreen.copy(alpha = 0.1f),
        Color.Transparent
    )
)