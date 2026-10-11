package com.app.feature_story.ui.story_view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.app.core_story.remote.model.response.StoriesListResponse
import kotlin.collections.forEachIndexed

/**
 * Renders a list of overlays (emoji / text) on top of story content.
 * Call this LAST inside the story's Box so overlays draw above the
 * media. Must be called from a BoxScope so `.align(...)` works.
 *
 * Purely visual — it doesn't consume touches, so your existing
 * tap-to-navigate / long-press-to-pause gesture handling on the
 * parent Box keeps working even where an overlay sits.
 */
/*@Composable
fun BoxScope.StoryOverlaysLayer(overlays: List<StoriesListResponse.UserStoryGroup.StoryOverlay?>) {


    val widthpx = LocalResources.current.displayMetrics?.widthPixels
    val heightPx = LocalResources.current.displayMetrics?.heightPixels

    Log.d("dvkdnvkdv", overlays.toString())
    overlays.forEachIndexed { index, overlay ->
     Log.d("lfjbvkfnkvbf", index.toString())
        overlay?.let { overlay ->
            key(index, overlay.value, overlay.x, overlay.y) {
                val alignment = BiasAlignment(
                    horizontalBias = (((overlay.x ?: 0.0) * 2 - 1).toFloat()).coerceIn(-1f, 1f),
                    verticalBias = (((overlay.y ?: 0.0) * 2 - 1).toFloat()).coerceIn(-1f, 1f)
                )
                Box(
                    modifier = Modifier.zIndex(4f)
                        .offset{
                            IntOffset(x = (overlay.x ?: 0).toInt().coerceAtMost(widthpx ?: 0), y = (overlay.y ?: 0).toInt().coerceAtMost(heightPx?:0))
                        }
//                        .align(alignment)
                        .graphicsLayer {
                            scaleX = overlay.scale?.toFloat() ?: 0f
                            scaleY = overlay.scale?.toFloat() ?: 0f
                            rotationZ = overlay.rotation?.toFloat() ?: 0f
                        }
                ) {
                    when (overlay.type) {
                        "emoji" -> EmojiOverlay(overlay)
                        "text" -> TextOverlay(overlay)
                        else -> Unit // unknown type -> render nothing rather than crash
                    }
                }
            }
        }
    }
}*/

@Composable
fun BoxScope.StoryOverlaysLayer(
    overlays: List<StoriesListResponse.UserStoryGroup.StoryOverlay?>
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val density = LocalDensity.current

        val containerWidth = constraints.maxWidth
        val containerHeight = constraints.maxHeight

        overlays.forEachIndexed { index, overlay ->

            overlay?.let { item ->

                key(
                    index,
                    item.value,
                    item.x,
                    item.y
                ) {

                    var overlaySize by remember(item.value) {
                        mutableStateOf(IntSize.Zero)
                    }

                    val x = (
                            (item.x ?: 0.5) * containerWidth
                            ).toFloat()

                    val y = (
                            (item.y ?: 0.5) * containerHeight
                            ).toFloat()

                    Box(
                        modifier = Modifier
                            .zIndex(4f)

                            // Center the overlay on x/y
                            .offset {
                                IntOffset(
                                    x = (
                                            x - overlaySize.width / 2f
                                            ).toInt(),

                                    y = (
                                            y - overlaySize.height / 2f
                                            ).toInt()
                                )
                            }

                            .onSizeChanged {
                                overlaySize = it
                            }

                            .graphicsLayer {
                                scaleX = item.scale?.toFloat() ?: 1f
                                scaleY = item.scale?.toFloat() ?: 1f
                                rotationZ = item.rotation?.toFloat() ?: 0f
                            }
                    ) {
                        when (item.type) {
                            "emoji" -> EmojiOverlay(item)

                            "text" -> TextOverlay(item)

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmojiOverlay(overlay: StoriesListResponse.UserStoryGroup.StoryOverlay) {
    Text(text = overlay.value.toString(), fontSize = 42.sp)
}

@Composable
private fun TextOverlay(overlay: StoriesListResponse.UserStoryGroup.StoryOverlay) {
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
            text = overlay.value.toString(),
            color = textColor,
            fontSize = (overlay.fontSize ?: 18.0).sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun String?.toComposeColorOrNull(): Color? =
    this?.let { hex ->
        runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrNull()
    }

private fun String?.toComposeColorOrDefault(default: Color): Color =
    this.toComposeColorOrNull() ?: default
