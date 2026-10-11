package com.app.feature_call.ui.states

import com.app.core_common.base.UIState
import com.uae.core_network.data.model.requestBody.ReviewRequestBody

data class FeedbackScreenState(
    val isLoading : Boolean = false,
    val reviewRequestBody: ReviewRequestBody? = ReviewRequestBody()
) : UIState