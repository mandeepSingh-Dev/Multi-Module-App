package com.app.core_ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.core_ui.ui.theme.theme_color_1


@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    shape : androidx.compose.ui.graphics.Shape = CircleShape,
    image : Any? = null
) {

    Box(
    modifier = Modifier
    .clip(shape)
    .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
    .padding(1.dp)
    ) {
        AsyncImageWithLoader(
            image = image ,
            placeholder = painterResource(com.app.core_ui.R.drawable.placeholderbackground),
            modifier = modifier
        )
    }
}