package com.app.core_auth.model.response


import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.google.gson.annotations.SerializedName
import com.uae.core_network.data.model.Interest

data class ProfileResponse(
    val count: Int?,
    val `data`: UserProfileData?,
    val message: String?,
    val status: Int?
) {
    data class UserProfileData(
        val about: String?,
        val active: Int?,
        val address: Any?,
        val birth: String?,
        val city: String?,
        val countryCode: String?,
        val createdAt: String?,
        val delete: Int?,
        val deviceId: String?,
        val deviceType: String?,
        val email: String?,
        val emailOtp: Any?,
        val emailOtpExpiresAt: Any?,
        val emailVerified: Int?,
        val fcmToken: Any?,
        val fullName: String?,
        val gender: Int?,
        @SerializedName("_id")
        val id: String?,
//        val id: String?,
        val interest: List<Interest?>?,
        val isBlocked: Int?,
        val isOnline: Int?,
        val languages: List<String?>?,
        val lastLogin: String?,
        val lastSeen: Any?,
        val location: Location?,
        val notifyMe: Int?,
        val phone: String?,
        val phoneOtp: Any?,
        val phoneOtpExpiresAt: Any?,
        val phoneVerified: Int?,
        val preferredLanguage: String?,
        val referralPromptShown: Boolean?,
        val profilePic: String?,
        val rating: Double?,
        val ratingCount: Int?,
        val role: Int?,
        val setProfile: Int?,
        val socialType: Any?,
        val step: Int?,
        val updatedAt: String?,
        @SerializedName("user_id")
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
    ) {
        data class Location(
            val coordinates: List<Int?>?,
            val type: String?
        )

        fun toProfileRequestBody(existedData: ProfileRequestBody?): ProfileRequestBody {
            return ProfileRequestBody(
                about = this.about ?: existedData?.about,
                birthDate = this.birth ?: existedData?.about,
                email = this.email ?: existedData?.about,
                fullName = this.fullName ?: existedData?.fullName,
                gender = this.gender ?: existedData?.gender,
                interest = this.interest?.map { it?._id } ?: existedData?.interest,
                languages = this.languages ?: existedData?.languages,
                city = this.city,
                preferredLanguage = this.preferredLanguage ?: existedData?.preferredLanguage,
                profilePic = this.profilePic ?: existedData?.profilePic,
                phone = this.phone ?: existedData?.phone,
                countryCode = this.countryCode ?: existedData?.countryCode,
                setProfile = this.setProfile ?: existedData?.setProfile,
                step = this.step ?: existedData?.step,
                bankName =  existedData?.bankName,
                accountNumber = existedData?.accountNumber,
                ifscCode = existedData?.ifscCode
            )
        }
    }
}