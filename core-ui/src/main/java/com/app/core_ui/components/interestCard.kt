package com.app.core_ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_9

val interestsList = listOf(
    "Casual Row",
    "Friendship",
    "Music",
    "Movies",
    "Travel",
    "Sports",
    "Food",
    "Books",
    "Fun & Entertainment",
)


@Composable
fun interestCard(
    name : String?,
    selected: Boolean,
    onSelect: () -> Unit
) {

    val bgColor by animateColorAsState(if(selected) Color.Black else theme_color_9)
    val shadowColor by animateColorAsState(if(selected) theme_color_11 else Color.Black.copy(0.1f))
    val textColor by animateColorAsState(if(selected) Color.White else Color.Black)


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .dropShadowCustom(color = shadowColor)
            .clip(CircleShape)
            .clickable{
                onSelect()
            }
            .background(color = bgColor, shape = CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp)) {

        Image(painterResource(R.drawable.clear_face_iconobackground), contentDescription = null, modifier = Modifier.size(30.dp))
        Text(name ?: "", modifier = Modifier, color = textColor, fontSize = 12.sp)
    }
}