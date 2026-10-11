package com.app.core_ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerEffect()
    )

}

@Composable
fun AnimatedContentShimmer(
    modifier: Modifier = Modifier,
    modifierShimmerBox : Modifier = Modifier,
    isLoading : Boolean = false,
    content : @Composable () -> Unit
){

    AnimatedContent(isLoading,
        modifier = modifier) {isLoading ->

        if(isLoading){
            ShimmerBox(modifier = modifierShimmerBox)
        }else{
            content()
        }
    }

}