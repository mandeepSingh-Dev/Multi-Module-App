package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UnreadNotificationResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class Data(
        val count: Int?
    )
}