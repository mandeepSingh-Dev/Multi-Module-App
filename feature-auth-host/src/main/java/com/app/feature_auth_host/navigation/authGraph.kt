package com.app.feature_auth_host.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.HostScreens
import com.app.feature_auth_host.ui.screens.LoginHostScreen
import com.app.feature_auth_host.ui.screens.SignUpHostScreen

fun EntryProviderScope<NavKey>.authHostGraph() {

    entry<HostScreens.AuthHostScreens.LoginHostScreen> {
        LoginHostScreen()
    }
    entry<HostScreens.AuthHostScreens.SignUpHostScreen> {
        SignUpHostScreen(
            isForUpdate = it.isForUpdate,
            step = it.step,
            phone = it.phone,
            countryCode = it.countryCode,
        )
    }
}
