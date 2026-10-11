package com.app.feature_cms.ui

import com.app.core_common.base.UIState

data class CMSUIState(
    val isLoading: Boolean = false,
    val content: String? = null
) : UIState