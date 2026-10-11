package com.app.feature_notification.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationListResponse(
    val count: Int?,
    val `data`: List<NotificationData>?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class NotificationData(
        val createdAt: String?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val isRead: Boolean?,
        val notificationData: NotificationData?,
        val title: String?,
        val updatedAt: String?,
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
    ) {
        @Keep
        data class NotificationData(
            val callId: Any?,
            val chatId: String?,
            val fullName: String?,
            val giftId: String?,
            val notificationType: Int?,
            val profilePic: String?,
            val referenceId: String?,
            val referenceType: String?,
            val userId: String?
        )
    }
}