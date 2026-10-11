package com.app.core_ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.theme_color_1
import com.zodiaq.ui.theme.Shape_20

fun Modifier.strokeGapModifier(
    color: Color = theme_color_1,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 12.dp
): Modifier {

    return drawBehind {
        drawRoundRect(
            color = color,
            style = Stroke(
                width = strokeWidth.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(5.dp.toPx(), 3.dp.toPx())
                )
            ),
            cornerRadius = CornerRadius(cornerRadius.toPx())
        )
    }

}
fun Modifier.strokeGapLineModifier(
    color: Color = theme_color_1,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 12.dp
): Modifier {

    return drawBehind {
        drawLine(
            color = color,
            start = Offset(0f,0f),
            end = Offset(0f,size.height),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(5.dp.toPx(), 3.dp.toPx())
            ),
        )

    }

}

fun Modifier.padding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
    start: Dp = 0.dp,
    end: Dp = 0.dp,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp
): Modifier {

    return this
        .padding(horizontal = horizontal, vertical = vertical)
        .padding(
            top = top,
            start = start,
            end = end,
            bottom = bottom,
        )
}

fun Modifier.dropShadowCustom(shape : Shape = Shape_20, color: Color = Color.Black.copy(0.1f), radius: Dp = 10.dp): Modifier {
    return this.dropShadow(shape = shape, shadow = Shadow(color = color, radius = radius))

}
