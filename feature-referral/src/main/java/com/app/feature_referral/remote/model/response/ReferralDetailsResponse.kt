package com.app.feature_referral.remote.model.response


import androidx.annotation.Keep

@Keep
data class ReferralDetailsResponse(
    val count: Int?,
    val `data`: ReferralDetailsData?,
    val message: String?,
    val status: Int?
) {
    @Keep
    data class ReferralDetailsData(
        val bonus: Bonus?,
        val referralCode: String?,
        val referrals: List<Referral?>?,
        val remainingLimit: Int?,
        val verifiedCount: Int?
    ) {
        @Keep
        data class Bonus(
            val amount: Int?,
            val displayAmount: String?,
            val friendLabel: String?,
            val referrerLabel: String?,
            val unit: String?
        )

        @Keep
        data class Referral(
            val bonusCoins: Int?,
            val bonusDisplay: String?,
            val joinedAt: String?,
            val referralId: String?,
            val status: String?,
            val user: User?
        ) {
            @Keep
            data class User(
                val fullName: String?,
                val profilePic: String?
            )
        }
        fun getSuccessfullReferralProgress(): Float? {
            return try{
                return (verifiedCount?.toFloat() ?: 0f).div(remainingLimit?.toFloat() ?: 0f)
            }catch (e: Exception){
                null
            }
        }
    }


}