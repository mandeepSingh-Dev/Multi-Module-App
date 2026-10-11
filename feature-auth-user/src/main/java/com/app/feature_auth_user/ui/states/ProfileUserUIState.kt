package com.app.feature_auth_user.ui.states

import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_common.base.UIState
import com.uae.core_network.data.model.Interest
import com.uae.core_network.data.model.InterestResponse

data class ProfileUserUIState(
    val isLoading : Boolean = false,
    val profileRequestBody: ProfileRequestBody? = ProfileRequestBody(),
    val interestLoading : Boolean = false,
    val interests : List<Interest>? = null,

    ) : UIState