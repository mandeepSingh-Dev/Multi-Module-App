package com.app.feature_bottom_nav.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.fromJson
import com.app.core_fcm.ui.FCMTokenManager
import com.app.feature_bottom_nav.ui.state.HomeBottomScreenState
import com.app.feature_wallet.domain.WalletAllUseCases
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class HomeBottomViewModel @Inject constructor(
     private val authAllUseCases: AuthAllUseCases,
     private val commonAllUseCases: CommonAllUseCases,
     private val walletAllUseCases: WalletAllUseCases,
    private val usM : UserManager,
    private val fcmTokenManager : FCMTokenManager
): BaseViewModel<HomeBottomScreenState>(HomeBottomScreenState()) {


    var shouldLoadApi : Boolean = true

    init {
        viewModelScope.launch {
            fcmTokenManager.registerFCMToken()
        }

    }

    fun getProfileData(){
        viewModelScope.launch {


            val profileData = usM.getUserDataString().fromJson<ProfileResponse.UserProfileData>()

            Log.d("kfnbknbkf", profileData.toString())
            updateState { state -> state.copy(userData = profileData) }

            if(shouldLoadApi) {
                authAllUseCases.getProfileUseCase().onEach {
                    it.getCallbacks(
                        onLoading = {},
                        onSuccess = {
                            val profileData = it.data?.data
                            updateState { state ->
                                state.copy(userData = profileData)
                            }
                        },
                        onError = {
                            Log.d("kfnbjkfnbjf", it.error.toString())
                        }
                    )
                }.launchIn(this)
                shouldLoadApi = false
            }
        }
    }

    fun getUnreadNotificationCounts() {
        commonAllUseCases.getUnreadNotificationUseCase().onEach { it.getCallbacks(
            onLoading = {},
            onSuccess = {
                val count = if((it.data?.data?.count ?: 0) > 0) it.data?.data?.count else null


                updateState { state -> state.copy(notificationCount = count) }
            },
            onError = {}
        ) }.launchIn(viewModelScope)
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

}