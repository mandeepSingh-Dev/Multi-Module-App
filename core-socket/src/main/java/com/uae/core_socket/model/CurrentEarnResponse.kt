package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CurrentEarnResponse(
    val beans: Int?,
    val callId: String?,
    val callType: String?,
    val code: String?,
    val completedMinutes: Int?,
    val currency: String?,
    val earnedAt: String?,
    val event: String?,
    val hostId: String?,
    val totalCallBeans: Int?
)