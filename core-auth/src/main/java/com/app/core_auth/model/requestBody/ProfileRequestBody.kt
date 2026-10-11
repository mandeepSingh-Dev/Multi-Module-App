package com.app.core_auth.model.requestBody


data class ProfileRequestBody(
    val about: String? = null,
    val birthDate: String?= null,
    val email: String?= null,
    val fullName: String?= null,
    val gender: Int?= null,
    val interest: List<String?>?= null,
    val languages: List<String?>?= null,
    val preferredLanguage: String?= null,
    val profilePic: String?= null,
    val phone: String?= null,
    val countryCode: String?= null,
    val city: String?= null,
    val password: String?= null,
    val setProfile: Int?= null,
    val step: Int?= null,
    val accountHolderName: String?= null,
    val bankName: String?= null,
    val accountNumber: String?= null,
    val ifscCode: String?= null,

)

