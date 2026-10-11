package com.app.core_earning.domain

import com.app.core_earning.domain.repository.EarningRepository
import com.app.core_earning.model.response.EarningDetailsResponse
import com.app.core_earning.model.response.EarningTrendResponse
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class EarningAllUseCases @Inject constructor(
     val getEarningDetailsUseCase: GetEarningDetailsUseCase,
     val getEarningTrendDetailsUseCase: GetEarningTrendDetailsUseCase
)

class GetEarningDetailsUseCase @Inject constructor(private val earningRepository: EarningRepository) {

    operator fun invoke(period : String?): Flow<NetworkResult<EarningDetailsResponse>>? {
        return earningRepository.getEarningDetails(period = period)
    }
}
class GetEarningTrendDetailsUseCase @Inject constructor(private val earningRepository: EarningRepository) {

    operator fun invoke(period : String?): Flow<NetworkResult<EarningTrendResponse>>? {
        return earningRepository.getEarningTrendDetails(period = period)
    }
}