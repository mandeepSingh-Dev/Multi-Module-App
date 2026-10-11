package com.app.feature_auth_host.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_auth.utils.SignUpHostSteps
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.OTPBottomSheetScreen
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.grey_9
import com.app.feature_auth_host.ui.screens.events.SignUpScreenEvents
import com.app.feature_auth_host.ui.screens.signup_steps.BankDetails
import com.app.feature_auth_host.ui.screens.signup_steps.BasicInformation
import com.app.feature_auth_host.ui.screens.signup_steps.BecomeHost
import com.app.feature_auth_host.ui.screens.signup_steps.SelectInterests
import com.app.feature_auth_host.ui.screens.signup_steps.SelectLanguage
import com.app.feature_auth_host.ui.viewmodels.SignUpViewModel




@Composable
fun SignUpHostScreen(
    isForUpdate : Boolean = false,
    phone: String?,
    countryCode: String?,
    step: Int? = null,
    backstack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {

    val localKeyboardController = LocalSoftwareKeyboardController.current

    val defaultStep = if(!isForUpdate) SignUpHostSteps.BECOME_HOST.value else SignUpHostSteps.BASIC_INFORMATION.value

    var currentStep by remember { mutableIntStateOf((step?.plus(1)) ?: defaultStep) }

    val uiState by signUpViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        signUpViewModel.isForUpdate = isForUpdate
        if(isForUpdate){
            signUpViewModel.getProfileData()
        }
    }

    BackHandler(enabled = (currentStep > defaultStep)) {
        if(currentStep == SignUpHostSteps.SELECT_LANGUAGE.value){
            backstack.removeLastOrNull()
        }else {
            currentStep = currentStep.minus(1).coerceAtLeast(defaultStep)
        }
    }

    LaunchedEffect(Unit) {
        if(!isForUpdate) {
            signUpViewModel.updateState { state ->
                state.copy(
                    profileRequestBody = state.profileRequestBody?.copy(
                        phone = phone,
                        countryCode = countryCode
                    )
                )
            }
        }
    }


    var showOtpView by rememberSaveable { mutableStateOf(false) }

    ObserveUIEvents(signUpViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
            is SignUpScreenEvents.OTPSuccess -> {
                showOtpView = false
                currentStep = SignUpHostSteps.SELECT_LANGUAGE.value
            }

            is SignUpScreenEvents.ProfileStepSuccess -> {
                when (currentStep) {
                    SignUpHostSteps.BECOME_HOST.value -> {
                        currentStep = SignUpHostSteps.BASIC_INFORMATION.value
                    }

                    SignUpHostSteps.BASIC_INFORMATION.value -> {
                      if(!isForUpdate){
                          showOtpView = true
                      }else{
                          currentStep = SignUpHostSteps.SELECT_LANGUAGE.value
                      }
                    }

                    SignUpHostSteps.SELECT_LANGUAGE.value -> {
                        currentStep = SignUpHostSteps.SELECT_YOUR_INTERESTS.value
                    }

                    SignUpHostSteps.SELECT_YOUR_INTERESTS.value -> {
                        currentStep = SignUpHostSteps.BANK_DETAILS.value
                    }
                    else -> {
                        backstack.removeAll(backstack)
                        backstack.add(AppScreens.HomeBottomContainerHostScreen)
                    }
                }
            }
        }
    }


    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        modifier = Modifier.fillMaxSize().imePadding(),
        showBackgroundImage = true,
        isLoaderTappable = true
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            HeaderView2(
                title = if(isForUpdate) "Profile" else "Sign Up",
                secTitle = if(isForUpdate) "Update your profile" else "Create your profile and tell us a little about yourself",
                showNotificationIcon = false,
                modifier = Modifier.padding(horizontal = 20.dp),
                onBack = {
                    if (currentStep == defaultStep) {
                        backstack.removeLastOrNull()
                    } else {
                        currentStep = currentStep.minus(1).coerceAtLeast(defaultStep)
                    }
                }, onNotification = {
                    backstack.add(AppScreens.NotificationListScreen)
                }

            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                signUpStepsIndicators(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 20.dp, top = 25.dp),
                    selectedStep = currentStep,
                    isForUpdate = isForUpdate
                ) {
                    currentStep = it
                }

                AnimatedContent(currentStep) { step ->
                    when (step) {
                        SignUpHostSteps.BECOME_HOST.value -> {
                            BecomeHost(Modifier, onBack =  {
                                backstack.removeLastOrNull()
                            }) {
                                localKeyboardController?.hide()
                                currentStep = SignUpHostSteps.BASIC_INFORMATION.value
                            }
                        }

                        SignUpHostSteps.BASIC_INFORMATION.value -> {
                            BasicInformation(
                                modifier = Modifier,
                                uiState = uiState,
                                isForUpdate = isForUpdate,
                                signUpViewModel = signUpViewModel,
                                onNext = {countryCode ->
                                    localKeyboardController?.hide()
                                    signUpViewModel.setProfile(SignUpHostSteps.BASIC_INFORMATION.value,countryCode = countryCode)
                                })
                        }

                        SignUpHostSteps.SELECT_LANGUAGE.value -> {
                            SelectLanguage(
                                modifier = Modifier,
                                uiState = uiState, signUpViewModel = signUpViewModel
                            ) {
                                signUpViewModel.setProfile(step = SignUpHostSteps.SELECT_LANGUAGE.value,)
                            }
                        }

                        SignUpHostSteps.SELECT_YOUR_INTERESTS.value -> {
                            SelectInterests(modifier = Modifier.padding(horizontal = 20.dp),
                                uiState = uiState,
                                signUpViewModel = signUpViewModel, onNext = {
                                    signUpViewModel.setProfile(step = SignUpHostSteps.SELECT_YOUR_INTERESTS.value,)
                                })
                        }

                        SignUpHostSteps.BANK_DETAILS.value -> {
                            BankDetails(modifier = Modifier.padding(horizontal = 20.dp),
                                uiState = uiState,
                                signUpViewModel = signUpViewModel) {
                                signUpViewModel.setProfile(step = SignUpHostSteps.BANK_DETAILS.value,)
                            }
                        }
                    }
                }
            }
        }
    }
val context = LocalContext.current

    OTPBottomSheetScreen(
        showOtpView = showOtpView,
        otpError = uiState?.otpError,
        isVerifyInProgress = uiState?.isVerifyOtpLoading ?: false,
        onCancel = {
            showOtpView = false
            context.showToast(message = "Your account has been created. Please log in using the number you just registered with.")
            backstack.removeLastOrNull()
        },
        onDismiss = {
            signUpViewModel.cancelVerifyOtpApiJob()
            showOtpView = false
        },
        onVerify = {otp ->
            signUpViewModel.verifyOTP(otp)
        }
    )
}

@Composable
private fun signUpStepsIndicators(modifier: Modifier, selectedStep: Int, isForUpdate: Boolean = false, onClick: (Int) -> Unit) {

    val steps = if(!isForUpdate) {SignUpHostSteps.entries} else {
        SignUpHostSteps.entries.drop(1)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {



        steps.forEach {

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .clickable {
                        onClick(it.value)
                    }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = grey_9)
                )
                Row {
                    AnimatedVisibility(it.value <= selectedStep) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush = brush2())
                        )
                    }
                }
            }
        }
    }
}

