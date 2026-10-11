package com.app.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_block.domain.BlockAllUseCases
import com.app.ui.events.BlockListScreenEvents
import com.app.ui.states.BlockedListScreenState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BlockListViewModel @Inject constructor(private val blockAllUseCases : BlockAllUseCases): BaseViewModel<BlockedListScreenState>(BlockedListScreenState()){

    init {
        getBlockList()
    }

    fun unblock(id : String?){
        blockAllUseCases.blockUnblockUseCase(id = id).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isBlockLoading = true) }
                },
                onSuccess = {
                    messageEvent(it.data?.message)
                    updateState { state ->
                        state.copy(isBlockLoading = false)
                    }
                    onEvent(BlockListScreenEvents.UnBlockSuccessfully)
                },
                onError = {
                    updateState { state -> state.copy(isBlockLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }


    fun getBlockList(isRefreshing : Boolean = false){
        blockAllUseCases.blockListUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = !isRefreshing, isRefreshing = isRefreshing) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isLoading = false,isRefreshing = false,
                        data = it.data?.data) }
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false,isRefreshing = false) }
                }
            )
        }.launchIn(viewModelScope)
    }




}