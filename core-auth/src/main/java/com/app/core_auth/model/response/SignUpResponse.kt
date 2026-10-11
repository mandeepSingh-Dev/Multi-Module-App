package com.app.core_auth.model.response


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val token: String?
    )
}