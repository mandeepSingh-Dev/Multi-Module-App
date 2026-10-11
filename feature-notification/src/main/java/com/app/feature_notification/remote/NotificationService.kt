package com.app.feature_notification.remote

import com.app.feature_notification.remote.model.NotificationListResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {

    @GET(EndPoints.Common.NOTIFICATION)
    suspend fun getNotificationsList(
        @Query(ApiConstants.page) page : Int,
        @Query(ApiConstants.limit) limit : Int,
    ): Response<NotificationListResponse>

}