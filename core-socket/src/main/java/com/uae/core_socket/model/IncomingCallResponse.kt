package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName

data class IncomingCallResponse(
    val acceptedAt: Any?,
    val callId: String?,
    val caller: Caller?,
    val callerId: String?,
    val chatId: String?,
    val createdAt: String?,
    val endedAt: Any?,
    val receiverId: String?,
    val status: String?,
    val timestamp: String?,
    val type: String?
) {
    data class Caller(
        val fullName: String?,
        @SerializedName("_id")
        val id: String?,
        val profilePic: String?
    )
}