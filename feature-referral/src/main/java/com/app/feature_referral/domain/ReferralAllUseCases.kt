package com.app.feature_referral.domain

import com.app.feature_referral.domain.repository.ReferralRepository
import com.app.feature_referral.remote.model.response.ReferralDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class ReferralAllUseCases @Inject constructor(
    val getReferralDetailsUseCase: GetReferralDetailsUseCase,
    val referralApplyUseCase: ReferralApplyUseCase,
    val referralViewedUseCase: ReferralViewedUseCase,
)

class GetReferralDetailsUseCase @Inject constructor(private val referralRepository: ReferralRepository) {
    operator fun invoke(
    ): Flow<NetworkResult<ReferralDetailsResponse>> {
        return referralRepository.getReferralDetails()
    }
}


class ReferralApplyUseCase @Inject constructor(private val referralRepository: ReferralRepository) {
    operator fun invoke(referralCode : String?
    ): Flow<NetworkResult<ApiResponse2>> {
        return referralRepository.referralApply(referralCode = referralCode)
    }
}


class ReferralViewedUseCase @Inject constructor(private val referralRepository: ReferralRepository) {
    operator fun invoke(
    ): Flow<NetworkResult<ApiResponse2>> {
        return referralRepository.referralViewed()
    }
}

