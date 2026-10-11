package com.app.feature_withdraw.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BeanPacksListResponse(
    val count: Int?,
    val `data`: List<BeanPackData?>?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class BeanPackData(
        val beans: Int?,
        @SerializedName("_id")
        val id: String?,
        val price: String?,
        val priceAmount: Int?,
        @Transient
        val isSelected : Boolean = false
    )
}