package com.app.feature_block.navigation.withdrawGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.BlockScreens
import com.app.core_navigation.WithDrawScreens
import com.app.ui.screen.BlockListScreen

fun EntryProviderScope<NavKey>.blockScreensGraph() {

    entry<BlockScreens.BlockScreen> {
        BlockListScreen(it.userType)
    }
//    entry<WithDrawScreens.WithdrawBeansScreens> {
//        WithdrawBeansScreens()
//    }
}
