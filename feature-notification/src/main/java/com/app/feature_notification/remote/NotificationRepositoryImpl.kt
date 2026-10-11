package com.app.feature_notification.remote

import com.app.feature_notification.domain.repository.NotificationRepository
import com.app.feature_notification.remote.model.NotificationListResponse
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class NotificationRepositoryImpl @Inject constructor(
    private val notificationService: NotificationService,
    private val networkHelper: NetworkHelper,
) : NotificationRepository {
    override fun getNotificationsList(
        page : Int,
        limit : Int,
    ): Flow<NetworkResult<NotificationListResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<NotificationListResponse>(
                call = {
                    notificationService.getNotificationsList(
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