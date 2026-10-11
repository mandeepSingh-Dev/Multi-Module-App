package com.app.feature_wallet.domain

import com.app.feature_wallet.domain.repository.WalletRepository
import com.app.feature_wallet.remote.model.response.CoinPacksListResponse
import com.app.feature_wallet.remote.model.response.WalletRechargeResponse
import com.app.feature_wallet.remote.model.response.WalletResponse
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class WalletAllUseCases @Inject constructor(
    val GetWalletUseCase: GetWalletUseCase,
    val getCoinPacksUseCase: GetCoinPacksUseCase,
    val rechargeWalletUseCase: RechargeWalletUseCase,
)
class GetWalletUseCase @Inject constructor(private val walletRepository: WalletRepository) {
    operator fun invoke(): Flow<NetworkResult<WalletResponse>> {
        return walletRepository.getWallet()
    }
}

class GetCoinPacksUseCase @Inject constructor(private val walletRepository: WalletRepository) {
    operator fun invoke(): Flow<NetworkResult<CoinPacksListResponse>> {
        return walletRepository.getCoinPacks()
    }
}
class RechargeWalletUseCase @Inject constructor(private val walletRepository: WalletRepository) {
    operator fun invoke(coinPackageId : String?): Flow<NetworkResult<WalletRechargeResponse>> {
        return walletRepository.rechargeWallet(coinPackageId = coinPackageId)
    }
}
