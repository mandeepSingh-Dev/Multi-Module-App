package com.app.feature_auth_user.ui.events

import com.app.core_common.base.UIEvent

interface ProfileScreenEvents : UIEvent{
    data class ProfileStepSuccess(val step : Int) : ProfileScreenEvents
}