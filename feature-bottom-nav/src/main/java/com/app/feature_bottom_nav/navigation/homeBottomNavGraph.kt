package com.app.feature_bottom_nav.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.core_navigation.UserScreens
import com.app.feature_bottom_nav.ui.HomeBottomContainerHostScreens
import com.app.feature_bottom_nav.ui.HomeBottomContainerScreens

fun EntryProviderScope<NavKey>.homeBottomNavGraph() {
    entry<AppScreens.HomeBottomContainerScreen> {
        HomeBottomContainerScreens()
    }
    entry<AppScreens.HomeBottomContainerHostScreen> {
        HomeBottomContainerHostScreens()
    }
}