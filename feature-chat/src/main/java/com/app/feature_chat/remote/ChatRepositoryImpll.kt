package com.app.feature_chat.remote

import com.app.core_common.UserManager
import com.app.feature_chat.domain.repository.ChatRepository
import com.app.feature_chat.remote.model.requestBody.SendMessageRequestBody
import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl  @Inject constructor(
    private val chatService: ChatService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : ChatRepository {

    override fun getChatMessage(page :  Int, limit : Int, chatId : String?): Flow<NetworkResult<ChatMessagesResponse>> {
        return try {
            networkHelper.executeWithFlow<ChatMessagesResponse>(
                call = {
                    chatService.getChatMessages(
                        page = page,
                        limit = limit,
                        chatId = chatId
                    )
                },
                onSuccess = { response ->
                    null
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
    override fun sendMessage(sendMessageRequestBody: SendMessageRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    chatService.sendMessage(
                        sendMessageRequestBody = sendMessageRequestBody)
                },
                onSuccess = { response ->
                    null
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