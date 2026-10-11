package com.uae.core_network.data.model.requestBody


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ReviewRequestBody(
    val callId: String? = null,
    val comment: String?= null,
    val partnerId: String?= null,
    val rating: Int? = null
)