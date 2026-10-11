package com.app.feature_withdraw.domain

import com.app.feature_withdraw.domain.repository.WithdrawRepository
import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse
import com.app.feature_withdraw.remote.model.response.WithdrawRequestResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalSummaryDetailsResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalVerificationStatusResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class WithdrawAllUseCases @Inject constructor(
    val beanPacksListUseCase: BeanPacksListUseCase,
    val withdrawalVerificationStatus: WithdrawalVerificationStatus,
    val withdrawalSummary: WithdrawalSummary,
    val mobileVerificationStart: MobileVerificationStart,
    val mobileVerify: MobileVerify,
    val faceVerification: FaceVerification,
    val faceVerificationComplete: FaceVerificationComplete,
    val voiceVerification: VoiceVerification,
    val voiceVerificationComplete: VoiceVerificationComplete,
    val withdrawUseCase: WithdrawUseCase,
)

class WithdrawalVerificationStatus @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(): Flow<NetworkResult<WithdrawalVerificationStatusResponse>> {
        return withdrawRepository.withdrawalVerificationStatus()
    }
}

class BeanPacksListUseCase @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(): Flow<NetworkResult<BeanPacksListResponse>> {
        return withdrawRepository.getBeanPackages()
    }
}

class WithdrawalSummary @Inject constructor(private val withdrawRepository: WithdrawRepository)
{
      operator fun invoke(): Flow<NetworkResult<WithdrawalSummaryDetailsResponse>> {
        return withdrawRepository.withdrawalSummary()
    }
}

class MobileVerificationStart @Inject constructor(private val withdrawRepository: WithdrawRepository)
{
      operator fun invoke(): Flow<NetworkResult<ApiResponse2>> {
        return withdrawRepository.mobileVerificationStart()
    }
}

class MobileVerify @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(otp : String?): Flow<NetworkResult<ApiResponse2>> {
        return withdrawRepository.mobileVerify(otp = otp)
    }
}

class FaceVerification @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(url : String?): Flow<NetworkResult<ApiResponse2>> {
        return withdrawRepository.faceVerification(url = url)
    }
}

class FaceVerificationComplete @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(url : String?): Flow<NetworkResult<ApiResponse2>> {
        return withdrawRepository.faceVerificationComplete(url = url)
    }
}

class VoiceVerification @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(url : String?): Flow<NetworkResult<ApiResponse2>> {
        return withdrawRepository.voiceVerification(url = url)
    }
}

class VoiceVerificationComplete @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(url : String?): Flow<NetworkResult<ApiResponse2>> {
        return withdrawRepository.voiceVerificationComplete(url = url)
    }
}

class WithdrawUseCase @Inject constructor(private val withdrawRepository: WithdrawRepository) {
      operator fun invoke(amount : Int?): Flow<NetworkResult<WithdrawRequestResponse>> {
        return withdrawRepository.withdraw(amount = amount)
    }
}
