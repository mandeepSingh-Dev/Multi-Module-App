package com.app.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.divider_color_3
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.linear_progress_bar_track_color

@Composable
fun DragLine(modifier: Modifier = Modifier) {

    Box(modifier = modifier.height(4.dp).background(color = grey_9, shape = CircleShape))

}