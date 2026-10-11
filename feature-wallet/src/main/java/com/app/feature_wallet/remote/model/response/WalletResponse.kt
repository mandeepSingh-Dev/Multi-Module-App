package com.app.feature_wallet.remote.model.response


data class WalletResponse(
    val count: Int?,
    val `data`: WalletData?,
    val message: String?,
    val status: Int?
) {
    data class WalletData(
        val balance: Int? = null,
        val bonusCoins: Int?= null,
        val purchasedCoins: Int?= null
    )
}