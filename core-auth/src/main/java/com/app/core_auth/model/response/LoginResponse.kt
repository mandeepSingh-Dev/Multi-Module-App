package com.app.core_auth.model.response


data class LoginResponse(
    val count: Int?,
    val `data`: LoginData?,
    val message: String?,
    val status: Int?
) {
    data class LoginData(
        val otpPending: Boolean?,
        val setProfile: Int?,
        val step: Int?,
        val token: String?
    )
}