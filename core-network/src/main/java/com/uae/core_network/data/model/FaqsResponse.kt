package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class FaqsResponse(
    val count: Int?,
    val `data`: List<FaqData?>?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class FaqData(
        val active: Int?,
        val category: Any?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val title: String?,
        val isExpanded : Boolean = false
    )
}