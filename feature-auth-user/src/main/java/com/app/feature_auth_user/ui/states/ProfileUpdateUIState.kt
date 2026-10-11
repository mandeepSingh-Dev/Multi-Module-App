package com.app.feature_auth_user.ui.states

import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_common.base.UIState
import com.uae.core_network.data.model.Interest

data class ProfileUpdateUIState(
    val isLoading: Boolean = false,
    val isInterestsLoading: Boolean = false,
    val profileRequestBody: ProfileRequestBody? = ProfileRequestBody(),
    val interests: List<Interest>? = null,
) : UIState