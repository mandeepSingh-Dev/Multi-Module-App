package com.app.core_fcm.model


import androidx.annotation.Keep

@Keep
data class PushNotificationData(
    val description: String?,
    val notificationData: NotificationData?,
    val title: String?,
    val userId: String?
) {
    @Keep
    data class NotificationData(
        val callId: Any?,
        val chatId: String?,
        val giftId: Any?,
        val notificationType: Int?,
        val orderId: Any?,
        val profilePic: String?,
        val referenceId: String?,
        val referenceType: String?,
        val userId: String?
    )
}