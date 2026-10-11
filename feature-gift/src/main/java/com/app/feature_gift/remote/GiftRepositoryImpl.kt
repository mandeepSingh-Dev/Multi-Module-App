package com.app.feature_gift.remote

import android.util.Log
import com.app.core_common.UserManager
import com.app.feature_gift.domain.repository.GiftRepository
import com.app.feature_gift.remote.model.requestBody.GiftSendRequestBody
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class GiftRepositoryImpl @Inject constructor(
    private val giftService: GiftService,
    private val networkHelper: NetworkHelper,
) : GiftRepository {
    override fun getGiftsList(): Flow<NetworkResult<GiftListResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<GiftListResponse>(
                call = {
                    giftService.getGiftsList()
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

    var count = 0
    override fun sendGift(giftSendRequestBody: GiftSendRequestBody?): Flow<NetworkResult<ApiResponse2>> {

        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    giftService.sendGiftApi(giftSendRequestBody = giftSendRequestBody)
                },
                onSuccess = { response ->
                    count++
                    Log.d("knkdnvd", count.toString())
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