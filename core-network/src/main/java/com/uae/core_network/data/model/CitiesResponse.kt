package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CitiesResponse(
    val count: Int?,
    val `data`: List<CityData?>?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class CityData(
        val city: String?,
        @SerializedName("_id")
        val id: String?,
        val latitude: String?,
        val longitude: String?
    )
}