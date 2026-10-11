package com.app.feature_home.remote

import android.util.Log
import com.app.core_common.UserManager
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.model.response.ChatListResponse
import com.app.feature_home.remote.model.response.DailyStreakResponse
import com.app.feature_home.remote.model.response.HomeDataUserResponse
import com.app.feature_home.remote.model.response.HostDetailsDataResponse
import com.app.feature_home.remote.model.response.HostListResponse
import com.app.feature_home.remote.model.response.TrackedVisitorResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeUserRepositoryImpl @Inject constructor(
    private val homeUserService: HomeUserService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : HomeUserRepository {

    override fun getHomeData(): Flow<NetworkResult<HomeDataUserResponse>> {
        return try {
            networkHelper.executeWithFlow<HomeDataUserResponse>(
                call = {
                    homeUserService.getHomeData()
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


    override fun getChatList(
        page: Int,
        limit: Int,
        filter: String?,
        search: String?
    ): Flow<NetworkResult<ChatListResponse>> {

        Log.d("bknfkbnf", page.toString())
        return try {
            networkHelper.executeWithResponseManipulate<ChatListResponse>(
                call = {
                    homeUserService.getChatList(
                        page = page,
                        limit = limit,
                        filter = filter,
                        search = search,
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



    override fun getHostList(
        page: Int, limit: Int, search: String?,
        city: String?,
        gender: String?,
        maxAge: Int?,
        isOnline: Int?,
        minRating: Float?,
        interests: List<String?>?,
    ): Flow<NetworkResult<HostListResponse>> {

        Log.d("bknfkbnf", page.toString())
        return try {
            networkHelper.executeWithResponseManipulate<HostListResponse>(
                call = {
                    homeUserService.getHostsList(
                        page = page,
                        limit = limit,
                        search = search,
                        city = city,
                        gender = gender,
                        maxAge = maxAge,
                        isOnline = isOnline,
                        minRating = minRating,
                        interests = interests,
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

    override fun getHostDetails(id: String?): Flow<NetworkResult<HostDetailsDataResponse>> {

        return try {
            networkHelper.executeWithResponseManipulate<HostDetailsDataResponse>(
                call = {
                    homeUserService.getHostDetails(
                        id = id
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





    override fun trackVisitors(id: String?): Flow<NetworkResult<TrackedVisitorResponse>> {

        val body = hashMapOf(ApiConstants.viewedUserId to id)
        return try {
            networkHelper.executeWithFlow<TrackedVisitorResponse>(
                call = {
                    homeUserService.trackVisitors(body)
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
    override fun getDailyStreak(): Flow<NetworkResult<DailyStreakResponse>> {

        return try {
            networkHelper.executeWithFlow<DailyStreakResponse>(
                call = {
                    homeUserService.getDailyStreak()
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


}
