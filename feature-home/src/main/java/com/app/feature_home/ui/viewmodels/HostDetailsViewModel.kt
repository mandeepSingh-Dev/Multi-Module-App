package com.app.feature_home.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.feature_block.domain.BlockAllUseCases
import com.app.feature_gift.domain.GiftAllUseCases
import com.app.feature_gift.remote.model.requestBody.GiftSendRequestBody
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.app.feature_gift.ui.events.GiftEvents
import com.app.feature_home.domain.HomeUserAllUseCases
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.events.HostDetailsScreenEvents
import com.app.feature_home.ui.states.HostDetailsScreenUIState
import com.uae.core_network.data.model.FavouriteUnFavResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HostDetailsViewModel @Inject constructor(
    private val homeUserAllUseCases: HomeUserAllUseCases,
    private val commonAllUseCases: CommonAllUseCases,
    private val blockAllUseCases: BlockAllUseCases,
    private val giftAllUseCases: GiftAllUseCases,
    ) :
    BaseViewModel<HostDetailsScreenUIState>(HostDetailsScreenUIState()) {

        init {
            getGiftsList()
        }

    private val _isFavourite = MutableStateFlow(mutableStateOf(false))
    val isFavourite = _isFavourite.asStateFlow()

    var isHostVisitingTracked = false

    fun getHostDetailsData(id: String?) {
        updateState { state ->
            state.copy(isLoading = true)
        }
        homeUserAllUseCases.getHostDetailsUseCase(id = id).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isLoading = true)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isLoading = false,
                            hostDetailsData = it.data?.data
                        )
                    }

                    trackVisitor(id = it.data?.data?.user?.id)
                },
                onError = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }

    fun updateDataWithExistingDetails(
        fullName: String?,
        profilePic: String?,
        rating: Double?,
        verified: Boolean?,
        gender: Int?,
        id: String?,
        chatID: String?,
    ) {

        updateState { state ->
            state.copy(
                hostDetailsData = state.hostDetailsData?.copy(
                    user = (state.hostDetailsData.user ?: HostData()).copy(
                        chatId = chatID,
                        fullName = fullName,
                        gender = gender,
                        id = id,
                        isVerified = verified,
                        profilePic = profilePic,
                        rating = rating
                    )
                )
            )
        }
    }


    var favouriteJob: Job? = null
    fun updateFavourite(isFav: Boolean) {
        updateFavState(isFav)

        favouriteJob?.cancel()
        favouriteJob = viewModelScope.launch {
            delay(400.milliseconds)
            hitFavouriteHostApi().launchIn(this)
        }
    }

    fun updateFavState(isFav: Boolean) {
        updateState { state ->
            val data = state.hostDetailsData
            state.copy(
                hostDetailsData = data?.copy(
                    user = data.user?.copy(
                        isWishlisted = isFav
                    )
                )
            )
        }
    }

    fun hitFavouriteHostApi(): Flow<NetworkResult<FavouriteUnFavResponse>> {
        return commonAllUseCases.favouriteHostUseCase(id = uiState.value?.hostDetailsData?.user?.id)
            .onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        updateFavState(it.data?.data?.isFavourite ?: false)
                    },
                    onError = {
                        errorEvent(it.error)
                    }
                )
            }
    }

    fun getGiftsList() {
        giftAllUseCases.getGiftListUseCase().onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isGiftsListLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isGiftsListLoading = false,
                            giftsList = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state -> state.copy(isGiftsListLoading = false) }
                    onEvent(GiftEvents.GiftListFetchError(it.error))
                }
            )
        }
            .launchIn(viewModelScope)
    }

    fun sendGift(
        receiverId: String?, gift: GiftListResponse.GiftData?
    ) {

        if (receiverId.isNullOrEmpty() || gift?.id.isNullOrEmpty()) return

        val body = GiftSendRequestBody(
            giftId = gift.id,
            receiverId = receiverId
        )
        giftAllUseCases.sendGiftUseCase(giftSendRequestBody = body).onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isGiftSending = true) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isGiftSending = false) }
                    messageEvent(it.data?.message)
                    onEvent(GiftEvents.GiftSentSuccessfully(
                        gift = gift
                    ))
                },
                onError = {
                    updateState { state -> state.copy(isGiftSending = false) }
                    onEvent(GiftEvents.GiftSentError(it.error))
                }
            )
        }
            .launchIn(viewModelScope)
    }

    var trackVisitingJob : Job? = null
    fun trackVisitor(id : String?){

        if(isHostVisitingTracked) return
        trackVisitingJob?.cancel()
        trackVisitingJob = homeUserAllUseCases.trackVisitorsUseCase(id = id).onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {},
                onSuccess = {
                    isHostVisitingTracked = true
                },
                onError = {}
            )
        }.launchIn(viewModelScope)
    }

    fun block(id : String?){
        blockAllUseCases.blockUnblockUseCase(id = id).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isBlockLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isBlockLoading = false)
                    }
                    onEvent(HostDetailsScreenEvents.BlockedSuccessfully(it.data?.message))
                },
                onError = {
                    updateState { state -> state.copy(isBlockLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }


}