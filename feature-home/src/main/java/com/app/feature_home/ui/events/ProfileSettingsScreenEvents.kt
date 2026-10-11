package com.app.feature_home.ui.events

import com.app.core_common.base.UIEvent

interface ProfileSettingsScreenEvents : UIEvent{
    data class  OnLogout(val message : String?) : ProfileSettingsScreenEvents
}