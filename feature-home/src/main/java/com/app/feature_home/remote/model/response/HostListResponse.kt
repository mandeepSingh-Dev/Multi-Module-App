package com.app.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class HostListResponse(
    val count: Int?,
    val `data`: List<HostData>?,
    val message: String?,
    val status: Int?
) {

}