package com.app.feature_auth_host.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.requestBody.LoginRequestBody
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.enums.UserType
import com.app.feature_auth_host.ui.screens.events.LoginScreenEvent
import com.app.feature_auth_host.ui.states.LoginUIState
import com.uae.core_network.networkUtils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.internal.http.hasBody

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authAllUseCases: AuthAllUseCases
) : BaseViewModel<LoginUIState>(LoginUIState()) {


    fun login(countryCode: String) {


        val body = uiState.value?.loginRequestBody?.copy(countryCode = countryCode, role = UserType.HOST.value)
        val error = validateFields(body)
        updateState { state ->
            state.copy(phoneNumError = error)
        }

        if (error.isNullOrEmpty()) {

            authAllUseCases.loginUseCase(body)?.onEach { networkResult ->
                Log.d("kfbnkfnbkfnb", networkResult.toString())
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state.copy(isLoading = true)
                        }
                    }

                    is NetworkResult.Success -> {
                        updateState { state ->
                            state.copy(isLoading = false)
                        }
                        onEvent(LoginScreenEvent.ShowHideOtpView(isShow = true))
                    }

                    is NetworkResult.Error -> {

                        updateState { state ->
                            state.copy(isLoading = false)
                        }
                        Log.d("fbnnbjfnbf", networkResult.error.toString())
                        errorEvent(networkResult.error)
                    }
                }
            }?.launchIn(viewModelScope)
        }
    }


    fun verifyOTP(otp : String?) {

        authAllUseCases.otpVerifyUseCase(otp = otp).onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {
                    updateState { state ->
                        state.copy(isLoading = true, otpError = null)
                    }
                }
                is NetworkResult.Success -> {
                    onEvent(LoginScreenEvent.OTPSuccess(
                        otpData = networkResult.data?.data,
                        countryCode = uiState.value?.loginRequestBody?.countryCode,
                        phone = uiState.value?.loginRequestBody?.phone
                    ))
                    updateState { state ->
                        state.copy(isLoading = false, otpError = null)
                    }
                }
                is NetworkResult.Error -> {
                    errorEvent(networkResult.error)
                    updateState { state ->
                        state.copy(isLoading = false, otpError = networkResult.error)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validateFields(body: LoginRequestBody?): String? {

        return if (body?.phone.isNullOrEmpty()) {
            "Please enter your mobile number"
        } else if ((body?.phone?.length ?: 0) < 10) {
            "Please enter correct number"
        } else {
            null
        }
    }


}


