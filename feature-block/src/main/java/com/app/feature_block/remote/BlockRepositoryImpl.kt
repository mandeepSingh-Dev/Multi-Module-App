package com.app.feature_block.remote

import com.app.core_common.UserManager
import com.app.feature_block.domain.repository.BlockRepository
import com.app.feature_block.remote.model.BlockUnblockResponse
import com.app.feature_block.remote.model.BlockUsersListResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BlockRepositoryImpl  @Inject constructor(
    private val blockService : BlockService,
    private val networkHelper: NetworkHelper,
) : BlockRepository {

    override fun blockedUsersList(): Flow<NetworkResult<BlockUsersListResponse>> {
        return try {
            networkHelper.executeWithResponseManipulate<BlockUsersListResponse>(
                call = {
                    blockService.getBlockedUsersList()
                },
                onSuccess = { response ->
                    response
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
    override fun blockUnblock(id: String?): Flow<NetworkResult<BlockUnblockResponse>> {

        val body = hashMapOf(ApiConstants.id to id)
        return try {
            networkHelper.executeWithResponseManipulate<BlockUnblockResponse>(
                call = {
                    blockService.blockUnblock(body)
                },
                onSuccess = { response ->
                    response
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
