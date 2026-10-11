package com.app.core_auth.remote

import com.app.core_auth.model.requestBody.ChangePasswordRequestBody
import com.app.core_auth.model.requestBody.LoginRequestBody
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.LoginResponse
import com.app.core_auth.model.response.OTPResponse
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_auth.model.response.SignUpResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    @POST(EndPoints.Auth.LOGIN)
    suspend fun login(@Body loginRequestBody: LoginRequestBody?) : Response<LoginResponse>

    @POST(EndPoints.Auth.OTP_VERIFY)
    suspend fun verifyOtp(@Body body: Map<String, String?>?) : Response<OTPResponse>

    @GET(EndPoints.Auth.PROFILE)
    suspend fun  getProfile() : Response<ProfileResponse>

    @PUT(EndPoints.Auth.PROFILE)
    suspend fun  setProfile(@Body profileRequestBody: ProfileRequestBody?) : Response<ProfileResponse>

    @POST(EndPoints.Auth.SIGN_UP)
    suspend fun  signUp(@Body profileRequestBody: ProfileRequestBody?) : Response<SignUpResponse>

    @POST(EndPoints.Auth.LOGOUT)
    suspend fun logout() : Response<ApiResponse2>

    @POST(EndPoints.Auth.PASSWORD)
    suspend fun  changePassword(@Body body : ChangePasswordRequestBody?) : Response<ApiResponse2>


    @POST(EndPoints.Auth.PASSWORD)
    suspend fun  resetPassword(@Body body : Map<String, String>) : Response<ApiResponse2>



}