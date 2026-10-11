package com.app.feature_home_host.domain.repository

import com.app.feature_home_host.remote.DashboardStatsApiResponse
import com.app.feature_home_host.remote.model.response.ProfileVisitorsListResponse
import com.app.feature_home_host.remote.model.response.VisitorDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface HomeHostRepository {
    fun toggleOnlineOffline(isOnline : Int): Flow<NetworkResult<ApiResponse2>>
    fun getDashboardStats(): Flow<NetworkResult<DashboardStatsApiResponse>>
    fun getProfileVisitors(
        page: Int,
        limit: Int,
        range: String?
    ): Flow<NetworkResult<ProfileVisitorsListResponse>>

    fun getVisitorDetails(visitorID: String?): Flow<NetworkResult<VisitorDetailsResponse>>
}
