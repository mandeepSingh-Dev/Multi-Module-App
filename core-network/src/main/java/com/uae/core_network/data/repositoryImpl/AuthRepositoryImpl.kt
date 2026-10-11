/*
package com.uae.core_network.data.repositoryImpl

import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AssistanceRepositoryImpl  @Inject constructor(
    private val assistanceService: AssistanceService,
    private val networkHelper: NetworkHelper,
) : AssistanceRepository {



    override fun login(
        page: Int, limit: Int, search: String?, status: Int?,
    ): Flow<NetworkResult<AssistanceListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<AssistanceListResponse>(
                call = {
                    assistanceService.assistanceListing(
                        page = page,
                        limit = limit,
                        search = search,
                        status = status
                    )
                },
                onSuccess = { response ->
                    null
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
*/
