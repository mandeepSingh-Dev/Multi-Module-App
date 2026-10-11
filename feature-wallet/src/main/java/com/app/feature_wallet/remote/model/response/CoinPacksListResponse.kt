package com.app.feature_wallet.remote.model.response


import com.google.gson.annotations.SerializedName

data class CoinPacksListResponse(
    val count: Int?,
    val `data`: List<CoinPacksListData?>?,
    val message: String?,
    val status: Int?
) {
    data class CoinPacksListData(
        val basePrice: Number?,
        val coins: Number?,
        val discount: Number?,
        val discountType: String?,
        val firstRechargeOffer: FirstRechargeOffer?,
        @SerializedName("_id")
        val id: String?,
        val isPopular: Boolean?,
        @Transient
        val isSelected: Boolean? = false,
        val order: Number?,
        val price: Number?
    ) {
        data class FirstRechargeOffer(
            val discountAmount: Number?,
            val discountPercent: Number?,
            val eligible: Boolean?,
            val maxPackagePrice: Number?,
            val originalPrice: Number?,
            val payablePrice: Number?
        )
    }
}