package com.app.feature_home.ui.events

import com.app.core_common.base.UIEvent

interface HomeSearchScreenEvents : UIEvent{

    data class CallPartner(
    val chatID: String?,
    val partnerID : String?,
    val personName : String?,
    val callType : String?,
    val profilePic : String?
    ) : HomeSearchScreenEvents
}