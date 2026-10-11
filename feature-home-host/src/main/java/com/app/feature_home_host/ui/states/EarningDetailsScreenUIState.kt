package com.app.feature_home_host.ui.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState
import com.app.core_common.utils.enums.PeriodTypes
import com.app.core_earning.model.response.EarningDetailsResponse
import com.app.core_earning.model.response.EarningTrendResponse

data class EarningDetailsScreenUIState(
    val isEarningDetailsLoading: Boolean = false,
    val isEarningTrendDetailsLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedEarningDetailsOption: String? = PeriodTypes.Month.value,
    val selectedEarningTrendDetailsOption: String? = PeriodTypes.Day.value,
    val earningDetails: EarningDetailsResponse.EarningDetialsData? = null,
    val earningTrendDetails: List<EarningTrendResponse.EarningTrendData?>? = null,
    val profileData: ProfileResponse.UserProfileData? = null,
) : UIState