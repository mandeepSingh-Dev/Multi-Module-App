package com.app.feature_wallet.navigation.authGraph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.WalletScreens
import com.app.feature_wallet.ui.screens.CoinsRechargeScreen
import com.app.feature_wallet.ui.screens.MyCoinsScreen
import com.app.feature_wallet.ui.screens.TransactionHistoryScreen

fun EntryProviderScope<NavKey>.walletUserGraph() {

    entry<WalletScreens.MyCoinsScreen> {
        MyCoinsScreen()
    }
    entry<WalletScreens.CoinsRechargeScreen> {
        CoinsRechargeScreen()
    }
    entry<WalletScreens.TransactionsHistoryScreen> {
        TransactionHistoryScreen()
    }
}
