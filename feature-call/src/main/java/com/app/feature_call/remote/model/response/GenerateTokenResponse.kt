package com.app.feature_call.remote.model.response


import com.google.gson.annotations.SerializedName

data class GenerateTokenResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val chatId: String?,
        val rtcToken: String?,
        val rtmToken: String?,
        val userId: String?
    )
}