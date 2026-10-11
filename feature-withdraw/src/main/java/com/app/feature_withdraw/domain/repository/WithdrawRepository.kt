package com.app.feature_withdraw.domain.repository

import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse
import com.app.feature_withdraw.remote.model.response.WithdrawRequestResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalSummaryDetailsResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalVerificationStatusResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface WithdrawRepository {

      fun withdrawalVerificationStatus(): Flow<NetworkResult<WithdrawalVerificationStatusResponse>>
      fun withdrawalSummary(): Flow<NetworkResult<WithdrawalSummaryDetailsResponse>>
      fun mobileVerificationStart(): Flow<NetworkResult<ApiResponse2>>
      fun mobileVerify(otp: String?): Flow<NetworkResult<ApiResponse2>>
      fun faceVerification(url: String?): Flow<NetworkResult<ApiResponse2>>
      fun faceVerificationComplete(url: String?): Flow<NetworkResult<ApiResponse2>>
      fun voiceVerification(url: String?): Flow<NetworkResult<ApiResponse2>>
      fun voiceVerificationComplete(url: String?): Flow<NetworkResult<ApiResponse2>>
      fun withdraw(amount: Int?): Flow<NetworkResult<WithdrawRequestResponse>>
      fun getBeanPackages(): Flow<NetworkResult<BeanPacksListResponse>>
}
