package com.app.feature_wallet.domain.repository
import com.app.feature_wallet.remote.model.response.CoinPacksListResponse
import com.app.feature_wallet.remote.model.response.TransactionsListResponse
import com.app.feature_wallet.remote.model.response.WalletRechargeResponse
import com.app.feature_wallet.remote.model.response.WalletResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getTransactions(
        page: Int,
        limit: Int,
        fromDate  : String?,
        toDate : String?,
        type : String?
    ): Flow<NetworkResult<TransactionsListResponse>>

    fun getWallet(): Flow<NetworkResult<WalletResponse>>
    fun getCoinPacks(): Flow<NetworkResult<CoinPacksListResponse>>
    fun rechargeWallet(coinPackageId: String?): Flow<NetworkResult<WalletRechargeResponse>>
}
