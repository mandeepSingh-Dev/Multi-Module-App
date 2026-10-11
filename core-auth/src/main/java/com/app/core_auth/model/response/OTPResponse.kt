package com.app.core_auth.model.response


data class OTPResponse(
    val count: Int?,
    val `data`: OTPData?,
    val message: String?,
    val status: Int?
) {
    data class OTPData(
        val setProfile: Int?,
        val step: Int?,
        val role: Int?,
        val token: String?,
        val showReferralPrompt: Boolean?,
)
}