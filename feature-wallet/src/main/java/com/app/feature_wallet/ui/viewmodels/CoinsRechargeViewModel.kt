package com.app.feature_wallet.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_wallet.domain.WalletAllUseCases
import com.app.feature_wallet.remote.model.response.CoinPacksListResponse
import com.app.feature_wallet.ui.WalletRechargeScreenEvents
import com.app.feature_wallet.ui.states.CoinRechargeUIState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class CoinsRechargeViewModel @Inject constructor(private val walletAllUseCases: WalletAllUseCases) :
    BaseViewModel<CoinRechargeUIState>(CoinRechargeUIState()) {


    init {
        getWallet()
        getCoinPackages()
    }

    fun getCoinPackages(isRefreshing: Boolean = false) {
        walletAllUseCases.getCoinPacksUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(
                            isLoading = !isRefreshing,
                            isRefreshing = isRefreshing
                        )
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isLoading = false, isRefreshing = false,
                            coinPacksListData = it.data?.data
                        )
                    }

                },
                onError = {
                    updateState { state -> state.copy(isLoading = false, isRefreshing = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }

    fun getWallet(isRefreshing: Boolean = false) {
        walletAllUseCases.GetWalletUseCase().onEach { networkResult ->

            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(
                            isLoading = !isRefreshing, isRefreshing = isRefreshing
                        )
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isLoading = false, isRefreshing = false,
                            walletData = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false, isRefreshing = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }

    fun rechargeWallet(
        coinPackageId: String?
    ) {
        walletAllUseCases.rechargeWalletUseCase(coinPackageId = coinPackageId)
            .onEach { networkResult ->

                networkResult.getCallbacks(
                    onLoading = {
                        updateState { state -> state.copy(isRecharging = true) }
                    },
                    onSuccess = {
                        updateState { state -> state.copy(isRecharging = false) }
                        onEvent(WalletRechargeScreenEvents.WalletRechargeSuccess)
                    },
                    onError = {
                        updateState { state ->
                            state.copy(
                                isRecharging = false
                            )
                        }
                        errorEvent(it.error)
                    }
                )
            }.launchIn(viewModelScope)
    }

    fun updateList(pack: CoinPacksListResponse.CoinPacksListData?) {

        updateState { state ->
            val mList = state.coinPacksListData?.toMutableList()

            val uList = mList?.map {
                if (it?.id == pack?.id) {
                    it?.copy(isSelected = !(it.isSelected ?: false))
                } else {
                    it?.copy(isSelected = false)
                }
            }

            state.copy(coinPacksListData = uList)
        }
    }

}