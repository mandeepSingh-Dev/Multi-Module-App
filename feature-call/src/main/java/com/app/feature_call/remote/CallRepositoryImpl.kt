package com.app.feature_call.remote

import com.app.core_common.UserManager
import com.app.feature_call.domain.repository.CallRepository
import com.app.feature_call.remote.model.requestBody.GenerateTokenRequestBody
import com.app.feature_call.remote.model.response.CallHistoryResponse
import com.app.feature_call.remote.model.response.GenerateTokenResponse
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CallRepositoryImpl  @Inject constructor(
    private val callService: CallService,
    private val networkHelper: NetworkHelper,
) : CallRepository {

    override fun generateToken(generateTokenRequestBody: GenerateTokenRequestBody?): Flow<NetworkResult<GenerateTokenResponse>> {
        return try {
            networkHelper.executeWithFlow<GenerateTokenResponse>(
                call = {
                    callService.generateToken(generateTokenRequestBody = generateTokenRequestBody)
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
    override fun getCallHistory(page : Int, limit: Int, type : String?): Flow<NetworkResult<CallHistoryResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<CallHistoryResponse>(
                call = {
                    callService.getCallHistory(
                        type = type,
                        page = page,
                        limit = limit
                    )
                },
                onSuccess = { response ->
                    response
                },
                onError = {
                },
                shouldLoading = false

            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

}