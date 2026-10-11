package com.app.feature_story.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_story.domain.usecases.StoryAllUseCases
import com.app.core_story.remote.model.response.StoriesListResponse
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class StoryViewScreenViewModel @Inject constructor(
    private val storyAllUseCases : StoryAllUseCases
): BaseViewModel<StoriesScreenUIState>(StoriesScreenUIState()) {


    init {
        getStoriesList()
    }

    private val _users = MutableStateFlow<List<StoriesListResponse.UserStoryGroup?>>(emptyList())
    val users = _users.asStateFlow()


    fun getStoriesList(isRefresh : Boolean = false){

        storyAllUseCases.getStoriesUseCase().onEach { networkResult ->
            Log.d("dnvkdnvkdvnd", networkResult.toString())
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isLoading = (uiState.value?.stories == null && !isRefresh),isRefreshing = isRefresh )
                    }
                },
                onSuccess = {

                    Log.d("fvnkfbnf", it.data?.usersList.toString())
                    updateState { state ->
                        state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            stories = it.data?.usersList)
                    }


                    val list = it.data?.usersList
                    _users.value = list ?: emptyList()
                },
                onError = {

                    updateState { state ->
                        state.copy(
                            isLoading = false,
                            isRefreshing = false)
                    }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }



}
