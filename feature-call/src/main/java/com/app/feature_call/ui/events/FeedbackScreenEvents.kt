package com.app.feature_call.ui.events

import com.app.core_common.base.UIEvent

interface FeedbackScreenEvents : UIEvent{
    data class FeedbackSubmittedSuccess(val msg : String?) : FeedbackScreenEvents
}