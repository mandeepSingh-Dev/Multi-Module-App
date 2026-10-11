package com.app.feature_home_host.remote

import com.app.feature_home_host.remote.model.response.ProfileVisitorsListResponse
import com.app.feature_home_host.remote.model.response.VisitorDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeHostService {
    @PATCH(EndPoints.HomeHost.ONLINE_OFFLINE)
    suspend fun toggleOnlineStatus(
        @Body body : Map<String, Int?>
    ) : Response<ApiResponse2>

    @GET(EndPoints.HomeHost.DASHBOARD_STATS)
    suspend fun dashboardStats() : Response<DashboardStatsApiResponse>

    @GET(EndPoints.Visitors.VISITORS)
    suspend fun getProfileVisitors(
        @Query(ApiConstants.page) page: Int,
        @Query(ApiConstants.limit) limit: Int,
        @Query(ApiConstants.range) range: String?,
    ): Response<ProfileVisitorsListResponse>

    @GET(EndPoints.Visitors.VISITORS_DETAILS)
    suspend fun getVisitorDetails(
        @Path(ApiConstants.visitorID) visitorID: String?,
    ): Response<VisitorDetailsResponse>



}