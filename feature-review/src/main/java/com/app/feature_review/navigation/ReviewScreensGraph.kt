package com.app.feature_review.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_review.ui.ReviewsListScreen

fun EntryProviderScope<NavKey>.reviewGraph() {

    entry<AppScreens.ReviewsListScreen> {
        ReviewsListScreen()
    }
    }
