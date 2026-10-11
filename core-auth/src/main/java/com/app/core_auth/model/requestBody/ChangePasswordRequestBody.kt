package com.app.core_auth.model.requestBody

data class ChangePasswordRequestBody(
    val currentPassword: String?,
    val newPassword: String?
)