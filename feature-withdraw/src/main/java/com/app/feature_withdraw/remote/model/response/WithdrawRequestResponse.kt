package com.app.feature_withdraw.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class WithdrawRequestResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class Data(
        val beans: Int?,
        val completed: Boolean?,
        val faceVerified: Boolean?,
        val mobileVerified: Boolean?,
        val status: String?,
        val transaction: Transaction?,
        val verificationRequired: Boolean?,
        val voiceVerified: Boolean?,
        val withdrawalCount: Int?
    ) {
        @Keep
        data class Transaction(
            val amount: Int?,
            val balanceAfter: Int?,
            val createdAt: String?,
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
            @SerializedName("__v")
            val v: Int?,
            val walletId: String?
        ) {
            @Keep
            data class Meta(
                val amount: Int?,
                val beansAfter: Int?,
                val currency: String?,
                val frequencyPeriodDays: Int?,
                val minimumBeans: Int?,
                val payoutMilestone: Int?
            )
        }
    }
}