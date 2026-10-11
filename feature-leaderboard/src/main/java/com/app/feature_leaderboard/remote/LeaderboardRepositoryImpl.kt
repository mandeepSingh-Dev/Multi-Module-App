package com.app.feature_leaderboard.remote

import com.app.feature_leaderboard.domain.repository.LeaderboardRepository
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class LeaderboardRepositoryImpl @Inject constructor(
    private val notificationService: LeaderboardService,
    private val networkHelper: NetworkHelper,
) : LeaderboardRepository {
    override fun getLeaderboardList(
        page : Int,
        limit : Int,
    ): Flow<NetworkResult<LeaderboardListResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<LeaderboardListResponse>(
                call = {
                    notificationService.getLeaderboardList(
                        page = page,
                        limit = limit
                    )
                },
                onSuccess = { response ->
                    response
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}