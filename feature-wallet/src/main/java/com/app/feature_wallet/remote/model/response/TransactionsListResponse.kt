package com.app.feature_wallet.remote.model.response


import com.google.gson.annotations.SerializedName

data class TransactionsListResponse(
    val count: Int?,
    val `data`: List<TransactionData>?,
    val message: String?,
    val status: Int?
) {
    data class TransactionData(
        val amount: Int?,
        val balanceAfter: Int?,
        val createdAt: String?,
        val display: Display?,
        @SerializedName("_id")
        val id: String?,
        val lockedBalanceAfter: Int?,
        val meta: Meta?,
        val referenceId: String?,
        val referenceType: String?,
        val remark: String?,
        val status: String?,
        val transactionId: String?,
        val type: String?,
        val updatedAt: String?,
        val userId: String?,
        val userProfile: UserProfile?,
        @SerializedName("__v")
        val v: Int?,
        val walletId: String?
    ) {
        data class Display(
            val amount: String?,
            val duration: Any?,
            val durationSeconds: Int?,
            val title: String?,
            val type: String?
        )

        data class Meta(
            val beans: Int?,
            val coinCost: Int?,
            val giftId: String?,
            val quantity: Int?,
            val receiverId: String?
        )

        data class UserProfile(
            val id: String?,
            val name: String?,
            val profilePic: String?
        )
    }
}