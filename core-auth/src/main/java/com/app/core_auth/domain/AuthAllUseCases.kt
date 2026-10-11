package com.app.core_auth.domain

import com.app.core_auth.domain.repository.AuthRepository
import com.app.core_auth.model.requestBody.ChangePasswordRequestBody
import com.app.core_auth.model.requestBody.LoginRequestBody
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.LoginResponse
import com.app.core_auth.model.response.OTPResponse
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_auth.model.response.SignUpResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class AuthAllUseCases @Inject constructor(
    val loginUseCase : LoginUseCase,
    val otpVerifyUseCase : OTPVerifyUseCase,
    val getProfileUseCase : GetProfileUseCase,
    val setProfileUseCase : SetProfileUseCase,
    val logoutUseCase : LogoutUseCase,
    val signUpUseCase : SignUpUseCase,
    val changePasswordUseCase : ChangePasswordUseCase,
    val resetPasswordUseCase : ResetPasswordUseCase,
)

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(loginRequestBody: LoginRequestBody?): Flow<NetworkResult<LoginResponse>>? {
        return authRepository.login(loginRequestBody)
    }
}
class OTPVerifyUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(otp : String?, isEmail : Boolean = false): Flow<NetworkResult<OTPResponse>> {
        return authRepository.verifyOTP(otp, isEmail = isEmail)
    }
}
class GetProfileUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<NetworkResult<ProfileResponse>> {
        return authRepository.getProfile()
    }
}

class SetProfileUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<ProfileResponse>> {
        return authRepository.setProfile(profileRequestBody = profileRequestBody)
    }
}

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<NetworkResult<ApiResponse2>> {
        return authRepository.logout()
    }
}
class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<SignUpResponse>> {
        return authRepository.signUp(profileRequestBody = profileRequestBody)
    }
}
class ChangePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(changePasswordRequestBody: ChangePasswordRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return authRepository.changePassword(changePasswordRequestBody = changePasswordRequestBody)
    }
}
class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<SignUpResponse>> {
        return authRepository.signUp(profileRequestBody = profileRequestBody)
    }
}