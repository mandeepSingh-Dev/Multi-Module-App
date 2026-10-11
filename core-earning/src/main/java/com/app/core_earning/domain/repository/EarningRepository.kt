package com.app.core_earning.domain.repository

import com.app.core_earning.model.response.EarningDetailsResponse
import com.app.core_earning.model.response.EarningTrendResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface EarningRepository {
    fun getEarningTrendDetails(period: String?): Flow<NetworkResult<EarningTrendResponse>>
    fun getEarningDetails(period: String?): Flow<NetworkResult<EarningDetailsResponse>>
}

