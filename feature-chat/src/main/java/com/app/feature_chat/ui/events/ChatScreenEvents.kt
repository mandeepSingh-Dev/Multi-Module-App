package com.app.feature_chat.ui.events

import com.app.core_common.base.UIEvent

interface ChatScreenEvents : UIEvent{

    object BlockedSuccessfully : ChatScreenEvents
    object UnblockedSuccessfully : ChatScreenEvents
}