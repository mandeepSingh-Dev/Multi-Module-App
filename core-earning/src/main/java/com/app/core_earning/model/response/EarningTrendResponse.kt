package com.app.core_earning.model.response


import androidx.annotation.Keep

@Keep
data class EarningTrendResponse(
    val count: Int?,
    val `data`: List<EarningTrendData?>?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class EarningTrendData(
        val lable: String?,
        val value: Int?
    )
}