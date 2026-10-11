package com.app.feature_home.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.fromJson
import com.app.core_story.domain.usecases.StoryAllUseCases
import com.app.feature_block.domain.BlockAllUseCases
import com.app.feature_home.domain.HomeUserAllUseCases
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.events.HomeUserScreenEvents
import com.app.feature_home.ui.states.HomeUserScreenState
import com.app.feature_wallet.domain.WalletAllUseCases
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import com.uae.core_socket.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@HiltViewModel
class HomeUserViewModel @Inject constructor(
    private val authAllUseCases: AuthAllUseCases,
    private val homeUserAllUseCases : HomeUserAllUseCases,
    private val storyAllUseCases : StoryAllUseCases,
     private val walletAllUseCases: WalletAllUseCases,
    private val blockAllUseCases: BlockAllUseCases,
    private val usM : UserManager,
) : BaseViewModel<HomeUserScreenState>(HomeUserScreenState()) {


    init {
        getProfileData()
    }

    fun connectToSocket(){
        viewModelScope.launch {
            usM.getUserToken().let {
                SocketIOManager.connect(authToken = it ){
                    Log.d("FNBJNBFJBF", "Connected")
                }
            }
        }
    }

    fun getProfileData(){
        viewModelScope.launch {
            val profileData = usM.getUserDataString().fromJson<ProfileResponse.UserProfileData>()
            updateState { state ->
                state.copy(profileData = profileData)
            }
            authAllUseCases.getProfileUseCase().onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        val profileData = it.data?.data
                        updateState { state ->
                            state.copy(profileData = profileData)
                        }
                    },
                    onError = {
                        Log.d("kfnbjkfnbjf", it.error.toString())
                    }
                )
            }.launchIn(this)
        }
    }


    fun getHomeData(isRefresh : Boolean = false){

        homeUserAllUseCases.getHomeDataUseCase().onEach {networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isLoading = (uiState.value?.homeUserData == null && !isRefresh),isRefreshing = isRefresh )
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isLoading = false,
                            isRefreshing = false,
                            homeUserData = it.data?.data,
                            onlineNow = it.data?.data?.onlineNow,
                            recomended = it.data?.data?.recomended)
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(isLoading = false,
                            isRefreshing = false)
                    }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }
    fun getStoriesList(isRefresh : Boolean = false){

        storyAllUseCases.getStoriesUseCase().onEach { networkResult ->
            Log.d("dnvkdnvkdvnd", networkResult.toString())
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isStoriesLoading = (uiState.value?.storiesList == null && !isRefresh),isRefreshing = isRefresh )
                    }
                },
                onSuccess = {

                    Log.d("fvnkfbnf", it.data?.usersList.toString())
                    updateState { state ->
                        state.copy(
                            isStoriesLoading = false,
                            isRefreshing = false,
                            storiesList = it.data?.usersList)
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(
                            isStoriesLoading = false,
                            isRefreshing = false)
                    }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }

    fun changeOnlineNowMenuVisibility(item : HostData?){

        updateState { state ->
            val mList = state.onlineNow?.toMutableList()
            val index = mList?.indexOfFirst { it?.id == item?.id } ?: -1
            if (index != -1) {
                mList?.set(
                    index,
                    item?.copy(
                        isMenuOpen = !item.isMenuOpen
                    )
                )
            }
            state.copy(onlineNow = mList)
        }
    }
    fun changeOnlineNowCallControllerVisibility(item : HostData?){
        updateState { state ->
            val mList = state.onlineNow?.toMutableList()
            val index = mList?.indexOfFirst { it?.id == item?.id } ?: -1
            if (index != -1) {
                mList?.set(
                    index,
                    item?.copy(isCallControllerViewOpen = !item.isCallControllerViewOpen)
                )
            }
            state.copy(onlineNow = mList)
        }
    }

    fun getWallet(isRefreshing : Boolean = false){
        walletAllUseCases.GetWalletUseCase().onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isWalletLoading = !isRefreshing)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isWalletLoading = false,
                            walletData = it.data?.data)
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(isWalletLoading = false)
                    }
                }
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
                    messageEvent(it.data?.message)
                    updateState { state ->
                        state.copy(isBlockLoading = false)
                    }
                    removeFromOnlineNowList(id = id)
                    onEvent(HomeUserScreenEvents.BlockSuccessfully)
                },
                onError = {
                    updateState { state -> state.copy(isBlockLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }

    fun removeFromOnlineNowList(id : String?){
        updateState { state ->
            val mListOnlineNow = state.onlineNow?.toMutableList()
            val mListRecommended = state.recomended?.toMutableList()
            mListOnlineNow?.removeIf { it?.id == id }
            mListRecommended?.removeIf { it?.id == id }
            state.copy(onlineNow = mListOnlineNow, recomended = mListRecommended)
        }

    }


}


