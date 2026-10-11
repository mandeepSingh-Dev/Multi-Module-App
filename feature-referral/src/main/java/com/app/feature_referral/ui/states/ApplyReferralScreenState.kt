package com.app.feature_referral.ui.states

import com.app.core_common.base.UIState

data class ApplyReferralScreenState(
    val isLoading : Boolean = false,
    val referralCode : String? = null
) : UIState