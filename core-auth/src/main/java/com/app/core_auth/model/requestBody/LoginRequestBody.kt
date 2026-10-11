package com.app.core_auth.model.requestBody


data class LoginRequestBody(
    val countryCode: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val role: Int? = null
)