package com.app.feature_auth_user.navigation.authGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.core_navigation.UserScreens
import com.app.feature_auth_user.ui.screens.AgeConfirmationScreen
import com.app.feature_auth_user.ui.screens.LoginUserScreen
import com.app.feature_auth_user.ui.screens.ProfileUpdateScreen
import com.app.feature_auth_user.ui.screens.ProfileUserScreen

fun EntryProviderScope<NavKey>.authGraph() {

    entry<UserScreens.AuthScreens.LoginScreen> {
        LoginUserScreen(dob = it.dob)
    }
    entry<UserScreens.AuthScreens.ProfileScreen> {
        ProfileUserScreen(dob = it.dob, step = it.step)
    }
    entry<UserScreens.AuthScreens.AgeConfirmationScreen> {
        AgeConfirmationScreen()
    }
    entry<UserScreens.AuthScreens.ProfileUpdateScreen> {
        ProfileUpdateScreen()
    }
}
