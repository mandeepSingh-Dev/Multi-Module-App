package com.app.feature_withdraw.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.fromJson
import com.app.feature_withdraw.domain.WithdrawAllUseCases
import com.app.feature_withdraw.ui.events.IdentityVerificationScreenEvents
import com.app.feature_withdraw.ui.states.IdentityVerificationUIState
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class IdentityVerificationViewModel @Inject constructor(
    private val commonAllUseCases: CommonAllUseCases,
    private val withdrawAllUseCases: WithdrawAllUseCases,
    private val usrMgr: UserManager
) : BaseViewModel<IdentityVerificationUIState>(IdentityVerificationUIState()) {


    init {
        viewModelScope.launch {
            val userData = usrMgr.getUserDataString().fromJson<ProfileResponse.UserProfileData>()

            updateState { state ->
                state.copy(phone = "${userData?.countryCode} ${userData?.phone}")
            }
        }
    }


    fun verifyOtp(otp: String?) {
        withdrawAllUseCases.mobileVerify(otp = otp).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isOTPVerificationLoading = true)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isOTPVerificationLoading = false)
                    }
                    onEvent(IdentityVerificationScreenEvents.OTPVerifiedSuccessfully)
                },
                onError = {

                    updateState { state ->
                        state.copy(isOTPVerificationLoading = false)
                    }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }


    fun uploadImage(
        uri: String?,
        onLoading: () -> Unit,
        onError: (NetworkResult.Error<ImageUploadResponse>) -> Unit,
        onSuccess: (NetworkResult.Success<ImageUploadResponse>) -> Unit
    ) {
        commonAllUseCases.imageUploadUseCase(
            fileUri = uri,
            folderName = ""
        ).onEach {
            it.getCallbacks(
                onLoading = onLoading,
                onSuccess = onSuccess,
                onError = onError
            )
        }.launchIn(viewModelScope)
    }


    fun faceVerificationStart(uri: String?) {

        if(uri.isNullOrEmpty()) {
            errorEvent("Something went wrong, Please try again!")
            return
        }
        uploadImage(
            uri = uri,
            onLoading = {
                updateState { state -> state.copy(isFaceVerificationStartInLoading = true) }
            },
            onSuccess = {
                val url = it.data?.data?.file
                withdrawAllUseCases.faceVerification(url).onEach {
                    it.getCallbacks(
                        onLoading = {
                            updateState { state -> state.copy(isFaceVerificationStartInLoading = true) }
                        },
                        onSuccess = {
                            updateState { state -> state.copy(isFaceVerificationStartInLoading = false) }
                            onEvent(IdentityVerificationScreenEvents.ShowFaceVerificationDoneDialog(url))
                        },
                        onError = {
                            updateState { state -> state.copy(isFaceVerificationStartInLoading = false) }
                            errorEvent(it.error)
                        }
                    )
                }.launchIn(viewModelScope)
            },
            onError = {
                updateState { state -> state.copy(isFaceVerificationStartInLoading = false) }
                errorEvent(it.error)
            },
            )
    }


    fun faceVerificationCompleted(url : String?){
        withdrawAllUseCases.faceVerificationComplete(url = url).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isFaceVerificationCompleteInLoading = true)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isFaceVerificationCompleteInLoading = false)
                    }
                    onEvent(IdentityVerificationScreenEvents.OnFaceVerificationAllDone)
                },
                onError = {
                    updateState { state ->
                        state.copy(isFaceVerificationCompleteInLoading = false)
                    }
                    errorEvent(it.error)
                }
            )
        }
            .launchIn(viewModelScope)
    }


    fun voiceVerificationStart(uri: String?) {

        if(uri.isNullOrEmpty()) {
            errorEvent("Something went wrong, Please try again!")
            return
        }
        uploadImage(
            uri = uri,
            onLoading = {
                updateState { state -> state.copy(isVoiceVerificationStartInLoading = true) }
            },
            onSuccess = {
                val url = it.data?.data?.file
                withdrawAllUseCases.voiceVerification(url).onEach {
                    it.getCallbacks(
                        onLoading = {
                            updateState { state -> state.copy(isVoiceVerificationStartInLoading = true) }
                        },
                        onSuccess = {
                            updateState { state -> state.copy(isVoiceVerificationStartInLoading = false) }
                            onEvent(IdentityVerificationScreenEvents.ShowVoiceVerificationDoneDialog(url))
                        },
                        onError = {
                            updateState { state -> state.copy(isVoiceVerificationStartInLoading = false) }
                            errorEvent(it.error)
                        }
                    )
                }.launchIn(viewModelScope)
            },
            onError = {
                updateState { state -> state.copy(isVoiceVerificationStartInLoading = false) }
                errorEvent(it.error)
            },
        )
    }


    fun voiceVerificationCompleted(url : String?){
        withdrawAllUseCases.voiceVerificationComplete(url = url).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isVoiceVerificationCompleteInLoading = true)
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isVoiceVerificationCompleteInLoading = false)
                    }
                    onEvent(IdentityVerificationScreenEvents.OnVoiceVerificationAllDone)
                },
                onError = {
                    updateState { state ->
                        state.copy(isVoiceVerificationCompleteInLoading = false)
                    }
                    errorEvent(it.error)
                }
            )
        }
            .launchIn(viewModelScope)
    }



}