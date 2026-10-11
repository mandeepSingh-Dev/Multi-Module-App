package com.app.feature_referral.domain.repository

import com.app.feature_referral.remote.model.response.ReferralDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow


interface ReferralRepository {
    fun getReferralDetails(): Flow<NetworkResult<ReferralDetailsResponse>>
    fun referralViewed(): Flow<NetworkResult<ApiResponse2>>
    fun referralApply(referralCode: String?): Flow<NetworkResult<ApiResponse2>>
}
