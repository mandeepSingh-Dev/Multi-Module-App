package com.app.core_ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.theme_color_2
import com.zodiaq.ui.theme.Shape_20


@Composable
inline fun Modifier.clickableDebAnim(
    debounceInterval : Long = 600L,
    animDuration :Int = 500,
    crossinline onClick : () -> Unit
): Modifier {


    var isPressed by remember { mutableStateOf(false) }

    val scaleAnim by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f, label = "")
    val dropShadowAlpha by animateFloatAsState(targetValue = if (isPressed) 1f else 0f, label = "")
    val translationYAnim by animateFloatAsState(
        targetValue = if (isPressed) 20f else 0f,
        animationSpec = tween(
            durationMillis = animDuration
        ),
        label = "Animated_Click"
    )
    var lastTime by remember { mutableLongStateOf(0L) }


    return pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
            }

            MotionEvent.ACTION_UP -> {
                isPressed = false

                val currentTime = System.currentTimeMillis()
                val difference = currentTime - lastTime
                if (difference >= debounceInterval) {
                    lastTime = currentTime
                    onClick()
                }
            }

            else -> {
                isPressed = false
            }
        }
        true
    }
        .graphicsLayer {
        scaleX = scaleAnim
        scaleY = scaleAnim
        translationY = translationYAnim
    }.dropShadow(shape = Shape_20, shadow = Shadow(radius = 20.dp, color = theme_color_2.copy(alpha = dropShadowAlpha)))

}



@Composable
inline fun Modifier.clickableDeb(
    debounceInterval : Long = 600L,
    crossinline onClick : () -> Unit
): Modifier {


    var isPressed by remember { mutableStateOf(false) }

    var lastTime by remember { mutableLongStateOf(0L) }

    return pointerInteropFilter {
        when (it.action) {
            MotionEvent.ACTION_DOWN -> {}

            MotionEvent.ACTION_UP -> {
                val currentTime = System.currentTimeMillis()
                val difference = currentTime - lastTime
                if (difference >= debounceInterval) {
                    lastTime = currentTime
                    onClick()
                }
            }

            else -> {}
        }
        true
    }
}


