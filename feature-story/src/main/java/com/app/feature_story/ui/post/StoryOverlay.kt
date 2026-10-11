package com.app.feature_story.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storydemo.Overlay

/**
 * Renders a list of overlays (emoji / text) on top of story content.
 * Call this LAST inside the story's Box so overlays draw above the
 * media. Must be called from a BoxScope so `.align(...)` works.
 *
 * Purely visual — it doesn't consume touches, so your existing
 * tap-to-navigate / long-press-to-pause gesture handling on the
 * parent Box keeps working even where an overlay sits.
 */
@Composable
fun BoxScope.StoryOverlaysLayer(overlays: List<Overlay>) {
    overlays.forEachIndexed { index, overlay ->
        key(index, overlay.value, overlay.x, overlay.y) {
            val alignment = overlayBiasAlignment(overlay.x, overlay.y)
            Box(
                modifier = Modifier
                    .align(alignment)
                    .graphicsLayer {
                        scaleX = overlay.scale.toFloat()
                        scaleY = overlay.scale.toFloat()
                        rotationZ = overlay.rotation.toFloat()
                    }
            ) {
                OverlayVisual(overlay)
            }
        }
    }
}

/**
 * Just the visual (emoji text / text bubble) — no positioning, no
 * gestures. Shared by the read-only story viewer above and the
 * drag/scale/rotate-enabled upload editor (see StoryUploadScreen.kt).
 */
@Composable
internal fun OverlayVisual(overlay: Overlay) {
    when (overlay.type) {
        "emoji" -> Text(text = overlay.value, fontSize = 42.sp)
        "text" -> {
            val textColor = overlay.color.toComposeColorOrDefault(Color.White)
            val bgColor = overlay.backgroundColor?.toComposeColorOrNull()
            Box(
                modifier = if (bgColor != null) {
                    Modifier
                        .background(bgColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                } else {
                    Modifier
                }
            ) {
                Text(
                    text = overlay.value,
                    color = textColor,
                    fontSize = (overlay.fontSize ?: 18.0).sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        else -> Unit // unknown type -> render nothing rather than crash
    }
}

internal fun String?.toComposeColorOrNull(): Color? =
    this?.let { hex ->
        runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrNull()
    }

internal fun String?.toComposeColorOrDefault(default: Color): Color =
    this.toComposeColorOrNull() ?: default

/** Shared alignment mapping: normalized 0..1 center -> Compose bias -1..1. */
internal fun overlayBiasAlignment(x: Double, y: Double): BiasAlignment = BiasAlignment(
    horizontalBias = ((x * 2 - 1).toFloat()).coerceIn(-1f, 1f),
    verticalBias = ((y * 2 - 1).toFloat()).coerceIn(-1f, 1f)
)
