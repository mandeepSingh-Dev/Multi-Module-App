package com.app.feature_call.domain.repository

import com.app.feature_call.remote.model.requestBody.GenerateTokenRequestBody
import com.app.feature_call.remote.model.response.CallHistoryResponse
import com.app.feature_call.remote.model.response.GenerateTokenResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow


interface CallRepository {
    fun generateToken(generateTokenRequestBody: GenerateTokenRequestBody?): Flow<NetworkResult<GenerateTokenResponse>>
    fun getCallHistory(page : Int, limit: Int,type: String?): Flow<NetworkResult<CallHistoryResponse>>
}
