package com.app.feature_home_host.ui.states

import com.app.core_common.base.UIState
import com.app.feature_home_host.remote.model.response.VisitorDetailsResponse

data class VisitorDetailsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: VisitorDetailsResponse.Data? = null
) : UIState