package com.app.feature_home_host.remote.model.response


import com.google.gson.annotations.SerializedName

data class ChatListResponse(
    val count: Int?,
    val `data`: ChatListData?,
    val message: String?,
    val status: Int?
) {
    data class ChatListData(
        val chats: List<Chat>?,
        val counts: Counts?
    ) {
        data class Chat(
            @SerializedName("_id")
            val id: String?,
            val lastMessage: String?,
            val lastMessageAt: String?,
            val lastMessageBy: Int?,
            val partner: Partner?,
            val unreadCount: Int?,
            val updatedAt: String?
        ) {
            data class Partner(
                val fullName: String?,
                @SerializedName("_id")
                val id: String?,
                val isOnline: Int?,
                val lastSeen: Any?,
                val profilePic: String?,
                val rating: Int?
            )
        }

        data class Counts(
            val all: Int?,
            @SerializedName("_id")
            val id: Any?,
            val unread: Int?
        )
    }
}