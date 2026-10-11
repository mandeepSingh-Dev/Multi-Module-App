package com.app.feature_auth_host.ui.states

import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_common.base.UIState
import com.uae.core_network.data.model.CitiesResponse
import com.uae.core_network.data.model.Interest
import com.uae.core_network.data.model.LanguageListResponse

data class SignUpScreenUIState(
    val isLoading: Boolean = false,
    val isCitiesLoading: Boolean = false,
    val isVerifyOtpLoading: Boolean = false,
    val profileRequestBody: ProfileRequestBody? = ProfileRequestBody(),
    val confirmPassword: String? = null,
    val interestLoading: Boolean = false,
    val languagesLoading: Boolean = false,
    val interests: List<Interest>? = null,
    val languages: List<LanguageListResponse.Language?>? = null,
    val otpError: String? = null,
    val citiesList: List<CitiesResponse.CityData?>? = null,
    ) : UIState