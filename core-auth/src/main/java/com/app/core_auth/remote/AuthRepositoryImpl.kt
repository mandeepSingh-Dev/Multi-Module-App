package com.app.core_auth.remote

import android.util.Log
import com.app.core_auth.domain.repository.AuthRepository
import com.app.core_auth.model.requestBody.ChangePasswordRequestBody
import com.app.core_auth.model.requestBody.LoginRequestBody
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.LoginResponse
import com.app.core_auth.model.response.OTPResponse
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_auth.model.response.SignUpResponse
import com.app.core_common.UserManager
import com.app.core_common.utils.toJson
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : AuthRepository {

    override fun login(loginRequestBody: LoginRequestBody?): Flow<NetworkResult<LoginResponse>> {
        return try {
            networkHelper.executeWithFlow<LoginResponse>(
                call = {
                    authService.login(loginRequestBody)
                },
                onSuccess = { response ->
                    val token = response.body()?.data?.token
                    Log.d("kdvbndjbnd", token.toString())
                    userManager.saveUserToken(token = token)
                    null
                },
                onError = {
                    Log.d("kfnbknfbf", it.toString())
                },
            )
        } catch (e: Exception) {
            Log.d("kfnbknfbf", e.message.toString())
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }


    override fun signUp(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<SignUpResponse>> {
        return try {
            networkHelper.executeWithFlow<SignUpResponse>(
                call = {
                    authService.signUp(profileRequestBody = profileRequestBody)
                },
                onSuccess = { response ->
                    val token = response.body()?.data?.token
                    userManager.saveUserToken(token = token)
                    null
                },
                onError = {
                    Log.d("kfnbknfbf", it.toString())
                },
            )
        } catch (e: Exception) {
            Log.d("kfnbknfbf", e.message.toString())
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun verifyOTP(
        otp: String?,
        isEmail: Boolean
    ): Flow<NetworkResult<OTPResponse>> {
        return try {
            networkHelper.executeWithFlow<OTPResponse>(
                call = {
                    authService.verifyOtp(
                        body = hashMapOf(
                            if (isEmail) {
                                ApiConstants.emailOtp
                            } else {
                                ApiConstants.phoneOtp
                            } to otp
                        )
                    )
                },
                onSuccess = { response ->
                    val token = response.body()?.data?.token
                    userManager.saveUserToken(token = token)
                    userManager.saveUserType(userType = response.body()?.data?.role)
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun getProfile(): Flow<NetworkResult<ProfileResponse>> {
        return try {
            networkHelper.executeWithFlow<ProfileResponse>(
                call = {
                    authService.getProfile()
                },
                onSuccess = { response ->
                    val userDataJson = response.body()?.data.toJson()
                    userManager.saveUserData(userDataJson)
                    userManager.saveUserType(userType = response.body()?.data?.role)

                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun setProfile(profileRequestBody: ProfileRequestBody?): Flow<NetworkResult<ProfileResponse>> {
        return try {
            networkHelper.executeWithFlow<ProfileResponse>(
                call = {
                    authService.setProfile(profileRequestBody = profileRequestBody)
                },
                onSuccess = { response ->
                    val userDataJson = response.body()?.data.toJson()
                    userManager.saveUserData(userDataJson)
                    userManager.saveUserType(userType = response.body()?.data?.role)
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun logout(): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    authService.logout()
                },
                onSuccess = { response ->
                    userManager.clearLocalData()
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun changePassword(changePasswordRequestBody: ChangePasswordRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    authService.changePassword(body = changePasswordRequestBody)
                },
                onSuccess = { response ->
                    userManager.clearLocalData()
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun resetPassword(password : String): Flow<NetworkResult<ApiResponse2>> {
        val body = hashMapOf(ApiConstants.password to password)
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    authService.resetPassword(body = body)
                },
                onSuccess = { response ->
                    userManager.clearLocalData()
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}
