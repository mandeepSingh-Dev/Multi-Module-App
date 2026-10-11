package com.app.feature_leaderboard.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_leaderboard.ui.LeaderboardListScreen

fun EntryProviderScope<NavKey>.leaderboardGraph() {

    entry<AppScreens.LeaderboardListScreen> {
        LeaderboardListScreen(it.userType)
    }
    }
