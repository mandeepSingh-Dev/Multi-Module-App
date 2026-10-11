package com.app.feature_chat.remote.model.response


import com.google.gson.annotations.SerializedName

data class ChatMessagesResponse(
    val count: Int?,
    val `data`: List<MessageChat?>?,
    val message: String?,
    val status: Int?
) {


    data class MessageChat(
        val createdAt: String?= null,
        val fileName: String?= null,
        @SerializedName("_id")
        val id: String?= null,
        val isRead: Boolean? = null,
        val message: String?= null,
        val messageType: String?= null,
        val mimeType: String?= null,
        val role: Int?= null,
        val size: String?= null,
        val updatedAt: String?= null,
        val url: String?= null,
        val user: User?= null,
        val userId: String?= null
    ) {
        data class User(
            val fullname: String?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?
        )
    }
}