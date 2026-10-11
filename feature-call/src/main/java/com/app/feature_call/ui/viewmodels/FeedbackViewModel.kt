package com.app.feature_call.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.feature_call.ui.events.FeedbackScreenEvents
import com.app.feature_call.ui.states.FeedbackScreenState
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val commonAllUseCases: CommonAllUseCases
) : BaseViewModel<FeedbackScreenState>(FeedbackScreenState()) {


    fun addReview() {
        commonAllUseCases.reviewUseCase(uiState.value?.reviewRequestBody).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false) }
                    onEvent(FeedbackScreenEvents.FeedbackSubmittedSuccess(it.data?.message))
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                    errorEvent(it.error)
                }
            )
        }
            .launchIn(viewModelScope)
    }


}