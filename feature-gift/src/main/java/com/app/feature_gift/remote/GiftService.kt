package com.app.feature_gift.remote

import com.app.feature_gift.remote.model.requestBody.GiftSendRequestBody
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GiftService {

    @GET(EndPoints.HomeUser.GIFT)
    suspend fun getGiftsList(
        @Query(ApiConstants.page) page: Int = 1,
        @Query(ApiConstants.limit) limit: Int = 1000,
    ): Response<GiftListResponse>

    @POST(EndPoints.HomeUser.GIFT_SEND)
    suspend fun sendGiftApi(
        @Body giftSendRequestBody: GiftSendRequestBody?
    ): Response<ApiResponse2>

}