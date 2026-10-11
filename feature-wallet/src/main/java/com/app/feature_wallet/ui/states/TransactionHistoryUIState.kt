package com.app.feature_wallet.ui.states

import com.app.core_common.base.UIState
import com.app.feature_wallet.remote.model.response.WalletResponse

data class TransactionHistoryUIState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val walletData: WalletResponse.WalletData? = null,
    val transactionType : String? = null,
    val fromDate : String? = null,
    val toDate : String? = null,
) : UIState