package com.app.feature_auth_user.ui.viewmodels

import androidx.core.text.buildSpannedString
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.BaseViewModel
import com.app.feature_auth_user.ui.events.ProfileScreenEvents
import com.app.feature_auth_user.ui.screens.ProfileStepper
import com.app.feature_auth_user.ui.states.ProfileUserUIState
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileUserViewModel @Inject constructor(
    private val authAllUseCases: AuthAllUseCases,
    private val commonAllUseCases: CommonAllUseCases,
) : BaseViewModel<ProfileUserUIState>(ProfileUserUIState()) {

    init {
        getAllInterests()
    }

    fun setProfile(
        step: Int
    ) = viewModelScope.launch{
        var body = uiState.value?.profileRequestBody

        body = if(step == ProfileStepper.INTERESTS.value){
            body?.copy(interest = uiState.value?.interests?.filter { it.isSelected }?.map { it._id },
                setProfile = 1,
                step = step)
        }else{
            body?.copy(step = step)
        }

        val error = validateFields(step, body)
        if (!error.isNullOrEmpty()) {
            errorEvent(error)
        } else {
            when {
                step == ProfileStepper.PHOTO.value -> {
                    uploadImage(fileUri = body?.profilePic, folderName = "").onEach { networkResult ->
                        when(networkResult){
                            is NetworkResult.Loading -> {
                                updateState { state ->
                                    state.copy(isLoading = true)
                                }
                            }
                            is NetworkResult.Success -> {
                                updateState { state ->
                                    state.copy(isLoading = false)
                                }
                                updateState { state ->
                                    state.copy(profileRequestBody = body?.copy(profilePic = networkResult.data?.data?.file))
                                }
                                updateProfile(step = step,uBody = body).launchIn(this)
                            }
                            is NetworkResult.Error -> {
                                updateState { state ->
                                    state.copy(isLoading = false)
                                }
                                errorEvent(networkResult.error)
                            }
                        }
                    }.launchIn(this)
                }
                else -> {
                    updateProfile(step = step,uBody = body).launchIn(this)
                }
            }
        }
    }

    private fun updateProfile(step : Int, uBody: ProfileRequestBody?): Flow<NetworkResult<ProfileResponse>> {



        return authAllUseCases.setProfileUseCase(profileRequestBody = uBody).onEach { networkResult ->
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
                    onEvent(ProfileScreenEvents.ProfileStepSuccess(step))
                }

                is NetworkResult.Error -> {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    errorEvent(networkResult.error)
                }
            }

        }
    }

    fun validateFields(step: Int, body: ProfileRequestBody?): String? {
        return when (step) {
            ProfileStepper.PHOTO.value -> {
                if (body?.profilePic.isNullOrEmpty()) "Please add your profile photo!" else null
            }
            ProfileStepper.NAME.value -> {
                if (body?.fullName.isNullOrEmpty()) "Please enter your full name" else null
            }
            ProfileStepper.INTERESTS.value -> {
                if (body?.interest.isNullOrEmpty()) "Please select your interests" else null
            }

            else -> null
        }
    }


    fun uploadImage(
        fileUri: String?,
        folderName: String?
    ): Flow<NetworkResult<ImageUploadResponse>> {
        return commonAllUseCases.imageUploadUseCase(
            fileUri = fileUri,
            folderName = folderName
        )
    }

    fun getAllInterests(){
        commonAllUseCases.getInterestsUseCase().onEach { networkResult ->
            when(networkResult) {
                is NetworkResult.Loading -> {
                    updateState { state -> state.copy(interestLoading = true) }
                }
                is NetworkResult.Success -> {
                    updateState { state -> state.copy(interestLoading = false) }
                    updateState { state ->
                        state.copy(interests = networkResult.data?.data)
                    }
                }
                is NetworkResult.Error -> {
                    updateState { state -> state.copy(interestLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

}