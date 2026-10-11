package com.app.feature_home_host.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class VisitorDetailsResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class Data(
        val gifts: Gifts?,
        val lastViewedAt: String?,
        val reviews: Reviews?,
        val stats: Stats?,
        val visitor: Visitor?
    ) {
        @Keep
        data class Gifts(
            val items: List<Item?>?,
            val total: Int?
        ) {
            @Keep
            data class Item(
                val coinsSpent: Int?,
                @SerializedName("_id")
                val id: String?,
                val image: String?,
                val lastSentAt: String?,
                val name: String?,
                val quantity: Int?
            )
        }

        @Keep
        data class Reviews(
            val averageRating: Number?,
            val items: List<Item?>?,
            val total: Int?
        ) {
            @Keep
            data class Item(
                val comment: String?,
                val createdAt: String?,
                @SerializedName("_id")
                val id: String?,
                val rating: Number?
            )
        }

        @Keep
        data class Stats(
            val audioCalls: Int?,
            val callDurationSeconds: Int?,
            val calls: Int?,
            val profileViews: Int?,
            val timesFavorited: Int?,
            val videoCalls: Int?
        )

        @Keep
        data class Visitor(
            val age: Int?,
            val city: String?,
            val fullName: String?,
            @SerializedName("_id")
            val id: String?,
            val isOnline: Boolean?,
            val joinedAt: String?,
            val lastSeen: String?,
            val profilePic: String?,
            val userId: String?
        )
    }
}