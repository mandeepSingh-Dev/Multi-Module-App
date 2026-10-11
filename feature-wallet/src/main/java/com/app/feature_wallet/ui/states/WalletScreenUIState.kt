package com.app.feature_wallet.ui.states
import com.app.core_common.base.UIState
import com.app.feature_wallet.remote.model.response.WalletResponse.WalletData

data class WalletScreenUIState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val walletData: WalletData? = null,
) : UIState
