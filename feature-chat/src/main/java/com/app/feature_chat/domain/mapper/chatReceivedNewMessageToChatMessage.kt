package com.app.feature_chat.domain.mapper

import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.uae.core_socket.model.ChatNewMessageResponse

fun ChatNewMessageResponse?.toChatMessage(): ChatMessagesResponse.MessageChat? {

    if(this == null) return null
    val msg = this.message
    return ChatMessagesResponse.MessageChat(
        createdAt = msg?.createdAt,
        fileName = msg?.fileName,
        id = msg?.id,
        isRead = msg?.isRead,
        message = msg?.message,
        messageType = msg?.messageType,
        mimeType = msg?.mimeType,
        role = msg?.role,
        size = msg?.size,
        updatedAt = msg?.updatedAt,
        url = msg?.url,
        user = ChatMessagesResponse.MessageChat.User(
            fullname = msg?.user?.fullname,
            id =  msg?.user?.id,
            profilePic =  msg?.user?.profilePic
        ),
        userId = msg?.userId
    )

}