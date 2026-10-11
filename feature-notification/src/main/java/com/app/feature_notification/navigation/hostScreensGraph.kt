package com.app.feature_notification.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_notification.ui.NotificationListScreen

fun EntryProviderScope<NavKey>.notificationGraph() {

    entry<AppScreens.NotificationListScreen> {
        NotificationListScreen()
    }
    }
