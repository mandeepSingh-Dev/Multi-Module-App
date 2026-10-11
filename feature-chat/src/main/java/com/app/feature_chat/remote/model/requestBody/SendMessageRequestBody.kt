package com.app.feature_chat.remote.model.requestBody


import com.app.feature_chat.remote.model.response.ChatMessagesResponse

data class SendMessageRequestBody(
    @Transient
    val tempId : String?,
    @Transient
    val createdAt : String?,
    val chatId: String?,
    val fileName: String?,
    val message: String?,
    val messageType: String?,
    val mimeType: String?,
    val partnerId: String?,
    val size: String?,
    val url: String?
){


    fun toMessageChat(user: ChatMessagesResponse.MessageChat.User): ChatMessagesResponse.MessageChat {
        return ChatMessagesResponse.MessageChat(
            createdAt = this.createdAt,
            fileName = this.fileName,
            id = this.tempId,
            isRead = null,
            message = this.message,
            messageType = this.messageType,
            mimeType = this.mimeType,
            size = this.size,
            updatedAt = this.createdAt,
            url = this.url,
            user = user,
            userId = user.id
        )
    }

}