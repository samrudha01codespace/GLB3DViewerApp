package com.samrudha.glb3dviewerapp.Design

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp


@Composable
fun DarkTheme_text(): Color {
    return if (isDarkMode()) {
        Color.White
    } else {
        Color.Black
    }
}

@Composable
fun isDarkMode(): Boolean {
    return isSystemInDarkTheme()
}

@Composable
fun DarkTheme(): Color {
    return if (isDarkMode()) {
        Color.Black
    } else {
        Color.White
    }
}
@SuppressLint("RestrictedApi")
@Composable
fun Modifier.shimmer(): Modifier {
    val transition = rememberInfiniteTransition()
    val translateX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    return this.then(
        Modifier.drawWithCache {
            val gradientWidth = size.width * 0.4f
            // compute start based on animated fraction
            val startX = lerp(-gradientWidth, size.width + gradientWidth, translateX)
            val colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.14f),
                Color.Transparent
            )
            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset(startX - gradientWidth, 0f),
                end = Offset(startX, size.height)
            )
            onDrawWithContent {
                drawContent()
                drawRect(brush = brush, blendMode = BlendMode.SrcOver)
            }
        }
    )
}
