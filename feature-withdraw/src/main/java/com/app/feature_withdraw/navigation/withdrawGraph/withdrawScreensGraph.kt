package com.app.feature_withdraw.navigation.withdrawGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.WithDrawScreens
import com.app.feature_withdraw.ui.screens.IdentityVerificationScreen
import com.app.feature_withdraw.ui.screens.WithdrawBeansScreens

fun EntryProviderScope<NavKey>.withdrawScreensGraph() {

    entry<WithDrawScreens.IdentityVerificationScreen> {
        IdentityVerificationScreen()
    }
    entry<WithDrawScreens.WithdrawBeansScreens> {
        WithdrawBeansScreens()
    }
}
