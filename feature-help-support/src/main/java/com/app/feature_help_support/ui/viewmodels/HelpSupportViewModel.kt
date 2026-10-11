package com.app.feature_help_support.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_help_support.ui.states.HelpSupportScreenState
import com.uae.core_network.data.model.FaqsResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class HelpSupportViewModel @Inject constructor(
    private val commonAllUseCases: CommonAllUseCases
): BaseViewModel<HelpSupportScreenState>(HelpSupportScreenState()) {

    init {
        getAllFaqs()
    }


    fun getAllFaqs(){
        commonAllUseCases.getFaqsResponse().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false,
                        faqs = it.data?.data) }
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }

    fun toggleFaqExpanded(faq: FaqsResponse.FaqData?) {
        val faqId = faq?.id ?: return

        updateState { state ->
            val currentFaqs = state.faqs ?: return@updateState state

            val updatedFaqs = currentFaqs.map { item ->
                if (item?.id == faqId) {
                    item.copy(isExpanded = !(item.isExpanded ?: false))
                } else {
                    item
                }
            }
            state.copy(faqs = updatedFaqs)
        }
    }
}