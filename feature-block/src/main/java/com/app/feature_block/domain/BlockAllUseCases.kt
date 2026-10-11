package com.app.feature_block.domain

import com.app.feature_block.domain.repository.BlockRepository
import com.app.feature_block.remote.model.BlockUnblockResponse
import com.app.feature_block.remote.model.BlockUsersListResponse
import com.uae.core_network.domain.repository.CommonRepository
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class BlockAllUseCases @Inject constructor(
    val blockUnblockUseCase: BlockUnblockUseCase,
    val blockListUseCase: BlockListUseCase,
)

class BlockUnblockUseCase @Inject constructor(
    private val blockRepository: BlockRepository
) {
    operator fun invoke(id : String?): Flow<NetworkResult<BlockUnblockResponse>> {
        return blockRepository.blockUnblock(id = id)
    }
}

class BlockListUseCase @Inject constructor(
    private val blockRepository: BlockRepository
) {
    operator fun invoke(): Flow<NetworkResult<BlockUsersListResponse>> {
        return blockRepository.blockedUsersList()
    }
}
