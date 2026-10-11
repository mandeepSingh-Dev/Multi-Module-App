package com.app.feature_gift.domain.repository

import com.app.feature_gift.remote.model.requestBody.GiftSendRequestBody
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GiftRepository {
    fun sendGift(giftSendRequestBody: GiftSendRequestBody?): Flow<NetworkResult<ApiResponse2>>
    fun getGiftsList(): Flow<NetworkResult<GiftListResponse>>
}
