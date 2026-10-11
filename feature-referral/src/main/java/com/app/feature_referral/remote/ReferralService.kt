package com.app.feature_referral.remote

import com.app.feature_referral.remote.model.response.ReferralDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReferralService {

    @GET(EndPoints.Referral.REFERRAL)
    suspend fun getReferralDetails() : Response<ReferralDetailsResponse>


    @POST(EndPoints.Referral.REFERRAL_APPLY)
    suspend fun referralApply(@Body body : Map<String, String?> ) : Response<ApiResponse2>

    @POST(EndPoints.Referral.REFERRAL_VIEWED)
    suspend fun referralViewed() : Response<ApiResponse2>


}