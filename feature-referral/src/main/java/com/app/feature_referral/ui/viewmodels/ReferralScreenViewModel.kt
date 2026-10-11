package com.app.feature_referral.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.feature_referral.domain.ReferralAllUseCases
import com.app.feature_referral.ui.states.ReferralScreenUIState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class ReferralScreenViewModel @Inject constructor(private val referralAllUseCases : ReferralAllUseCases): BaseViewModel<ReferralScreenUIState>(ReferralScreenUIState()){

    init {
        getReferralDetails()
    }

    fun getReferralDetails(isRefreshing : Boolean = false){
        referralAllUseCases.getReferralDetailsUseCase().onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = !isRefreshing, isRefreshing = isRefreshing)}
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false,isRefreshing = false,
                        data = it.data?.data)}
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false,isRefreshing = false)}
                    errorEvent(it.error)
                }
            )
        }
            .launchIn(viewModelScope)
    }


}