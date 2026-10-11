package com.app.feature_referral.ui.events

import com.app.core_common.base.UIEvent

interface  ApplyReferralScreenEvents  : UIEvent{

    object ReferralApplied : ApplyReferralScreenEvents
    object ReferralSkipped : ApplyReferralScreenEvents
}