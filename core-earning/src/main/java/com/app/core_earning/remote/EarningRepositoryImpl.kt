package com.app.core_earning.remote

import com.app.core_common.UserManager
import com.app.core_earning.domain.repository.EarningRepository
import com.app.core_earning.model.response.EarningDetailsResponse
import com.app.core_earning.model.response.EarningTrendResponse
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EarningRepositoryImpl @Inject constructor(
    private val earningService: EarningService,
    private val networkHelper: NetworkHelper,
) : EarningRepository {

    override fun getEarningDetails(period : String?): Flow<NetworkResult<EarningDetailsResponse>> {
        return try {
            networkHelper.executeWithFlow<EarningDetailsResponse>(
                call = {
                    earningService.getEarningDetails(period = period)
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
    override fun getEarningTrendDetails(period : String?): Flow<NetworkResult<EarningTrendResponse>> {
        return try {
            networkHelper.executeWithFlow<EarningTrendResponse>(
                call = {
                    earningService.getEarningTrendResponse(period = period)
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
