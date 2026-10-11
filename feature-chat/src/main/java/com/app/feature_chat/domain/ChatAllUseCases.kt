package com.app.feature_chat.domain

import com.app.feature_chat.domain.repository.ChatRepository
import com.app.feature_chat.remote.model.requestBody.SendMessageRequestBody
import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class ChatAllUseCases @Inject constructor(
    val getChatMessagesUseCase: GetChatMessagesUseCase,
    val sendMessageUseCase: SendMessageUseCase,
)

class GetChatMessagesUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(
        page : Int,
        limit : Int,
        chatID : String?
    ): Flow<NetworkResult<ChatMessagesResponse>> {
        return chatRepository.getChatMessage(
            page = page,
            limit = limit,
            chatId = chatID
        )
    }
}

class SendMessageUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(sendMessageRequestBody: SendMessageRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return chatRepository.sendMessage(sendMessageRequestBody = sendMessageRequestBody)
    }
}
