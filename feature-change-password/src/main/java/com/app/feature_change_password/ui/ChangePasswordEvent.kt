package com.app.feature_change_password.ui

import com.app.core_common.base.UIEvent

interface ChangePasswordEvent : UIEvent{

    data class PwdChangedSuccessfully(val msg : String?) : ChangePasswordEvent
}