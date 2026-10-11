package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ChatJoinedResponse(
    val chatId: String?,
    val timestamp: String?,
    val userId: String?
)