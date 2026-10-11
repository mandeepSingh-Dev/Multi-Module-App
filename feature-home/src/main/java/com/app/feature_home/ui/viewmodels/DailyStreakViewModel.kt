package com.app.feature_home.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_home.domain.HomeUserAllUseCases
import com.app.feature_home.ui.states.DailyStreakUIState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class DailyStreakViewModel @Inject constructor(private val homeUserAllUseCases: HomeUserAllUseCases): BaseViewModel<DailyStreakUIState>(DailyStreakUIState()) {

    init {
        getDailyStreak()
    }

    fun getDailyStreak(){
        homeUserAllUseCases.getDailyStreakUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {

                    updateState { state -> state.copy(isLoading = false,
                        dailyStreakData = it.data?.data
                    ) }
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }


}