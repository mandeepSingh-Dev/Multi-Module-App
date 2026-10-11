package com.app.feature_auth_host.ui.screens.events

import com.app.core_auth.model.response.OTPResponse
import com.app.core_common.base.UIEvent

interface SignUpScreenEvents : UIEvent{
    object SignUpSuccess : SignUpScreenEvents
    data class OTPSuccess(val otpData : OTPResponse.OTPData?) : SignUpScreenEvents
    data class ProfileStepSuccess(val step : Int) : SignUpScreenEvents
}