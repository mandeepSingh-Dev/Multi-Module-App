package com.app.feature_home.ui.states

import com.app.core_common.base.UIState
import com.app.feature_home.remote.model.response.DailyStreakResponse

data class DailyStreakUIState (
    val isLoading: Boolean = true,
    val dailyStreakData: DailyStreakResponse.DailyStreakData? = null
): UIState