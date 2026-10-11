package com.app.core_ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetStatusBarTextColor(shouldBlackText : Boolean = true) {

    val view = LocalView.current

    LaunchedEffect(Unit) {
        val window = (view.context as Activity).window
        WindowInsetsControllerCompat(window, view)
            .isAppearanceLightStatusBars = shouldBlackText
    }
}
@Composable
fun SetNavigationBarTextColor(shouldBlackText : Boolean = true) {

    val view = LocalView.current

    LaunchedEffect(Unit) {
        val window = (view.context as Activity).window
        WindowInsetsControllerCompat(window, view)
            .isAppearanceLightNavigationBars = shouldBlackText
    }
}