package com.app.feature_gift.remote.model.response

import com.google.gson.annotations.SerializedName

data class GiftListResponse(
    val count: Int?,
    val `data`: List<GiftData?>?,
    val message: String?,
    val status: Int?
) {
    data class GiftData(
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
        val tier: Tier?,
        val tierId: String?
    ) {
        data class Tier(
            val hostSplitPercent: Int?,
            val name: String?,
            val tierNumber: Int?,
            val coinRange: String?,
            val maxCoins: Number?,
            val minCoins: Number?,
        )
    }
}