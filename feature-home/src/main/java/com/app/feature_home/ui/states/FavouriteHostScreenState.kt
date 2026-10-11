package com.app.feature_home.ui.states

import com.app.core_common.base.UIState

data class FavouriteHostScreenState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
)  : UIState