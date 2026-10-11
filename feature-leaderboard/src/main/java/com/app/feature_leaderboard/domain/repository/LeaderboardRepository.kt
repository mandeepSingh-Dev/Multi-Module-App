package com.app.feature_leaderboard.domain.repository

import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface LeaderboardRepository {
    fun getLeaderboardList(page: Int, limit: Int): Flow<NetworkResult<LeaderboardListResponse>>
}
