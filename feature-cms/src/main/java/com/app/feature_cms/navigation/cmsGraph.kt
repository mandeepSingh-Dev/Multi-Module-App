package com.app.feature_cms.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.feature_cms.ui.CMSScreen

fun EntryProviderScope<NavKey>.cmsGraph() {

    entry<AppScreens.CMSScreen> {
        CMSScreen(type = it.type, role = it.role)
    }
    }
