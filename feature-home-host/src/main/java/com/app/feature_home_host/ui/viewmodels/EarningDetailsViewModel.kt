package com.app.feature_home_host.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.enums.PeriodTypes
import com.app.core_common.utils.fromJson
import com.app.core_earning.domain.EarningAllUseCases
import com.app.feature_home_host.ui.states.EarningDetailsScreenUIState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class EarningDetailsViewModel @Inject constructor(
    private val earningAllUseCases: EarningAllUseCases,
    private val authAllUseCases: AuthAllUseCases,
    private val usManager : UserManager) :
    BaseViewModel<EarningDetailsScreenUIState>(EarningDetailsScreenUIState()) {

        var earningTrendDetailsJob : Job? = null
        var earningDetailsJob : Job? = null


    init {
        getProfileData()
        getEarningDetails(PeriodTypes.Month.value)
        getEarningTrendDetails(PeriodTypes.Day.value)
    }

    fun getEarningDetails(period: String?,isRefreshing : Boolean = false) {
        earningDetailsJob?.cancel()
        earningDetailsJob =    earningAllUseCases.getEarningDetailsUseCase(period = period)?.onEach { networkResult ->
             networkResult.getCallbacks(
                 onLoading = {
                     updateState { state ->
                         state.copy(isEarningDetailsLoading = !isRefreshing, isRefreshing = isRefreshing)
                     }
                 },
                 onSuccess = {
                     updateState { state ->
                         state.copy(
                             isEarningDetailsLoading = false,
                             isRefreshing = false,
                             earningDetails = it.data?.data
                         )
                     }
                 },
                 onError = {
                     updateState { state ->
                         state.copy(
                             isEarningDetailsLoading = false,
                             isRefreshing = false
                         )
                     }
                     errorEvent(it.error)
                 }
             )
         }?.launchIn(viewModelScope)
    }


    fun getEarningTrendDetails(period: String?, isRefreshing : Boolean = false) {
        earningTrendDetailsJob?.cancel()
        earningTrendDetailsJob = earningAllUseCases.getEarningTrendDetailsUseCase(period = period)?.onEach { networkResult ->
             networkResult.getCallbacks(
                 onLoading = {
                     updateState { state ->
                         state.copy(isEarningTrendDetailsLoading = !isRefreshing, isRefreshing = isRefreshing)
                     }
                 },
                 onSuccess = {
                     updateState { state ->
                         state.copy(
                             isEarningTrendDetailsLoading = true,
                             isRefreshing = false,
                             earningTrendDetails = it.data?.data
                         )
                     }
                 },
                 onError = {
                     updateState { state ->
                         state.copy(
                             isEarningTrendDetailsLoading = true,
                             isRefreshing = false
                         )
                     }
                     errorEvent(it.error)
                 }
             )
         }?.launchIn(viewModelScope)
    }

    fun getProfileData() {
        viewModelScope.launch {
            val profileData =
                usManager.getUserDataString().fromJson<ProfileResponse.UserProfileData>()
            Log.d("fkbnfjbnf", profileData?.isOnline.toString())
            updateState { state ->
                state.copy(profileData = profileData)
            }
            authAllUseCases.getProfileUseCase().onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        val profileData = it.data?.data
                        updateState { state ->
                            state.copy(
                                profileData = profileData,
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



}