package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CMSResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class Data(
        val createdAt: String?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val role: Int?,
        val type: Int?,
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}