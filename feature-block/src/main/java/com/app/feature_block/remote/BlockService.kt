package com.app.feature_block.remote

import com.app.feature_block.remote.model.BlockUnblockResponse
import com.app.feature_block.remote.model.BlockUsersListResponse
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BlockService {


    @POST(EndPoints.HomeUser.BLOCK_UNBLOCK)
    suspend fun blockUnblock(
        @Body map: Map<String, String?>?,
    ): Response<BlockUnblockResponse>


    @GET(EndPoints.HomeUser.BLOCK_UNBLOCK)
    suspend fun getBlockedUsersList(): Response<BlockUsersListResponse>



}