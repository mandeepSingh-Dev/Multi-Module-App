package com.app.feature_call.domain

import com.app.feature_call.domain.repository.CallRepository
import com.app.feature_call.remote.model.requestBody.GenerateTokenRequestBody
import com.app.feature_call.remote.model.response.GenerateTokenResponse
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class CallAllUseCases @Inject constructor(
    val generateTokenUseCase: GenerateTokenUseCase,
)

class GenerateTokenUseCase @Inject constructor(private val callRepository: CallRepository) {
    operator fun invoke(
        generateTokenRequestBody: GenerateTokenRequestBody?
    ): Flow<NetworkResult<GenerateTokenResponse>> {
        return callRepository.generateToken(
            generateTokenRequestBody = generateTokenRequestBody
        )
    }
}

