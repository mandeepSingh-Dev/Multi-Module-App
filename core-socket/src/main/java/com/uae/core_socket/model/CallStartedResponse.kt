package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName

data class CallStartedResponse(
    val callId: String?,
    val callerId: String?,
    val receiverId: String?,
    val status: String?,
    val type: String?
)