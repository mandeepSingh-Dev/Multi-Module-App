package com.app.feature_home.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.fromJson
import com.app.feature_home.ui.events.ProfileSettingsScreenEvents
import com.app.feature_home.ui.states.ProfileSettingsScreenState
import com.uae.core_network.networkUtils.getCallbacks
import com.uae.core_network.networkUtils.networkCodeError
import com.uae.core_socket.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val authAllUseCases: AuthAllUseCases,
    private val userManager: UserManager,
) : BaseViewModel<ProfileSettingsScreenState>(
    ProfileSettingsScreenState()
) {

    fun getProfile() = viewModelScope.launch {
        val profileData = userManager.getUserDataString()?.fromJson<ProfileResponse.UserProfileData>()
        val userType = userManager.getUserType()
        updateState { state ->
            state.copy(profileData = profileData)
        }
        authAllUseCases.getProfileUseCase().collectLatest { networkResult ->
            networkResult.getCallbacks(
                onLoading = {},
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            profileData = it.data?.data,
                        )
                    }
                },
                onError = {
                    errorEvent(it.error)
                }
            )

        }

    }

    fun logout() {
        authAllUseCases.logoutUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isLoading = true)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    SocketIOManager.clear()
                    onEvent(ProfileSettingsScreenEvents.OnLogout(it.message))
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

}