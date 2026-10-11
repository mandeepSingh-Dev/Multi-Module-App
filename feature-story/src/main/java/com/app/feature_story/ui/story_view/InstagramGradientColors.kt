package com.app.feature_story.ui.story_view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Official Instagram Gradient Colors
val InstagramGradientColors = listOf(
    Color(0xFF833AB4), // Purple
    Color(0xFFFD1D1D), // Red
    Color(0xFFF56040), // Orange
    Color(0xFFFFDC80), // Yellow
    Color(0xFF833AB4)  // Loop back for smooth sweep
)

@Composable
@Preview
fun SolidCircularInstagramLoader(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    storyCount: Int = 4,
    segmentGapAngle: Float = 10f,
    useStroke: Boolean = false,
    strokeWidth: Dp = 4.dp
) {
    // 1. Continuous Rotation Animation (Faster for a loading feel)
    val infiniteTransition = rememberInfiniteTransition(label = "SolidLoaderTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SolidLoaderRotation"
    )

    // Calculate arc angle per segment based on total count and gaps
    val totalGapsAngle = storyCount * segmentGapAngle
    val availableAngle = 360f - totalGapsAngle
    val segmentSweepAngle = if (storyCount > 0) availableAngle / storyCount else 360f

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = this.size

            // Brush for the overall circle (solid gradient fill)
            val instagramBrush = Brush.sweepGradient(colors = InstagramGradientColors)

            // 2. Rotate the entire drawn section smoothly
            rotate(degrees = rotation) {
                for (i in 0 until storyCount) {
                    val startAngle = i * (segmentSweepAngle + segmentGapAngle)

                    if (useStroke) {
                        // Draw thin arcs (segmented ring)
                        val strokeWidthPx = strokeWidth.toPx()
                        val arcSize = canvasSize.width - strokeWidthPx
                        drawArc(
                            brush = instagramBrush,
                            startAngle = startAngle,
                            sweepAngle = segmentSweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidthPx),
                            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                            size = Size(arcSize, arcSize)
                        )
                    } else {
                        // Draw thick segments filling the circle
                        drawArc(
                            brush = instagramBrush,
                            startAngle = startAngle,
                            sweepAngle = segmentSweepAngle,
                            useCenter = true, // Key: fills to the center
                            topLeft = Offset.Zero,
                            size = canvasSize
                        )
                    }
                }
            }
        }
    }
}