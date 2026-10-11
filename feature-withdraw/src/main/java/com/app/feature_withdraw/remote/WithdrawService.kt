package com.app.feature_withdraw.remote

import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse
import com.app.feature_withdraw.remote.model.response.WithdrawRequestResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalSummaryDetailsResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalVerificationStatusResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface WithdrawService {


    @GET(EndPoints.Withdrawal.WITHDRAWAL_VERIFICATION_STATUS)
    suspend fun withdrawalVerificationStatus() : Response<WithdrawalVerificationStatusResponse>

    @GET(EndPoints.Bean.BEAN_PACKAGES)
    suspend fun getBeanPackages() : Response<BeanPacksListResponse>


    @GET(EndPoints.Withdrawal.WITHDRAWAL_SUMMARY)
    suspend fun withdrawalSummary() : Response<WithdrawalSummaryDetailsResponse>


    @POST(EndPoints.Withdrawal.MOBILE_VERIFICATION_START)
    suspend fun mobileVerificationStart() : Response<ApiResponse2>


    @POST(EndPoints.Withdrawal.MOBILE_VERIFY)
    suspend fun mobileVerify(@Body body : Map<String, String?>) : Response<ApiResponse2>


    @POST(EndPoints.Withdrawal.FACE_VERIFICATION)
    suspend fun faceVerification(@Body body : Map<String, String?>) : Response<ApiResponse2>


    @POST(EndPoints.Withdrawal.FACE_VERIFICATION_COMPLETE)
    suspend fun faceVerificationComplete(@Body body : Map<String, String?>) : Response<ApiResponse2>



    @POST(EndPoints.Withdrawal.VOICE_VERIFICATION)
    suspend fun voiceVerification(@Body body : Map<String, String?>) : Response<ApiResponse2>


    @POST(EndPoints.Withdrawal.VOICE_VERIFICATION_COMPLETE)
    suspend fun voiceVerificationComplete(@Body body : Map<String, String?>) : Response<ApiResponse2>



    @POST(EndPoints.Withdrawal.WITHDRAWAL)
    suspend fun withdraw(
        @Body body : Map<String, Int?>
    ) : Response<WithdrawRequestResponse>



}