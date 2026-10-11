package com.app.feature_home.remote

import com.app.feature_home.remote.model.response.ChatListResponse
import com.app.feature_home.remote.model.response.DailyStreakResponse
import com.app.feature_home.remote.model.response.HomeDataUserResponse
import com.app.feature_home.remote.model.response.HostDetailsDataResponse
import com.app.feature_home.remote.model.response.HostListResponse
import com.app.feature_home.remote.model.response.TrackedVisitorResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeUserService {
    @GET(EndPoints.HomeUser.USER_HOME)
    suspend fun getHomeData(): Response<HomeDataUserResponse>




    @GET(EndPoints.HomeUser.CHAT_LIST)
    suspend fun getChatList(
        @Query(ApiConstants.page) page: Int,
        @Query(ApiConstants.limit) limit: Int,
        @Query(ApiConstants.filter) filter: String?,
        @Query(ApiConstants.search) search: String?,
    ): Response<ChatListResponse>
    @GET(EndPoints.HomeUser.HOST)
    suspend fun getHostsList(
        @Query(ApiConstants.page) page: Int,
        @Query(ApiConstants.limit) limit: Int,
        @Query(ApiConstants.search) search: String?,
        @Query(ApiConstants.city) city: String?,
        @Query(ApiConstants.gender) gender: String?,
        @Query(ApiConstants.maxAge) maxAge: Int?,
        @Query(ApiConstants.isOnline) isOnline: Int?,
        @Query(ApiConstants.minRating) minRating: Float?,
        @Query(ApiConstants.interests) interests: List<String?>?,
    ): Response<HostListResponse>

    @GET(EndPoints.HomeUser.HOST)
    suspend fun getHostDetails(
        @Query(ApiConstants.id) id: String?,
    ): Response<HostDetailsDataResponse>









    @POST(EndPoints.Visitors.TRACK_VISITOR)
    suspend fun trackVisitors(
        @Body map: Map<String, String?>?,
    ): Response<TrackedVisitorResponse>


    @GET(EndPoints.STREAK.DAILY_STREAK)
    suspend fun getDailyStreak(): Response<DailyStreakResponse>









}