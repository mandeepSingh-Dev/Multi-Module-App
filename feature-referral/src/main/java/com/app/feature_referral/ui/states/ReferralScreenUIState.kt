package com.app.feature_referral.ui.states

import com.app.core_common.base.UIState
import com.app.feature_referral.remote.model.response.ReferralDetailsResponse

data class ReferralScreenUIState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val data : ReferralDetailsResponse.ReferralDetailsData? = null
) : UIState