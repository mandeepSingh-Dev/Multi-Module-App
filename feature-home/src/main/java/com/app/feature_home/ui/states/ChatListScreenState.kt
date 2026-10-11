package com.app.feature_home.ui.states

import com.app.core_common.base.UIState
import com.app.feature_home.remote.model.response.ChatListResponse

data class ChatListScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val chatCounts: ChatListResponse.ChatListData.Counts? = null,
) : UIState