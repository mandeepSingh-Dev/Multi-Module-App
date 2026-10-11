package com.app.feature_change_password.navigation.authGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_change_password.ui.ChangePasswordScreen

fun EntryProviderScope<NavKey>.changePwdGraph() {

    entry<AppScreens.ChangePasswordScreen> {
        ChangePasswordScreen()
    }
}
