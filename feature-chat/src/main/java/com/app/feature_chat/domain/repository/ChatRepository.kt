package com.app.feature_chat.domain.repository

import com.app.feature_chat.remote.model.requestBody.SendMessageRequestBody
import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow


interface ChatRepository {
    fun sendMessage(sendMessageRequestBody: SendMessageRequestBody?): Flow<NetworkResult<ApiResponse2>>
    fun getChatMessage(
        page: Int,
        limit: Int,
        chatId: String?
    ): Flow<NetworkResult<ChatMessagesResponse>>
}
