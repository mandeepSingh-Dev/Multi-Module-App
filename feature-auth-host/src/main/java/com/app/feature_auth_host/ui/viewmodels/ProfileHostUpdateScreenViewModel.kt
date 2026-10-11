package com.app.feature_auth_host.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.extensions.isUrl
import com.app.core_common.utils.fromJson
import com.app.feature_auth_host.ui.states.ProfileUpdateUIState
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.Interest
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileHostUpdateScreenViewModel @Inject constructor(
    private val commonAllUseCases: CommonAllUseCases,
    private val authAllUseCases: AuthAllUseCases,
    private val usM: UserManager
) : BaseViewModel<ProfileUpdateUIState>(ProfileUpdateUIState()) {


    init {
        getProfileData()
        getAllInterests()
    }


    fun getProfileData() {
        viewModelScope.launch {
            val profileData = usM.getUserDataString().fromJson<ProfileResponse.UserProfileData>()

            updateState { state ->
                state.copy(
                    profileRequestBody = profileData?.toProfileRequestBody(
                        state.profileRequestBody
                    )
                )
            }
            profileData?.interest?.let {
                selectInterests(it)
            }

            authAllUseCases.getProfileUseCase().onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        val profileData = it.data?.data
                        updateState { state ->
                            state.copy(profileRequestBody = profileData?.toProfileRequestBody(state.profileRequestBody))
                        }

                        profileData?.interest?.let {
                            selectInterests(it)
                        }
                    },
                    onError = {
                        Log.d("kfnbjkfnbjf", it.error.toString())
                        errorEvent(it.error)
                    }
                )
            }.launchIn(this)
        }
    }

    fun updateProfile(
        countryCode: String? = null
    ) = viewModelScope.launch {
        val body =
            uiState.value?.profileRequestBody?.copy(countryCode = countryCode?.ifBlank { uiState.value?.profileRequestBody?.countryCode })

        updateState { state -> state.copy(profileRequestBody = body) }

        if (body?.profilePic.isUrl() == false) {
            uploadImage(
                fileUri = body?.profilePic,
                folderName = ""
            ).onEach { networkResult ->
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
                        updateState { state ->
                            state.copy(profileRequestBody = body?.copy(profilePic = networkResult.data?.data?.file))
                        }

                        updateProfile(uBody = body).launchIn(this)
                    }

                    is NetworkResult.Error -> {
                        updateState { state ->
                            state.copy(isLoading = false)
                        }
                        errorEvent(networkResult.error)
                    }
                }
            }.launchIn(this)
        } else {
            updateProfile(body).launchIn(viewModelScope)
        }
    }


    private fun updateProfile(
        uBody: ProfileRequestBody?
    ): Flow<NetworkResult<ProfileResponse>> {

        return authAllUseCases.setProfileUseCase(profileRequestBody = uBody)
            .onEach { networkResult ->
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

    fun uploadImage(
        fileUri: String?,
        folderName: String?
    ): Flow<NetworkResult<ImageUploadResponse>> {
        return commonAllUseCases.imageUploadUseCase(
            fileUri = fileUri,
            folderName = folderName
        )
    }

    fun getAllInterests() {
        commonAllUseCases.getInterestsUseCase().onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Loading -> {
                    updateState { state -> state.copy(isInterestsLoading = true) }
                }

                is NetworkResult.Success -> {
                    updateState { state ->
                        state.copy(isInterestsLoading = false, interests = networkResult.data?.data)
                    }
                }

                is NetworkResult.Error -> {
                    updateState { state -> state.copy(isInterestsLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun selectInterests(interests: List<Interest?>) {
        updateState { state ->
            state.interests ?: return@updateState state
            val mList = state.interests.map { interest ->
                interest.copy(isSelected = interests.any { it?._id == interest._id })
            }

            state.copy(interests = mList)
        }
    }


}