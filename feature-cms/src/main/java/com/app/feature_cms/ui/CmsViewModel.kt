package com.app.feature_cms.ui

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class CmsViewModel @Inject constructor(private val commonAllUseCases: CommonAllUseCases) :
    BaseViewModel<CMSUIState>(CMSUIState()) {

    fun getCMS(type: Int, role: Int) {
        commonAllUseCases.getCmsUseCase(
            type = type,
            role = role
        ).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false,
                        content = it.data?.data?.description
                        ) }

                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }


}