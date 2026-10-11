package com.app.feature_notification.domain.repository

import com.app.feature_notification.remote.model.NotificationListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotificationsList(page: Int, limit: Int): Flow<NetworkResult<NotificationListResponse>>
}
