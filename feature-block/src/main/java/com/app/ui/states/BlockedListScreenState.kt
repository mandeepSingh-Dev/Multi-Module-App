package com.app.ui.states

import com.app.core_common.base.UIState
import com.app.feature_block.remote.model.BlockUsersListResponse

data class BlockedListScreenState(
    val isLoading: Boolean = false,
    val isBlockLoading: Boolean = false,
    val data: List<BlockUsersListResponse.BlockUserData?>? = null,
    val isRefreshing: Boolean = false
) : UIState