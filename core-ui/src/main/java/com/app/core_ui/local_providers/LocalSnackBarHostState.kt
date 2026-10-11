package com.app.core_ui.local_providers

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

val LocalSnackBarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackBar_host_state provided!")
}
val LocalBackStackNav = staticCompositionLocalOf<NavBackStack<NavKey>> {
    error("No local back stack for navigation provided!")
}