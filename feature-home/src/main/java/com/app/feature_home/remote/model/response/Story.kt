package com.app.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class Story(
    val active: Int?,
    val createdAt: String?,
    val expiresAt: String?,
    @SerializedName("_id")
    val id: String?,
    val mediaType: String?,
    val views: Int?,
    val viewsFormatted: String?,
    val overlays: List<Any?>?,
    val updatedAt: String?,
    val url: String?,
    val user: String?,
    @SerializedName("__v")
    val v: Int?
)