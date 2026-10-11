package com.app.feature_auth_user.ui.events

import com.app.core_auth.model.response.OTPResponse
import com.app.core_common.base.UIEvent

interface  LoginScreenEvent : UIEvent {
    data class ShowHideOtpView(val isShow : Boolean) : LoginScreenEvent
    data class OTPSuccess(val otpData: OTPResponse.OTPData?) : LoginScreenEvent
    object ContinueAsGuest : LoginScreenEvent
}