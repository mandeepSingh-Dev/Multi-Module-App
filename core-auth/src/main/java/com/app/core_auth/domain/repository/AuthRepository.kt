package com.app.core_auth.domain.repository

import com.app.core_auth.model.requestBody.ChangePasswordRequestBody
import com.app.core_auth.model.requestBody.LoginRequestBody
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.LoginResponse
import com.app.core_auth.model.response.OTPResponse
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_auth.model.response.SignUpResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

     fun login(loginRequestBody : LoginRequestBody?): Flow<NetworkResult<LoginResponse>>?

    fun verifyOTP(otp: String?, isEmail : Boolean): Flow<NetworkResult<OTPResponse>>
    fun getProfile(): Flow<NetworkResult<ProfileResponse>>
    fun setProfile(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<ProfileResponse>>
    fun logout(): Flow<NetworkResult<ApiResponse2>>
    fun signUp(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<SignUpResponse>>
    fun changePassword(changePasswordRequestBody: ChangePasswordRequestBody?): Flow<NetworkResult<ApiResponse2>>
    fun resetPassword(password: String): Flow<NetworkResult<ApiResponse2>>
}