package com.app.feature_help_support.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_help_support.ui.HelpSupportScreen

fun EntryProviderScope<NavKey>.helpSupportGraph() {

    entry<AppScreens.HelpSupportScreen> {
        HelpSupportScreen()
    }
    }
