package com.app.feature_story.ui.events

import com.app.core_common.base.UIEvent

interface StoryViewScreenEvents : UIEvent{

    data class CallPartner(
    val chatID: String?,
    val partnerID : String?,
    val personName : String?,
    val callType : String?,
    val profilePic : String?
    ) : StoryViewScreenEvents
}