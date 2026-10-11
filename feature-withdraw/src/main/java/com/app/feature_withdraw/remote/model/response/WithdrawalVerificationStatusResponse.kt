package com.app.feature_withdraw.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class WithdrawalVerificationStatusResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class Data(
        val completed: Boolean?,
        val faceVerified: Boolean?,
        val mobileVerified: Boolean?,
        val status: String?,
        val verificationRequired: Boolean?,
        val voiceVerified: Boolean?,
        val withdrawalCount: Int?
    )
}