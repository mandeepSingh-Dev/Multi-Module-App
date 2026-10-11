package com.app.feature_block.domain.repository

import com.app.feature_block.remote.model.BlockUnblockResponse
import com.app.feature_block.remote.model.BlockUsersListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow


interface BlockRepository {

    fun blockedUsersList(): Flow<NetworkResult<BlockUsersListResponse>>
    fun blockUnblock(id: String?): Flow<NetworkResult<BlockUnblockResponse>>
}
