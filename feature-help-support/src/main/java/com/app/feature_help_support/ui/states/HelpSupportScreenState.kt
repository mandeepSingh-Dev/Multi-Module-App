package com.app.feature_help_support.ui.states

import com.app.core_common.base.UIState
import com.uae.core_network.data.model.FaqsResponse

data class HelpSupportScreenState(
    val isLoading: Boolean = false,
    val faqs: List<FaqsResponse.FaqData?>? = null
) : UIState