package com.app.feature_home_host.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.fromJson
import com.app.feature_home_host.domain.HomeHosAllUseCases
import com.app.feature_home_host.ui.states.HomeHostUiState
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import com.uae.core_socket.SocketIOManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeHostViewModel @Inject constructor(
    private val homeHosAllUseCases: HomeHosAllUseCases,
    private val authAllUseCases: AuthAllUseCases,
    private val usManager: UserManager
) : BaseViewModel<HomeHostUiState>(HomeHostUiState()) {


    private val _isOnline = MutableStateFlow<Boolean?>(null)
    val isOnline = _isOnline.asStateFlow()

    init {
        getProfileData()

        isOnline
            .debounce(500.milliseconds)
            .distinctUntilChanged()
            .filterNotNull()
            .flatMapLatest {
                toggleOnlineOffline(if(isOnline.value == true) 1 else 0)
            }
            .launchIn(viewModelScope)
    }
    fun connectToSocket(){
        viewModelScope.launch {
            usManager.getUserToken().let {
                SocketIOManager.connect(authToken = it ){
                    Log.d("FNBJNBFJBF", "Connected")
                }
            }
        }
    }
    fun getProfileData() {
        viewModelScope.launch {
            val profileData =
                usManager.getUserDataString().fromJson<ProfileResponse.UserProfileData>()
            Log.d("fkbnfjbnf", profileData?.isOnline.toString())
            updateState { state ->
                state.copy(profileData = profileData, isOnline = profileData?.isOnline == 1)
            }
            authAllUseCases.getProfileUseCase().onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        val profileData = it.data?.data
                        updateState { state ->
                            state.copy(
                                profileData = profileData,
                                isOnline = profileData?.isOnline == 1
                            )
                        }
                    },
                    onError = {
                        Log.d("kfnbjkfnbjf", it.error.toString())
                    }
                )
            }.launchIn(this)
        }
    }


    fun updateOnline(isBoolean: Boolean) {
        _isOnline.value = isBoolean
        updateState { state ->
            state.copy(isOnline = isBoolean)
        }
    }

    fun toggleOnlineOffline(isOnline : Int): Flow<NetworkResult<ApiResponse2>> {

        return homeHosAllUseCases.toggleOnlineOfflineUseCase(isOnline = isOnline).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isOnlineLoading = true)
                    }
                },
                onSuccess = {

                    updateState { state ->
                        state.copy(isOnlineLoading = false)
                    }
                },
                onError = {

                    updateState { state ->
                        state.copy(isOnlineLoading = false)
                    }
                }
            )
        }
    }


    fun getDashboardStats(isRefreshing : Boolean = false) {
        updateState { state ->
            state.copy(
                isLoading = !isRefreshing,
            )
        }
        homeHosAllUseCases.getDashboardStatsUseCase().onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(
                            isLoading = !isRefreshing,
                            isRefreshing = isRefreshing
                        )

                    }
                },
                onSuccess = {
                    viewModelScope.launch {
                        delay(2000.milliseconds)
                        updateState { state ->
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                dashboardData = it.data?.data
                            )
                        }
                    }
                },
                onError = {
                    errorEvent(it.error)
                    updateState { state ->
                        state.copy(isLoading = false,
                            isRefreshing = false,
                            )
                    }
                }
            )
        }
            .launchIn(viewModelScope)
    }

}