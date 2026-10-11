package com.app.feature_referral.remote

import com.app.feature_referral.domain.repository.ReferralRepository
import com.app.feature_referral.remote.model.response.ReferralDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReferralRepositoryImpl  @Inject constructor(
    private val callService: ReferralService,
    private val networkHelper: NetworkHelper,
) : ReferralRepository {

    override fun getReferralDetails(): Flow<NetworkResult<ReferralDetailsResponse>> {
        return try {
            networkHelper.executeWithFlow<ReferralDetailsResponse>(
                call = {
                    callService.getReferralDetails()
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
    override fun referralApply(referralCode : String?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            val body = hashMapOf(ApiConstants.referralCode to referralCode)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    callService.referralApply(body = body)
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
    override fun referralViewed(): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    callService.referralViewed()
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