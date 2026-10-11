package com.app.core_earning.model.response


import androidx.annotation.Keep

@Keep
data class EarningDetailsResponse(
    val count: Int?,
    val `data`: EarningDetialsData?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class EarningDetialsData(
        val callEarnings: Int?,
        val giftEarnings: Int?,
        val growth: Growth?,
        val period: String?,
        val totalCalls: Int?,
        val totalEarnings: Int?,
        val totalTalkTime: String?
    ) {
        @Keep
        data class Growth(
            val callEarnings: GrowthData?,
            val giftEarnings: GrowthData?,
            val totalCalls: GrowthData?,
            val totalEarnings: GrowthData?,
            val totalTalkTime: GrowthData?
        ) {
            @Keep
            data class GrowthData(
                val direction: String?,
                val value: String?
            )
        }
    }
}