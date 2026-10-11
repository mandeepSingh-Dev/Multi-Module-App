package com.app.feature_home.ui.states

import com.app.core_common.base.UIState
import com.app.feature_gift.remote.model.response.GiftListResponse
 import com.app.feature_home.remote.model.response.HostDetailsData

data class HostDetailsScreenUIState(
    val isLoading : Boolean = false,
    val isBlockLoading : Boolean = false,
    val isGiftsListLoading : Boolean = false,
    val isGiftSending : Boolean = false,
    val giftsList: List<GiftListResponse.GiftData?>? = emptyList(),
    val hostDetailsData: HostDetailsData? = HostDetailsData()
) : UIState