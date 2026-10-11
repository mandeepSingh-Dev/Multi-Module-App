package com.app.feature_home.ui.events

import com.app.core_common.base.UIEvent

interface HomeUserScreenEvents : UIEvent{

    data class CallPartner(
    val chatID: String?,
    val partnerID : String?,
    val personName : String?,
    val callType : String?,
    val profilePic : String?
    ) : HomeUserScreenEvents

    object BlockSuccessfully : HomeUserScreenEvents

}