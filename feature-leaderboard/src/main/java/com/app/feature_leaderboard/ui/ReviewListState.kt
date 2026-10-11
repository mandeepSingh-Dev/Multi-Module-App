package com.app.feature_leaderboard.ui

import com.app.core_common.base.UIState
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse.LeaderboardData.LeaderBoardItem

data class LeaderboardListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val resetAt: String? = null,
    val topThree: List<LeaderBoardItem?>? = null
) : UIState