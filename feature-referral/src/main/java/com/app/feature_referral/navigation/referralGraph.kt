package com.app.feature_referral.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
 import com.app.core_navigation.ReferralScreens
import com.app.feature_referral.ui.screens.ApplyReferralCodeScreen
import com.app.feature_referral.ui.screens.ReferEarnScreen

fun EntryProviderScope<NavKey>.referralGraph() {

    entry<ReferralScreens.ReferralScreen> {
        ReferEarnScreen()
    }
    entry<ReferralScreens.ApplyReferralCodeScreen> {
        ApplyReferralCodeScreen()
    }
}