package com.app.feature_call.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.core_navigation.CallScreens
import com.app.feature_call.ui.CallHistoryScreen
import com.app.feature_call.ui.FeedbackScreen

fun EntryProviderScope<NavKey>.callGraph() {

    entry< CallScreens.CallHistoryScreen> {
        CallHistoryScreen()
    }
    entry<AppScreens.FeedbackScreen> {
        FeedbackScreen(
            partnerId = it.partnerId,
            callId = it.callId,
            profilePic = it.profilePic,
            onBack = {},
            onSuccess = {}
        )
    }
}