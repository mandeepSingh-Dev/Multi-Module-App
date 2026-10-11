package com.app.feature_wallet.remote

import com.app.feature_wallet.remote.model.response.CoinPacksListResponse
import com.app.feature_wallet.remote.model.response.TransactionsListResponse
import com.app.feature_wallet.remote.model.response.WalletRechargeResponse
import com.app.feature_wallet.remote.model.response.WalletResponse
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WalletService {
    @GET(EndPoints.Wallet.WALLET)
    suspend fun getWallet(): Response<WalletResponse>

    @GET(EndPoints.Wallet.TRANSACTIONS)
    suspend fun getTransactions(
        @Query(ApiConstants.page) page: Int?,
        @Query(ApiConstants.limit) limit: Int?,
        @Query(ApiConstants.fromDate) fromDate: String?,
        @Query(ApiConstants.endDate) endDate: String?,
        @Query(ApiConstants.type) type: String?,
    ): Response<TransactionsListResponse>

    @GET(EndPoints.Wallet.COIN_PACKAGES)
    suspend fun getCoinPacks(
    ): Response<CoinPacksListResponse>

    @POST(EndPoints.Wallet.WALLET_RECHARGE)
    suspend fun rechargeWallet(
        @Body body : Map<String, String?>
    ): Response<WalletRechargeResponse>

}