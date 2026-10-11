package com.app.feature_chat.remote

import com.app.feature_chat.remote.model.requestBody.SendMessageRequestBody
import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatService {

    @GET(EndPoints.CHAT.MESSAGES)
    suspend fun getChatMessages(
        @Query(ApiConstants.page) page : Int,
        @Query(ApiConstants.limit) limit : Int,
        @Query(ApiConstants.chatId) chatId : String?,
    ) : Response<ChatMessagesResponse>

    @POST(EndPoints.CHAT.MESSAGES)
    suspend fun sendMessage(
        @Body sendMessageRequestBody: SendMessageRequestBody?
    ) : Response<ApiResponse2>

}