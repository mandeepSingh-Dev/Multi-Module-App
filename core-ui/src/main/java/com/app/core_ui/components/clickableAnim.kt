package com.app.core_ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.theme_color_1
import com.zodiaq.ui.theme.Shape_20


@Composable
fun Modifier.clickableAnim(
    animDuration : Int = 200,
    onClick: () -> Unit) : Modifier {

    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        animationSpec = tween(
            durationMillis = animDuration
        ),
        label = "Animated_Click"
    )
    val dropShadowAlpha by animateFloatAsState(targetValue = if (isPressed) 1f else 0f, label = "")


    val translationY by animateFloatAsState(
        targetValue = if (isPressed) 20f else 0f,
        animationSpec = tween(
            durationMillis = animDuration
        ),
        label = "Animated_Click"
    )

    return this.pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
            }

            MotionEvent.ACTION_UP -> {
                isPressed = false
                onClick()
            }

            else -> {
                isPressed = false
            }
        }
        true
    }.graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.translationY = translationY
    }.dropShadow(shape = Shape_20, shadow = Shadow(radius = 20.dp, color = theme_color_1.copy(alpha = dropShadowAlpha)))



}
