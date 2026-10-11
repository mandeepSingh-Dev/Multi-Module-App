package com.app.feature_gift.ui.events

import com.app.core_common.base.UIEvent
import com.app.feature_gift.remote.model.response.GiftListResponse

interface GiftEvents : UIEvent {

    data class GiftSentSuccessfully( val gift: GiftListResponse.GiftData?) : GiftEvents
    data class GiftListFetchError(val msg : String? = null) : GiftEvents
    data class GiftSentError(val msg : String? = null) : GiftEvents
}