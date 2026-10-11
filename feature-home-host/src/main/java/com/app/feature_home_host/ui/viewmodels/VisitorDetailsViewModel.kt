package com.app.feature_home_host.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_home_host.domain.HomeHosAllUseCases
import com.app.feature_home_host.ui.states.VisitorDetailsState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class VisitorDetailsViewModel @Inject constructor(private val homeHosAllUseCases: HomeHosAllUseCases) :
    BaseViewModel<VisitorDetailsState>(
        VisitorDetailsState()
    ) {


    fun getVisitorDetails(visitorId: String?, isRefreshing: Boolean = false) {

        homeHosAllUseCases.getVisitorDetailsUseCase(visitorId).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(
                            isLoading = !isRefreshing,
                            isRefreshing = isRefreshing
                        )
                    }
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false, isRefreshing = false,
                        data = it.data?.data) }
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false, isRefreshing = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }


}