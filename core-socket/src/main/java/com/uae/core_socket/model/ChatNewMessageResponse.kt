package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ChatNewMessageResponse(
    val message: ReceivedMessage?,
    val timestamp: String?
) {
    @Keep
    data class ReceivedMessage(
        val chatId: String?,
        val createdAt: String?,
        val fileName: String?,
        @SerializedName("_id")
        val id: String?,
        val isRead: Boolean?,
        val message: String?,
        val messageType: String?,
        val mimeType: String?,
        val role: Int?,
        val size: String?,
        val updatedAt: String?,
        val url: String?,
        val user: User?,
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
    ) {
        @Keep
        data class User(
            val fullname: String?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?
        )
    }
}