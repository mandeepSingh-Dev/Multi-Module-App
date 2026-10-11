package com.app.feature_bottom_nav.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_common.utils.enums.UserType
import com.app.feature_home.ui.ChatUsersListScreen
import com.app.feature_home.ui.ProfileSettingsScreen
import com.app.feature_home.ui.HostSearchScreen
import com.app.feature_home_host.ui.EarningDetailsScreen
import com.app.feature_home_host.ui.HomeHostScreen

fun EntryProviderScope<NavKey>.homeNavHostScreensGraph(bottomTabBarHeight: Dp,
                                                       onEarningScreen : () -> Unit) {

    entry<HomeHostNavScreens.HomeScreen> {
        HomeHostScreen(bottomTabBarHeight,
            onEarningScreen = onEarningScreen)
    }
    entry<HomeHostNavScreens.EarningsScreen> {
        EarningDetailsScreen(bottomTabBarHeight)
        }
    entry<HomeHostNavScreens.ChatScreen> {
            ChatUsersListScreen(bottomTabBarHeight, userType = UserType.HOST.value)
        }
    entry<HomeHostNavScreens.ProfileScreen> {
        ProfileSettingsScreen(bottomTabBarHeight, UserType.HOST.value)
    }
}
