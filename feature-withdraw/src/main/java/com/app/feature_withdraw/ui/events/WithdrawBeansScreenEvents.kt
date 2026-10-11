package com.app.feature_withdraw.ui.events

import com.app.core_common.base.UIEvent

interface WithdrawBeansScreenEvents : UIEvent{
    object GoToVerificationScreen : WithdrawBeansScreenEvents
    object WithdrawRequestSuccess : WithdrawBeansScreenEvents
}