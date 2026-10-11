package com.app.feature_call.remote

import com.app.feature_call.remote.model.requestBody.GenerateTokenRequestBody
import com.app.feature_call.remote.model.response.CallHistoryResponse
import com.app.feature_call.remote.model.response.GenerateTokenResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CallService {

    @POST(EndPoints.CALL.GENERATE_TOKEN)
    suspend fun generateToken(
        @Body generateTokenRequestBody : GenerateTokenRequestBody?
    ) : Response<GenerateTokenResponse>


    @GET(EndPoints.CALL.CALL_HISTORY)
    suspend fun getCallHistory(
        @Query(ApiConstants.page) page : Int?,
        @Query(ApiConstants.limit) limit : Int?,
        @Query(ApiConstants.type) type : String?
    ) : Response<CallHistoryResponse>
}