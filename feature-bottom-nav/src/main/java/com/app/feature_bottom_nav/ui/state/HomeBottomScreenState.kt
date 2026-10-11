package com.app.feature_bottom_nav.ui.state

import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState
import com.app.feature_wallet.remote.model.response.WalletResponse

data class HomeBottomScreenState(
    val userData: ProfileResponse.UserProfileData? = null,
    val notificationCount: Int? = null,
    val isWalletLoading: Boolean = false,
    val walletData: WalletResponse.WalletData? = null
) : UIState