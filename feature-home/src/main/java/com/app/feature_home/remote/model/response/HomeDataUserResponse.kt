package com.app.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class HomeDataUserResponse(
    val count: Int?,
    val `data`: HomeDataUser?,
    val message: String?,
    val status: Int?
) {
    data class HomeDataUser(
        val onlineNow: List<HostData?>?,
        val recomended: List<HostData?>?
    ) {



    }
}