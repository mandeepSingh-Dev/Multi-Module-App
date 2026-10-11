package com.uae.core_network.data.service

import com.uae.core_network.models.ApiResponse
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.GET

interface  ServerKeyService {
    @GET(EndPoints.Common.SERVER_PUBLIC_KEY)
    suspend fun getServerPublicKey() : Response<ApiResponse?>?

}