package com.app.feature_home_host.remote.model.response


import com.google.gson.annotations.SerializedName

data class BlockUsersListResponse(
    val count: Int?,
    val `data`: List<Data?>?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val blockedAt: String?,
        val countryCode: String?,
        val email: String?,
        val fullName: String?,
        @SerializedName("_id")
        val id: String?,
        val isBlock: Boolean?,
        val phone: String?,
        val profilePic: String?,
        val rating: Int?
    )
}