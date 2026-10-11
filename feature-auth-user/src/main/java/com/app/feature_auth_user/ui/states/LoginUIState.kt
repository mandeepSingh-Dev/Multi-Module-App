package com.app.feature_auth_user.ui.states

import com.app.core_auth.model.requestBody.LoginRequestBody
import com.app.core_common.base.UIState

data class LoginUIState(
    val isLoading : Boolean = false,
    val loginRequestBody: LoginRequestBody? = LoginRequestBody(),
    val phoneNumError : String? = null,
    val otpError : String? = null
) : UIState