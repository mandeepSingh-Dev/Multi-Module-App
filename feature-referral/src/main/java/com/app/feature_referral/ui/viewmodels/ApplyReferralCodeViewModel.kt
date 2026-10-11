package com.app.feature_referral.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.feature_referral.domain.ReferralAllUseCases
import com.app.feature_referral.ui.events.ApplyReferralScreenEvents
import com.app.feature_referral.ui.states.ApplyReferralScreenState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class ApplyReferralCodeViewModel @Inject constructor(val referralAllUseCases: ReferralAllUseCases): BaseViewModel<ApplyReferralScreenState>(ApplyReferralScreenState()){



    init {
        referralViewed()
    }


    fun applyReferralCode(){
        val refCode = uiState.value?.referralCode

        referralAllUseCases.referralApplyUseCase(referralCode = refCode).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false) }
                    onEvent(ApplyReferralScreenEvents.ReferralApplied)
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }



    fun referralViewed(){

        referralAllUseCases.referralViewedUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                },
                onSuccess = {
                },
                onError = {
                }
            )
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }





}