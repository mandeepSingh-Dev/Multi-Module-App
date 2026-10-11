package com.app.feature_home_host.ui.states

import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState
import com.app.feature_home_host.remote.DashboardStatsApiResponse.DashboardStatsData

data class HomeHostUiState(
    val isLoading :  Boolean? = null,
    val isRefreshing :  Boolean = false,
    val isOnlineLoading : Boolean = false,
    val isOnline : Boolean = false,
    val profileData : ProfileResponse.UserProfileData? = null,
    val dashboardData: DashboardStatsData? = null
) : UIState