package com.app.core_ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

fun Modifier.shimmerEffect(shape : Shape = RectangleShape): Modifier = composed {

    val transition = rememberInfiniteTransition(
        label = "shimmer"
    )

    val translateAnimation by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    background(
        shape = shape,
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation, 0f),
            end = Offset(translateAnimation + 500f, 0f)
        )
    )
}