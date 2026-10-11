package com.app.feature_change_password.ui.state

import com.app.core_common.base.UIState
import com.app.feature_change_password.ui.PasswordValidation

data class ChangePasswordUIState(
    val isLoading: Boolean = false,
    val currentPwdError : String? = null,
    val newPwdError : String? = null,
    val confirmPwdError : String? = null,
    val passwordValidation: PasswordValidation? = null
) : UIState