package com.app.feature_wallet.remote

import android.util.Log
import com.app.core_common.UserManager
import com.app.feature_wallet.domain.repository.WalletRepository
import com.app.feature_wallet.remote.model.response.CoinPacksListResponse
import com.app.feature_wallet.remote.model.response.TransactionsListResponse
import com.app.feature_wallet.remote.model.response.WalletRechargeResponse
import com.app.feature_wallet.remote.model.response.WalletResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WalletRepositoryImpl  @Inject constructor(
    private val homeUserService: WalletService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : WalletRepository {

    override fun getWallet( ): Flow<NetworkResult<WalletResponse>> {

        return try {
            networkHelper.executeWithFlow<WalletResponse>(
                call = {
                    homeUserService.getWallet()
                       },
                onSuccess = { response ->
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
    override fun getTransactions(page : Int, limit: Int,
                                 fromDate  : String?,
                                 toDate : String?,
                                 type : String?): Flow<NetworkResult<TransactionsListResponse>> {

        Log.d("bknfkbnf", page.toString())
        return try {
            networkHelper.executeWithResponseManipulate<TransactionsListResponse>(
                call = {
                    homeUserService.getTransactions(
                        page = page,
                        limit = limit,
                        fromDate = fromDate,
                        endDate = toDate,
                        type = type,
                    )
                       },
                onSuccess = { response ->
                    response
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getCoinPacks(): Flow<NetworkResult<CoinPacksListResponse>> {

        return try {
            networkHelper.executeWithResponseManipulate<CoinPacksListResponse>(
                call = {
                    homeUserService.getCoinPacks(
                    )
                       },
                onSuccess = { response ->
                    response
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
    override fun rechargeWallet(coinPackageId : String?): Flow<NetworkResult<WalletRechargeResponse>> {

        return try {
            val body = hashMapOf(ApiConstants.coinPackageId to coinPackageId)
            networkHelper.executeWithResponseManipulate<WalletRechargeResponse>(
                call = {
                    homeUserService.rechargeWallet(
                        body = body
                    )
                       },
                onSuccess = { response ->
                    response
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
