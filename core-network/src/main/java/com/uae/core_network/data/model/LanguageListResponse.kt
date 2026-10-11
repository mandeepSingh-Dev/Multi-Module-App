package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName

data class LanguageListResponse(
    val count: Int?,
    val `data`: List<Language?>?,
    val message: String?,
    val status: Int?
) {
    data class Language(
        val code: String?,
        val isNative: Boolean?,
        val name: String?,
        val popularity: Int?,
        val shortLabel: String?,
        val isSelected : Boolean = false
    )
}