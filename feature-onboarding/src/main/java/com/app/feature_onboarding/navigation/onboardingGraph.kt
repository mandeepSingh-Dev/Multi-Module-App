package com.app.feature_onboarding.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_onboarding.ui.OnBoardingScreen
import com.app.feature_onboarding.ui.UserTypeSelectionScreen

fun EntryProviderScope<NavKey>.onboardingGraph() {

    entry<AppScreens.OnBoardingScreen> {
        OnBoardingScreen()
    }
    entry<AppScreens.UserTypeSelectionScreen> {
        UserTypeSelectionScreen()
    }
}
