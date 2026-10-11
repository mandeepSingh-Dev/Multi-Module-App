package com.app.feature_notification.ui.state

import com.app.core_common.base.UIState

data class NotificationListState(val isLoading : Boolean = false,
    val isRefreshing : Boolean = false) : UIState