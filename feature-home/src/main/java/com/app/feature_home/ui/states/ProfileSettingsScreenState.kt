package com.app.feature_home.ui.states

import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState

data class ProfileSettingsScreenState(
    val isLoading : Boolean = false,
    val profileData : ProfileResponse.UserProfileData? = null,
 ) : UIState