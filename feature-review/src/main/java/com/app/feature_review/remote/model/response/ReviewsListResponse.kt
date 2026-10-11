package com.app.feature_review.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ReviewsListResponse(
    val count: Int?,
    val `data`: ReviewData?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class ReviewData(
        val items: List<ReviewDataItem>?,
        val limit: Int?,
        val page: Int?,
        val summary: Summary?
    ) {
        @Keep
        data class ReviewDataItem(
            val call: Call?,
            val comment: String?,
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            val rating: Int?,
            val reviewer: Reviewer?
        ) {
            @Keep
            data class Call(
                val endedAt: Any?,
                val startedAt: String?,
                val type: String?
            )

            @Keep
            data class Reviewer(
                val fullName: String?,
                @SerializedName("_id")
                val id: String?,
                val profilePic: String?,
                val userId: String?
            )
        }

        @Keep
        data class Summary(
            val averageRating: Number?,
            val totalReviews: Int?
        )
    }
}