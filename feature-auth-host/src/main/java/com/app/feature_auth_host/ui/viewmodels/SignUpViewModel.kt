package com.app.feature_auth_host.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_auth.domain.AuthAllUseCases
import com.app.core_auth.model.requestBody.ProfileRequestBody
import com.app.core_auth.model.response.ProfileResponse
import com.app.core_auth.model.response.SignUpResponse
import com.app.core_auth.utils.SignUpHostSteps
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.extensions.isUrl
import com.app.core_common.utils.fromJson
import com.app.feature_auth_host.ui.screens.events.SignUpScreenEvents
import com.app.feature_auth_host.ui.states.SignUpScreenUIState
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.Interest
import com.uae.core_network.data.model.LanguageListResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authAllUseCases: AuthAllUseCases,
    private val commonAllUseCases: CommonAllUseCases,
    private val usM: UserManager
) : BaseViewModel<SignUpScreenUIState>(SignUpScreenUIState()) {

    var isForUpdate = false

    init {
        getAllLanguages()
        getCities()
    }


    fun setProfile(
        step: Int,
        countryCode: String? = null
    ) = viewModelScope.launch {
        var body =
            uiState.value?.profileRequestBody?.copy(countryCode = countryCode?.ifBlank { uiState.value?.profileRequestBody?.countryCode })

        body = when (step) {
            SignUpHostSteps.SELECT_LANGUAGE.value -> {
                body?.copy(languages = uiState.value?.languages?.filter { it?.isSelected == true }
                    ?.map { it?.name }, setProfile = 1, step = step)
            }

            SignUpHostSteps.SELECT_YOUR_INTERESTS.value -> {
                body?.copy(
                    interest = uiState.value?.interests?.filter { it.isSelected }
                        ?.map { it._id },
                    setProfile = 1,
                    step = step
                )
            }

            else -> {
                body?.copy(step = step)
            }
        }

        updateState { state -> state.copy(profileRequestBody = body) }

        val error = validateFields(step, body)
        if (!error.isNullOrEmpty()) {
            errorEvent(error)
        } else {
            when {
                step == SignUpHostSteps.BASIC_INFORMATION.value -> {

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

                                    val uBody = body?.copy(profilePic = networkResult.data?.data?.file)

                                    updateState { state ->
                                        state.copy(isLoading = false)
                                    }

                                    updateState { state ->
                                        state.copy(profileRequestBody = uBody)
                                    }

                                    if (!isForUpdate) {
                                        signUp(
                                            step = SignUpHostSteps.BASIC_INFORMATION.value,
                                            uBody = uBody
                                        ).launchIn(this)
                                    } else {
                                        updateProfile(step = step, uBody = uBody).launchIn(this)
                                    }

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
                        if (!isForUpdate) {
                            signUp(
                                step = SignUpHostSteps.BASIC_INFORMATION.value,
                                uBody = body
                            ).launchIn(this)
                        } else {
                            updateProfile(step = step, uBody = body).launchIn(this)
                        }
                    }
                }

                else -> {
                    updateProfile(step = step, uBody = body).launchIn(this)
                }
            }
        }
    }


    private fun signUp(step: Int, uBody: ProfileRequestBody?): Flow<NetworkResult<SignUpResponse>> {

        return authAllUseCases.signUpUseCase(profileRequestBody = uBody).onEach { networkResult ->
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
                    onEvent(SignUpScreenEvents.ProfileStepSuccess(step))
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

    private fun updateProfile(
        step: Int,
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
                        onEvent(SignUpScreenEvents.ProfileStepSuccess(step))
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


    fun validateFields(
        step: Int,
        body: ProfileRequestBody?
    ): String? {

        return when (step) {

            SignUpHostSteps.BASIC_INFORMATION.value -> {

                when {
                    body?.profilePic.isNullOrEmpty() -> {
                        "Please add your profile photo!"
                    }

                    body.fullName.isNullOrBlank() -> {
                        "Please enter your full name"
                    }

                    body.phone.isNullOrBlank() && !isForUpdate -> {
                        "Please enter your phone number"
                    }

                    body.email.isNullOrBlank() && !isForUpdate -> {
                        "Please enter your email address"
                    }

                    body.about.isNullOrBlank() -> {
                        "Please enter something about yourself"
                    }

                    body.password.isNullOrBlank() && !isForUpdate -> {
                        "Please enter your password"
                    }

                    uiState.value?.confirmPassword.isNullOrBlank() && !isForUpdate -> {
                        "Please confirm your password"
                    }

                    body.password != uiState?.value?.confirmPassword && !isForUpdate -> {
                        "Passwords do not match"
                    }

                    else -> {
                        null
                    }
                }
            }

            SignUpHostSteps.SELECT_LANGUAGE.value -> {
                val isLanguagesSelected = body?.languages?.isNotEmpty()
                if (isLanguagesSelected == false) {
                    "Please select languages"
                } else {
                    null
                }
            }

            SignUpHostSteps.SELECT_YOUR_INTERESTS.value -> {
                val isInterestsSelected = body?.interest?.isNotEmpty()

                if (isInterestsSelected == false) {
                    "Please select interests"
                } else {
                    null
                }
            }

            SignUpHostSteps.BANK_DETAILS.value -> {
                when {
                    body?.accountHolderName.isNullOrEmpty() -> {
                        "Please enter your account name"
                    }

                    body.accountNumber.isNullOrBlank() -> {
                        "Please enter your account number"
                    }

                    body.bankName.isNullOrBlank() -> {
                        "Please enter your bank name"
                    }

                    body.accountNumber.isNullOrBlank() -> {
                        "Please enter your account number"
                    }

                    body.ifscCode.isNullOrBlank() -> {
                        "Please enter your IFSC code"
                    }

                    else -> null
                }
            }

            else -> null
        }
    }


    fun validateUpdateProfileFields(step: Int?): String? {
        return when (step) {
            SignUpHostSteps.SELECT_LANGUAGE.value -> {
                val isLanguagesSelected =
                    uiState.value?.languages?.filter { it?.isSelected == true }?.isNotEmpty()
                if (isLanguagesSelected == false) {
                    "Please select languages"
                } else {
                    null
                }
            }

            SignUpHostSteps.SELECT_YOUR_INTERESTS.value -> {
                val isInterestsSelected =
                    uiState.value?.interests?.filter { it.isSelected == true }?.isNotEmpty()
                if (isInterestsSelected == false) {
                    "Please select interests"
                } else {
                    null
                }
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

    fun getAllInterests() {
        commonAllUseCases.getInterestsUseCase().onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Loading -> {
                    updateState { state -> state.copy(interestLoading = uiState.value?.interests.isNullOrEmpty()) }
                }

                is NetworkResult.Success -> {
                    updateState { state -> state.copy(interestLoading = false) }
                    updateState { state ->
                        state.copy(interests = networkResult.data?.data)
                    }
                    uiState.value?.profileRequestBody?.interest?.let {
                        selectInterestsById(it)
                    }
                }

                is NetworkResult.Error -> {
                    updateState { state -> state.copy(interestLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }


    fun getAllLanguages() {
        commonAllUseCases.getLanguagesUseCase().onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Loading -> {
                    updateState { state -> state.copy(languagesLoading = true) }
                }

                is NetworkResult.Success -> {
                    updateState { state -> state.copy(languagesLoading = false) }
                    updateState { state ->
                        state.copy(languages = networkResult.data?.data)
                    }
                    uiState.value?.profileRequestBody?.languages?.let {
                        selectLanguages(it)
                    }
                }

                is NetworkResult.Error -> {
                    updateState { state -> state.copy(languagesLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }
    fun getCities() {
        commonAllUseCases.getCitiesUseCase(search = "").onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isCitiesLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isCitiesLoading = false,
                            citiesList = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state -> state.copy(isCitiesLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }
    fun updatesLanguages(item: LanguageListResponse.Language?) {
        updateState { state ->
            val mList = state.languages?.toMutableList()
            val newList = mList?.map { lang ->
                if (lang?.code == item?.code) {
                    lang?.copy(isSelected = !(item?.isSelected ?: false))
                } else {
                    lang
                }
            }
            state.copy(languages = newList)
        }
    }

    fun updateInterests(item: Interest?) {
        updateState { state ->
            val mList = state.interests?.toMutableList()
            val newList = mList?.map { interest ->
                if (interest._id == item?._id) {
                    interest.copy(isSelected = !(item?.isSelected ?: false))
                } else {
                    interest
                }
            }
            state.copy(interests = newList)
        }
    }

    var verifyOTPJob: Job? = null
    fun verifyOTP(otp: String?) {
        verifyOTPJob?.cancel()
        verifyOTPJob =
            authAllUseCases.otpVerifyUseCase(otp = otp, isEmail = true).onEach { networkResult ->
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state.copy(isLoading = true, otpError = null)
                        }
                    }

                    is NetworkResult.Success -> {
                        onEvent(SignUpScreenEvents.OTPSuccess(networkResult.data?.data))
                        getAllInterests()
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

    fun cancelVerifyOtpApiJob() {
        verifyOTPJob?.cancel()
        verifyOTPJob = null
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
            profileData?.languages?.let {
                selectLanguages(it)
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
                        profileData?.languages?.let {
                            selectLanguages(it)
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

    fun selectInterests(interests: List<Interest?>) {
        updateState { state ->
            state.interests ?: return@updateState state
            val mList = state.interests.map { interest ->
                interest.copy(isSelected = interests.any { it?._id == interest._id })
            }

            state.copy(interests = mList)
        }
    }

    fun selectInterestsById(interests: List<String?>) {
        updateState { state ->
            state.interests ?: return@updateState state
            val mList = state.interests.map { interest ->
                interest.copy(isSelected = interests.any { it == interest._id })
            }

            state.copy(interests = mList)
        }
    }

    fun selectLanguages(langs: List<String?>) {
        updateState { state ->
            state.languages ?: return@updateState state
            val mList = state.languages.map { language ->
                language?.copy(isSelected = langs.any { it.equals(language.name, ignoreCase = true) })
            }

            state.copy(languages = mList)
        }
    }


}