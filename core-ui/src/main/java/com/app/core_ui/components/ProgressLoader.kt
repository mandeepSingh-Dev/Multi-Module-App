package com.app.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.red_4
import com.app.core_ui.ui.theme.red_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_2
import com.app.core_ui.ui.theme.theme_color_6

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressLoader(
    isLoading: Boolean = false, loaderSize: Dp = 50.dp,
    isTappable: Boolean = false
) {

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = theme_color_1.copy(alpha = 0f))
                .then(
                    if (isTappable) {
                        Modifier.clickable(
                            interactionSource = null,
                            indication = ripple(color = Color.Transparent)
                        ) {}
                    } else {
                        Modifier
                    })
        ) {
            ContainedLoadingIndicator(
                modifier = Modifier
                    .size(loaderSize)
                    .dropShadow(
                        CircleShape,
                        shadow = Shadow(radius = 20.dp, color = Color.White.copy(alpha = 0.7f))
                    ),
                containerColor = red_4.copy(alpha = 0.8f),
                indicatorColor = theme_color_1,
            )
        }
    }
}