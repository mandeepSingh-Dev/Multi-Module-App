package com.app.feature_wallet.ui.states

import com.app.core_common.base.UIState
import com.app.feature_wallet.remote.model.response.CoinPacksListResponse.CoinPacksListData
import com.app.feature_wallet.remote.model.response.WalletResponse

data class CoinRechargeUIState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isRecharging: Boolean = false,
    val coinPacksListData: List<CoinPacksListData?>? = null,
    val walletData: WalletResponse.WalletData? = null,
) : UIState