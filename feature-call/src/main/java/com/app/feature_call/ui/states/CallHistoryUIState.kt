package com.app.feature_call.ui.states

import com.app.core_common.base.UIState

data class CallHistoryUIState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
) : UIState