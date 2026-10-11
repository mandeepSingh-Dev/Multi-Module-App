package com.app.feature_story.ui.post2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storydemo.Overlay
import kotlin.math.roundToInt

/**
 * Read-only overlays for the story VIEWER. Call it inside the story's
 * Box, after the media. It never consumes touches, so tap-left/right and
 * long-press-to-pause on the parent keep working.
 */
@Composable
fun StoryOverlaysLayer(overlays: List<Overlay>, modifier: Modifier = Modifier) {
   /* OverlayLayout(overlays = overlays, modifier = modifier) { overlay ->
        Box(
            Modifier.graphicsLayer {
                scaleX = overlay.scale.toFloat()
                scaleY = overlay.scale.toFloat()
                rotationZ = overlay.rotation.toFloat()
            }
        ) {
            OverlayVisual(overlay)
        }
    }*/
}

/**
 * Full-size layer that places every overlay so that its CENTER sits at
 * (x * width, y * height) — the exact meaning of the normalized x/y in
 * the API. Shared by the viewer (read-only) and the upload editor
 * (draggable), so both always agree on where an overlay lives.
 */
@Composable
internal fun OverlayLayout(
    overlays: List<Overlay>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (Overlay) -> Unit
)
{
    Layout(
        content = {
            overlays.forEachIndexed { index, overlay ->
                key(index) {
                    Box(Modifier.layoutId(index)) { itemContent(overlay) }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val placed = measurables.map { it to it.measure(loose) }

        layout(width, height) {
            placed.forEach { (measurable, placeable) ->
                val overlay = overlays.getOrNull(measurable.layoutId as? Int ?: return@forEach)
                    ?: return@forEach
                placeable.place(
                    x = (overlay.x * width - placeable.width / 2.0).roundToInt(),
                    y = (overlay.y * height - placeable.height / 2.0).roundToInt()
                )
            }
        }
    }
}

/** Just the visual (emoji / text bubble): no position, no gestures. */
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
        else -> Unit // unknown type -> render nothing instead of crashing
    }
}

internal fun String?.toComposeColorOrNull(): Color? =
    this?.let { hex -> runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrNull() }

internal fun String?.toComposeColorOrDefault(default: Color): Color =
    this.toComposeColorOrNull() ?: default
