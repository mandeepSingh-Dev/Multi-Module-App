package com.app.feature_gift.remote.model.requestBody


import com.google.gson.annotations.SerializedName

data class GiftSendRequestBody(
    val giftId: String?,
    val receiverId: String?
)