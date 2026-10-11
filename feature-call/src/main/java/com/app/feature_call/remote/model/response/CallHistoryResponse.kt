package com.app.feature_call.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CallHistoryResponse(
    val count: Int?,
    val `data`: List<CallHistoryData>?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class CallHistoryData(
        val callId: String?,
        val callType: String?,
        val callerId: CallerId?,
        val chatId: String?,
        val coinsSpent: Number?,
        val connectedAt: Any?,
        val createdAt: String?,
        val duration: String?,
        val durationSeconds: Int?,
        val endReason: String?,
        val endedAt: String?,
        val endedBy: String?,
        @SerializedName("_id")
        val id: String?,
        val receiverId: ReceiverId?,
        val startedAt: String?,
        val status: String?,
        val updatedAt: String?
    ) {
        @Keep
        data class CallerId(
            val fullName: String?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?
        )

        @Keep
        data class ReceiverId(
            val fullName: String?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?
        )
    }
}