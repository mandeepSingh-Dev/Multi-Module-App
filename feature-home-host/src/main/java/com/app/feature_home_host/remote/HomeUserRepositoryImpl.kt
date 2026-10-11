package com.app.feature_home_host.remote

import android.util.Log
import com.app.core_common.UserManager
import com.app.feature_home_host.domain.repository.HomeHostRepository
import com.app.feature_home_host.remote.model.response.ProfileVisitorsListResponse
import com.app.feature_home_host.remote.model.response.VisitorDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeHostRepositoryImpl  @Inject constructor(
    private val homeUserService: HomeHostService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : HomeHostRepository {

    override fun toggleOnlineOffline(isOnline : Int): Flow<NetworkResult<ApiResponse2>> {


        return try {

            val body = hashMapOf(ApiConstants.isOnline to isOnline)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    homeUserService.toggleOnlineStatus(body = body)
                       },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun getDashboardStats(): Flow<NetworkResult<DashboardStatsApiResponse>> {
        return try {
            networkHelper.executeWithFlow<DashboardStatsApiResponse>(
                call = {
                    homeUserService.dashboardStats()
                       },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getProfileVisitors(
        page: Int,
        limit: Int,
        range: String?
    ): Flow<NetworkResult<ProfileVisitorsListResponse>> {

        Log.d("bknfkbnf", page.toString())
        return try {
            networkHelper.executeWithResponseManipulate<ProfileVisitorsListResponse>(
                call = {
                    homeUserService.getProfileVisitors(
                        page = page,
                        limit = limit,
                        range = range
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
    override fun getVisitorDetails(
        visitorID: String?
    ): Flow<NetworkResult<VisitorDetailsResponse>> {

        return try {
            networkHelper.executeWithResponseManipulate<VisitorDetailsResponse>(
                call = {
                    homeUserService.getVisitorDetails(
                        visitorID = visitorID,
                    )
                },
                onSuccess = { response ->
                    response
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}
