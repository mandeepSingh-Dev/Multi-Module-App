package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CallAcceptedResponse(
    val acceptedAt: String?,
    val callId: String?,
    val callerId: String?,
    val chatId: String?,
    val code: String?,
    val createdAt: String?,
    val currency: String?,
    val endedAt: Any?,
    val pricingLevel: Int?,
    val rateLabel: String?,
    val ratePerMinute: Int?,
    val receiverId: String?,
    val status: String?,
    val type: String?
)