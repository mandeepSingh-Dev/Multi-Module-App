package com.app.feature_leaderboard.remote

 import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse
 import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LeaderboardService {

    @GET(EndPoints.Leaderboard.LEADERBOARD)
    suspend fun getLeaderboardList(
        @Query(ApiConstants.page) page : Int,
        @Query(ApiConstants.limit) limit : Int,
    ): Response<LeaderboardListResponse>

}