package com.app.feature_withdraw.ui.viewmodels

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_common.base.UIState
import com.app.feature_withdraw.domain.WithdrawAllUseCases
import com.app.feature_withdraw.domain.repository.WithdrawRepository
import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse
import com.app.feature_withdraw.ui.events.WithdrawBeansScreenEvents
import com.app.feature_withdraw.ui.states.WithdrawBeanUIState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class WithdrawBeansViewModel @Inject constructor(private val withdrawAllUseCases: WithdrawAllUseCases) :
    BaseViewModel<WithdrawBeanUIState>(WithdrawBeanUIState()) {


    init {
        getBeanPackages()
        getWithdrawalSummaryDetails()
    }

    fun getBeanPackages() {

        withdrawAllUseCases.beanPacksListUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isBeansPackListLoading = true)
                    }
                },
                onSuccess = {

                    updateState { state ->
                        state.copy(
                            isBeansPackListLoading = false,
                            beanPackData = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(isBeansPackListLoading = false)
                    }
                }
            )
        }.launchIn(viewModelScope)
    }

    fun getWithdrawalSummaryDetails() {
        withdrawAllUseCases.withdrawalSummary().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isLoading = true)
                    }
                },
                onSuccess = {
                    it.data?.data
                    updateState { state ->
                        state.copy(
                            isLoading = false,
                            withdrawSummaryData = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                }
            )
        }.launchIn(viewModelScope)
    }

    fun selectBean(bean: BeanPacksListResponse.BeanPackData?) {

        updateState { state ->
            val list = state.beanPackData?.toMutableList()?.map {
                it?.copy(isSelected = if (bean?.id == it.id) !it.isSelected else false)
            }
            state.copy(
                beanPackData = list,
                selectedAmount = if (list?.any { it?.isSelected ?: false }
                        ?: false) bean?.priceAmount else null,
            )
        }
    }

    fun updateAmount(amount: String?) {
        updateState { state ->
            state.copy(selectedAmount = amount?.toIntOrNull())
        }

        updateState { state ->
            val list = state.beanPackData?.toMutableList()?.map {
                it?.copy(isSelected = it.priceAmount == amount?.toIntOrNull())
            }
            state.copy(beanPackData = list)
        }

    }


    fun checkWithdrawVerificationStatus() {
        withdrawAllUseCases.withdrawalVerificationStatus().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isWithdrawLoading = true)
                    }
                },
                onSuccess = {

                    if (it.data?.data?.verificationRequired == true) {
                        onEvent(WithdrawBeansScreenEvents.GoToVerificationScreen)
                    }else{
                        withdrawRequest()
                    }



                    updateState { state ->
                        state.copy(isWithdrawLoading = false)
                    }
                            },
                onError = {
                    updateState { state ->
                        state.copy(isWithdrawLoading = false)
                    }
                    errorEvent(it.error)
                }
            )
        }
            .launchIn(viewModelScope)
    }
    fun withdrawRequest() {
        val amount = uiState.value?.selectedAmount

        if(amount == null){
            errorEvent("Please select amount!")
            return
        }else {

            withdrawAllUseCases.withdrawUseCase(amount = amount ).onEach {
                it.getCallbacks(
                    onLoading = {
                        updateState { state ->
                            state.copy(isWithdrawLoading = true)
                        }
                    },
                    onSuccess = {
                        updateState { state ->
                            state.copy(isWithdrawLoading = false)
                        }
                        onEvent(WithdrawBeansScreenEvents.WithdrawRequestSuccess)
                    },
                    onError = {
                        updateState { state ->
                            state.copy(isWithdrawLoading = false)
                        }
                        errorEvent(it.error)
                    }
                )
            }.launchIn(viewModelScope)
        }
    }

}