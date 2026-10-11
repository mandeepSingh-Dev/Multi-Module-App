package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class GiftReceivedResponseX(
    val beans: Int?,
    val coins: Int?,
    val gift: Gift?,
    val partnerBeans: Int?,
    val quantity: Int?,
    val receiverId: String?,
    val senderId: String?,
    val transactionId: String?
) {
    @Keep
    data class Gift(
        val animation: Any?,
        val animationEffect: String?,
        val coinCost: Int?,
        val createdAt: String?,
        @SerializedName("_id")
        val id: String?,
        val image: String?,
        val isActive: Boolean?,
        val name: String?,
        val order: Int?,
        val tierId: String?,
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}