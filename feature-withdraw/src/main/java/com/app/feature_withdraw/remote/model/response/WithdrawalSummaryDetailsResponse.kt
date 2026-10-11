package com.app.feature_withdraw.remote.model.response


import androidx.annotation.Keep

@Keep
data class WithdrawalSummaryDetailsResponse(
    val count: Int?,
    val `data`: WithdrawSummaryData?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class WithdrawSummaryData(
        val bankDetail: BankDetail?,
        val beanToRupeeRate: String?,
        val beans: Int?,
        val rupees: Double?,
        val rupeesFormatted: String?
    ) {
        @Keep
        data class BankDetail(
            val accountDisplayName: String?,
            val accountHolderName: String?,
            val accountNumber: String?,
            val bankName: String?,
            val hasBankAccount: Boolean?,
            val ifscCode: String?
        )
    }
}