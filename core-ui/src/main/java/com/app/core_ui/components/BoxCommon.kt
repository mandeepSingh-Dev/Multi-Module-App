package com.app.core_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import com.app.core_ui.R

@Composable
fun BoxCommon(
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor : androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
    containerBrush : Brush ? = null,
    contentAlignment : Alignment = Alignment.TopStart,
    isAppearanceLightStatusBars : Boolean = true,
    isAppearanceLightNavigationBars : Boolean = true,
    showBackgroundImage : Boolean = true,
    isLoaderTappable : Boolean = false,
    bodyContent: @Composable BoxScope.() -> Unit,
) {

    SetStatusBarTextColor(isAppearanceLightStatusBars)
    SetNavigationBarTextColor(isAppearanceLightNavigationBars)



    val isLoader by rememberUpdatedState(isLoading)

    val localKeyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = modifier.clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }){
        localKeyboardController?.hide()
    },
        contentAlignment = contentAlignment,
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .then(if(containerBrush != null){
                modifier.background(brush = containerBrush)
            }else{
                modifier.background(color = containerColor)
            })
        ) {
            if(showBackgroundImage) {
                Image(
                    painterResource(R.drawable._3__onboarding__1),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
            bodyContent()
            ProgressLoader(isLoader, isTappable = isLoaderTappable)
        }
    }
}
@Composable
fun BoxCommon2(
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor : androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White,
    containerBrush : Brush ? = null,
    contentAlignment : Alignment = Alignment.TopStart,
    bodyContent: @Composable BoxScope.() -> Unit,
) {

    val isLoader by rememberUpdatedState(isLoading)

    val localKeyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = modifier.clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }){
        localKeyboardController?.hide()
    },
        contentAlignment = contentAlignment,
    ) {
        Box(modifier = Modifier
            .then(if(containerBrush != null){
                modifier.background(brush = containerBrush)
            }else{
                modifier.background(color = containerColor)
            })
        ) {
            bodyContent()
            ProgressLoader(isLoader)
        }
    }
}