package com.app.feature_home_host.navigation.authGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.HostScreens
import com.app.feature_home_host.ui.ProfileVisitorVisitorsScreen
import com.app.feature_home_host.ui.VisitorDetailsScreen

fun EntryProviderScope<NavKey>.hostScreensGraph() {

    entry<HostScreens.HostProfileVisitorsScreen> {
        ProfileVisitorVisitorsScreen()
    }

    entry<HostScreens.VisitorDetailsScreen> {
        VisitorDetailsScreen(visitorId = it.visitorId)
    }
}
