package com.uae.core_socket.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class WalletBalanceUpdateResponse(
    val bonusCoins: Int?,
    val callId: String?,
    val code: String?,
    val coinsRemaining: Int?,
    val coinsSpent: Int?,
    val event: String?,
    val purchasedCoins: Int?
)