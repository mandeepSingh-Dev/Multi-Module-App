package com.app.core_earning.remote

import com.app.core_earning.model.response.EarningDetailsResponse
import com.app.core_earning.model.response.EarningTrendResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EarningService {
    @GET(EndPoints.Earning.HOST_EARNING_TREND)
    suspend fun getEarningTrendResponse(
        @Query(ApiConstants.period) period : String?
    ) : Response<EarningTrendResponse>

    @GET(EndPoints.Earning.EARNING_DETAILS)
    suspend fun getEarningDetails(
        @Query(ApiConstants.period) period : String?
    ) : Response<EarningDetailsResponse>

}