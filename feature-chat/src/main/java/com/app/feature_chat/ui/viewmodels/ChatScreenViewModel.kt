package com.app.feature_chat.ui.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.fromJson
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.feature_block.domain.BlockAllUseCases
import com.app.feature_chat.domain.ChatAllUseCases
import com.app.feature_chat.domain.mapper.toChatMessage
import com.app.feature_chat.remote.model.requestBody.SendMessageRequestBody
import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.app.feature_chat.ui.events.ChatScreenEvents
import com.app.feature_chat.ui.states.ChatScreenState
import com.uae.core_network.data.model.FavouriteUnFavResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import com.uae.core_socket.SocketConstants
import com.uae.core_socket.SocketIOManager
import com.uae.core_socket.model.ChatJoinedResponse
import com.uae.core_socket.model.ChatNewMessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val chatAllUseCases: ChatAllUseCases,
    private val authAllUseCases: AuthAllUseCases,
    private val commonAllUseCases: CommonAllUseCases,
    private val blockAllUseCases: BlockAllUseCases,
    private val userMgr: UserManager,

    ) : BaseViewModel<ChatScreenState>(ChatScreenState()) {


    var partnerID: String? = null
    var chatID: String? = null
    var page: Int = 0

    init {
        getProfileData()
        listenNewMessage()
    }


    fun listenNewMessage() {
        SocketIOManager.events.onEach { socketEvent ->
            Log.d("fkbnjkfbnf", socketEvent.toString())
            when (socketEvent) {
                is SocketIOManager.SocketEvent.NewMessage -> {
                    val slefUserId = uiState.value?.profileData?.id
                    socketEvent.data?.toString()?.fromJson<ChatNewMessageResponse>().takeIf {
                        it?.message?.userId != slefUserId
                    }?.toChatMessage()?.let {
                        appendInList(it)
                    }
                }

                is SocketIOManager.SocketEvent.ChatJoined -> {
                    val response = socketEvent.data?.toString()?.fromJson<ChatJoinedResponse>()
                    chatID = response?.chatId
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun getProfileData() {
        viewModelScope.launch {
            val profileData =
                userMgr.getUserDataString().fromJson<ProfileResponse.UserProfileData>()
            updateState { state ->
                state.copy(profileData = profileData)
            }
            authAllUseCases.getProfileUseCase().onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        val profileData = it.data?.data
                        updateState { state ->
                            state.copy(profileData = profileData)
                        }
                    },
                    onError = {
                    }
                )
            }.launchIn(this)
        }
    }

    fun sendJoinChatSocketEvent() {
        chatID?.let {
            val jsonObject = JSONObject()
            jsonObject.put(SocketConstants.CHAT_ID, chatID)
            jsonObject.put(SocketConstants.PARTNER_ID, partnerID)
            Log.d("fkbnfkbnf", jsonObject.toString())
            val d = SocketIOManager.emit(SocketIOManager.CHAT_JOIN, jsonObject)
            Log.d("fkbnkfnbf", d.toString())
        }
    }

    fun sendLeaveChatSocketEvent() {
        chatID?.let {
            val jsonObject = JSONObject()
            jsonObject.put(SocketConstants.CHAT_ID, chatID)
            jsonObject.put(SocketConstants.PARTNER_ID, partnerID)
            Log.d("fkbnfkbnf", jsonObject.toString())
            val d = SocketIOManager.emit(SocketIOManager.CHAT_LEAVE, jsonObject)
            Log.d("fkbnkfnbf", d.toString())
        }
    }

    fun sendMessage(
        message: String?,
        partnerId: String?,
        chatId: String?
    ) {
        val body = SendMessageRequestBody(
            tempId = "${System.currentTimeMillis()}-${UUID.randomUUID()}",
            chatId = chatId,
            fileName = null,
            message = message,
            messageType = "text",
            mimeType = null,
            partnerId = partnerId,
            size = null,
            url = null,

            createdAt = System.currentTimeMillis()
                .convertToDateFormat(DateFormats.DATE_FORMAT_12, true)
        )

        val user = ChatMessagesResponse.MessageChat.User(
            fullname = uiState.value?.profileData?.fullName,
            profilePic = uiState.value?.profileData?.profilePic,
            id = uiState.value?.profileData?.id
        )


        appendInList(
            body.toMessageChat(user)
        )
        chatAllUseCases.sendMessageUseCase(body).onEach { it ->
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isTextMessageSending = true)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isTextMessageSending = false)
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(isTextMessageSending = false)
                    }
                }
            )
        }.launchIn(viewModelScope)
    }

    private fun appendInList(messageChat: ChatMessagesResponse.MessageChat) {
        updateState { state ->
            val mList = state.messages?.toMutableList()

            mList?.add(0, messageChat)
            state.copy(messages = mList)
        }

    }

    fun loadMessages(page: Int, chatId: String?) {
//        if(chatId.isNullOrEmpty()) return

        chatAllUseCases.getChatMessagesUseCase(
            page = page,
            limit = 10,
            chatID = chatId
        ).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isLoading = true)
                    }
                },
                onSuccess = {

                    this.page = page

                    updateState { state ->
                        if (page == 1) {

                            state.copy(
                                messages = it.data?.data?.reversed(), isLoading = false,
                                futureListEmpty = it.data?.data.isNullOrEmpty()
                            )
                        } else {
                            val newList =
                                state.messages?.plus((it.data?.data ?: emptyList()).reversed())
                            state.copy(
                                messages = newList, isLoading = false,
                                futureListEmpty = (it.data?.data.isNullOrEmpty()) || it.data?.count == newList?.size
                            )
                        }
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                }
            )
        }.launchIn(viewModelScope)
    }

    var favouriteJob: Job? = null
    fun updateFavourite(isFav: Boolean) {

        updateState { state -> state.copy(isPartnerFav = if (isFav) 1 else 0) }
        favouriteJob?.cancel()
        favouriteJob = viewModelScope.launch {
            delay(400.milliseconds)
            hitFavouriteHostApi().launchIn(this)
        }
    }

    fun hitFavouriteHostApi(): Flow<NetworkResult<FavouriteUnFavResponse>> {
        return commonAllUseCases.favouriteHostUseCase(id = partnerID)
            .onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        updateState { state -> state.copy(isPartnerFav = if (it.data?.data?.isFavourite == true) 1 else 0) }
                    },
                    onError = {
                        errorEvent(it.error)
                    }
                )
            }
    }

    fun block(id: String?) {
        blockAllUseCases.blockUnblockUseCase(id = id).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isBlockLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isBlockLoading = false, isBlocked = it.data?.data?.isBlock ?: false)
                    }
                    if (it.data?.data?.isBlock == true) {
                        onEvent(ChatScreenEvents.BlockedSuccessfully)
                    } else {
                        onEvent(ChatScreenEvents.UnblockedSuccessfully)
                    }
                },
                onError = {
                    updateState { state -> state.copy(isBlockLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }


    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        sendLeaveChatSocketEvent()
        super.onCleared()

    }


}