package com.app.feature_home.ui.events

import com.app.core_common.base.UIEvent
import com.app.feature_gift.remote.model.response.GiftListResponse

interface  HostDetailsScreenEvents : UIEvent{
    data class BlockedSuccessfully(val message : String?) : HostDetailsScreenEvents


    data class CallPartner(
        val chatID: String?,
        val partnerID : String?,
        val personName : String?,
        val callType : String?,
        val profilePic : String?
    ) : HomeUserScreenEvents
}

