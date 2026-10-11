package com.app.feature_splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Composable
@Preview
fun SplashScreen(
    backStack : NavBackStack<NavKey> = LocalBackStackNav.current,
    splashViewModel: SplashViewModel = hiltViewModel()
) {

    AttachLifecycleObserver{event ->
        when(event){
            Lifecycle.Event.ON_RESUME -> {
                splashViewModel.startTimer()
            }
            else -> Unit
        }
    }

    ObserveUIEvents(splashViewModel.uiEvent){event ->
        when(event){
          is CommonUIEvents.NavigateTo -> {
              backStack.removeAll(backStack)
              backStack.add(event.navKey)
          }
        }
    }

    // 1. Initial Animatable values for Smooth Entrance
    val logoScale = remember { Animatable(0.85f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(20f) }

    // 2. Infinite Transition for Smooth Breathing & Ambient Glow FX
    val infiniteTransition = rememberInfiniteTransition(label = "SplashInfiniteEffects")

    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatingAnimation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAnimation"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAnimation"
    )

    // Smooth Entrance Sequence
    LaunchedEffect(Unit) {
        // Logo entrance: smooth scaling & fade in
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
        }
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }

        // Staggered smooth fade & slide-up for text
        delay(250)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    BoxCommon(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(R.drawable._1__splash_1__1_),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Animated Logo with Ambient Soft Glow & Shadow
            Image(
                painter = painterResource(R.drawable.kaho_ji_logoimage1),
                contentDescription = null,
                modifier = Modifier
                    // Soft Background Glow Layer
                    .graphicsLayer {
                        scaleX = logoScale.value * pulseScale * 1.12f
                        scaleY = logoScale.value * pulseScale * 1.12f
                        translationY = floatingOffset
//                        alpha = logoAlpha.value * glowAlpha
                    }
                    // Foreground Layer with Shadow & Scale
                    .graphicsLayer {
                        scaleX = logoScale.value * pulseScale
                        scaleY = logoScale.value * pulseScale
                        translationY = floatingOffset
                        alpha = logoAlpha.value
                        shadowElevation = 12.dp.toPx()
                        spotShadowColor = Color.Black.copy(alpha = 0.4f)
                        ambientShadowColor = Color.Black.copy(alpha = 0.2f)
                    }
            )

            // Animated Slogan Text with Smooth Slide & Fade
            Text(
                text = "Talk . Connect Enjoy",
                modifier = Modifier
                    .graphicsLayer {
                        alpha = textAlpha.value
                        translationY = textOffsetY.value + (floatingOffset * 0.4f)
                    }
            )
        }
    }
}

/*
    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable._1__splash_1__1_),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.kaho_ji_logoimage1),
                contentDescription = null,
                modifier = Modifier
            )
            Text("Talk . Connect Enjoy")
        }
    }
*/
