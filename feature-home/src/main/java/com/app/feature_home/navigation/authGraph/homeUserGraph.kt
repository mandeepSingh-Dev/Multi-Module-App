package com.app.feature_home.navigation.authGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.UserScreens
import com.app.feature_home.ui.DaysStreakScreen
import com.app.feature_home.ui.FavouriteHostsScreen
import com.app.feature_home.ui.HostDetailsScreen

fun EntryProviderScope<NavKey>.homeUserGraph() {

    entry<UserScreens.HostDetailsScreen> {
        HostDetailsScreen(
            id = it.id,
            chatId = it.chatId,
            fullName = it.fullName,
            gender = it.gender,
            isVerified = it.isVerified,
            isWishlist = it.isWishlist,
            profilePic = it.profilePic,
            rating = it.rating
            )
    }
    entry<UserScreens.FavouriteHostsScreen> {
        FavouriteHostsScreen()
    }
    entry<UserScreens.DaysStreakScreen> {
        DaysStreakScreen()
    }
}
