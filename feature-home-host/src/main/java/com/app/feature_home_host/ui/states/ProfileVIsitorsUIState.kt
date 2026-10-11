package com.app.feature_home_host.ui.states

import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState
import com.app.feature_home_host.remote.DashboardStatsApiResponse.DashboardStatsData

data class ProfileVisitorsUIState(
    val isLoading :  Boolean? = null,
    val isRefreshing :  Boolean = false,
) : UIState