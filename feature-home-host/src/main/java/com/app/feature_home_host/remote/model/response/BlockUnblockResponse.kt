package com.app.feature_home_host.remote.model.response


import com.google.gson.annotations.SerializedName

data class BlockUnblockResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val id: String?,
        val isBlock: Boolean?
    )
}