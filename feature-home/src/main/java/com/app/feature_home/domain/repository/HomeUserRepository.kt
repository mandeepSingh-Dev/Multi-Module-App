package com.app.feature_home.domain.repository

import com.app.feature_home.remote.model.response.ChatListResponse
import com.app.feature_home.remote.model.response.DailyStreakResponse
import com.app.feature_home.remote.model.response.HomeDataUserResponse
import com.app.feature_home.remote.model.response.HostDetailsDataResponse
import com.app.feature_home.remote.model.response.HostListResponse
import com.app.feature_home.remote.model.response.TrackedVisitorResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface HomeUserRepository {
    fun getHomeData(): Flow<NetworkResult<HomeDataUserResponse>>
    fun getChatList(page: Int, limit: Int, filter: String?, search : String?): Flow<NetworkResult<ChatListResponse>>
     fun getHostList(
        page: Int,
        limit: Int,
        search: String?,
        city: String?,
        gender: String?,
        maxAge: Int?,
        isOnline: Int?,
        minRating: Float?,
        interests: List<String?>?,
        ): Flow<NetworkResult<HostListResponse>>

    fun getHostDetails(id: String?): Flow<NetworkResult<HostDetailsDataResponse>>

    fun trackVisitors(id: String?): Flow<NetworkResult<TrackedVisitorResponse>>

    fun getDailyStreak(): Flow<NetworkResult<DailyStreakResponse>>
}
