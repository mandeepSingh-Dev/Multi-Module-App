package com.app.feature_wallet.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_wallet.domain.WalletAllUseCases
import com.app.feature_wallet.ui.states.WalletScreenUIState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class WalletViewModel @Inject constructor(private val walletAllUseCases: WalletAllUseCases) :
    BaseViewModel<WalletScreenUIState>(WalletScreenUIState()) {


    fun getWallet(isRefreshing: Boolean = false) {
        walletAllUseCases.GetWalletUseCase().onEach { networkResult ->

            networkResult.getCallbacks(
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

}