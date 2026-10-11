package com.app.feature_gift.domain

import android.util.Log
import com.app.feature_gift.domain.repository.GiftRepository
import com.app.feature_gift.remote.model.requestBody.GiftSendRequestBody
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class GiftAllUseCases @Inject constructor(
    val getGiftListUseCase: GetGiftListUseCase,
    val sendGiftUseCase: SendGiftUseCase,
)


class GetGiftListUseCase @Inject constructor(private val giftRepository: GiftRepository) {
    operator fun invoke(): Flow<NetworkResult<GiftListResponse>> {
        return giftRepository.getGiftsList()
    }
}

class SendGiftUseCase @Inject constructor(private val giftRepository: GiftRepository) {
    operator fun invoke(giftSendRequestBody: GiftSendRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return giftRepository.sendGift(
            giftSendRequestBody = giftSendRequestBody
        )
    }
}
