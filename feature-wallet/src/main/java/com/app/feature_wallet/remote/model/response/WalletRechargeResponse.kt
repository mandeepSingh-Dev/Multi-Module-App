package com.app.feature_wallet.remote.model.response


import com.google.gson.annotations.SerializedName

data class WalletRechargeResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val balance: Int?,
        val bonusCoins: Int?,
        val coinsAdded: Int?,
        val firstRechargeOffer: FirstRechargeOffer?,
        val purchasedCoins: Int?
    ) {
        data class FirstRechargeOffer(
            val discountAmount: Double?,
            val discountPercent: Int?,
            val eligible: Boolean?,
            val maxPackagePrice: Int?,
            val originalPrice: Int?,
            val payablePrice: Double?
        )
    }
}