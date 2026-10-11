package com.app.feature_bottom_nav.navigation

import androidx.compose.ui.unit.Dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.utils.enums.UserType
import com.app.feature_home.ui.ChatUsersListScreen
import com.app.feature_home.ui.HomeUserScreen
import com.app.feature_home.ui.ProfileSettingsScreen
import com.app.feature_home.ui.HostSearchScreen

fun EntryProviderScope<NavKey>.homeNavUserScreensGraph(
    bottomTabBarHeight: Dp,
    bottomNavBackstack: NavBackStack<NavKey>
) {

    entry<HomeUserNavScreens.HomeScreen> {
        HomeUserScreen(bottomTabBarHeight, onOnlineProfilesSeeAll = {
            bottomNavBackstack.add(HomeUserNavScreens.HomeSearchScreen(showOnlineProfiles = true))
        }, onRecommendedProfilesSeeAll = {
            bottomNavBackstack.add(HomeUserNavScreens.HomeSearchScreen())
        })
    }
    entry<HomeUserNavScreens.HomeSearchScreen> {
            HostSearchScreen(showOnlineProfile = it.showOnlineProfiles,bottomTabBarHeight)
        }
    entry<HomeUserNavScreens.ChatScreen> {
            ChatUsersListScreen(bottomTabBarHeight, userType = UserType.USER.value)
        }
    entry<HomeUserNavScreens.ProfileScreen> {
        ProfileSettingsScreen(bottomTabBarHeight, UserType.USER.value)
    }
}
