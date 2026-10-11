package com.app.feature_auth_user.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.Constants
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_navigation.ReferralScreens
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.OTPInputTextFields
import com.app.core_ui.components.OutlineButton
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.TextWithAsterisk
import com.app.core_ui.components.TimerWithResend
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brushGrey
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.grey_5
import com.app.core_ui.ui.theme.grey_7
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_2
import com.app.core_ui.ui.theme.theme_color_8
import com.app.feature_auth_user.ui.events.LoginScreenEvent
import com.app.feature_auth_user.ui.viewmodels.LoginViewModel
import com.joelkanyi.jcomposecountrycodepicker.annotation.RestrictedApi
import com.joelkanyi.jcomposecountrycodepicker.component.CountrySelectionDialog
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.delay

@OptIn(RestrictedApi::class)
@Composable
fun LoginUserScreen(
    dob : String?,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    var showOtpView by remember { mutableStateOf(false) }

    ObserveUIEvents(loginViewModel.uiEvent) { uiEVent ->
        when (uiEVent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uiEVent.error)
            }

            is LoginScreenEvent.ShowHideOtpView -> {
                Log.d("dbknfkbnfknb", "event ${uiEVent.isShow}")
                showOtpView = uiEVent.isShow
            }
            is LoginScreenEvent.OTPSuccess -> {
                if(uiEVent.otpData?.setProfile == 1){

                    if(uiEVent.otpData.showReferralPrompt == true){
                        backStack.removeAll(backStack)
                        backStack.add(ReferralScreens.ApplyReferralCodeScreen)
                    }else{
                        backStack.removeAll(backStack)
                        backStack.add(AppScreens.HomeBottomContainerScreen)
                    }

                }else {
                    backStack.add(
                        UserScreens.AuthScreens.ProfileScreen(dob = dob, step = uiEVent.otpData?.step)
                    )
                }
            }
            is LoginScreenEvent.ContinueAsGuest -> {
                backStack.removeAll(backStack)
                backStack.add(AppScreens.HomeBottomContainerScreen)
            }
        }
    }

    val state = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "IN"
    )


    var showCountryPicker by rememberSaveable { mutableStateOf(false) }


    val otpValues = remember { mutableStateListOf<String>("", "", "", "", "", "") }
    var hasCompleted by rememberSaveable { mutableStateOf(false) }


    val isButtonEn by remember {
        derivedStateOf {
            otpValues.all { it.isNotEmpty() }
        }
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                Image(
                    painter = painterResource(R.drawable.login_imageimage20),
                    contentDescription = null,
                    modifier = Modifier
                )


                GradientText(
                    "Welcome to",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .weight(1f)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.heart_left),
                            contentDescription = null, modifier = Modifier.size(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(color = theme_color_1, shape = CircleShape)
                        )
                    }
                    GradientText(
                        "Kahoji!", fontSize = 20.sp,
                        modifier = Modifier
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(color = theme_color_1, shape = CircleShape)
                        )

                        Image(
                            painter = painterResource(R.drawable.heart_right),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    "Login or sign up to start your journey",
                    color = Color.Black,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    modifier = Modifier.padding(top = 15.dp)
                )

                TextWithAsterisk(
                    text = "Mobile Number", modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .border(color = theme_color_8, width = 1.dp, shape = Shape_20)
                        .background(color = Color.White, shape = Shape_20)
                )
                {

                    KomposeCountryCodePicker(
                        state = state,
                        text = "",
                        showOnlyCountryCodePicker = true,
                        textStyle = TextStyle(fontSize = 13.sp, fontFamily = HAFFER_TRIAL_REGULAR),
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(1.dp)
                            .border(
                                brush = brush2(),
                                width = 1.dp,
                                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                            )
                            .background(
                                color = theme_color_8,
                                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                            )

                    )


                    TextFieldOuterLabel(
                        value = uiState?.loginRequestBody?.phone ?: "",
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                showOtpView = false
                                loginViewModel.updateState { state ->
                                    state.copy(
                                        loginRequestBody = state.loginRequestBody?.copy(phone = it)
                                    )
                                }
                            }
                        },
                        hint = "Enter your mobile number",
                        showShadows = false,
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.phonefa7_solid_phone),
                                contentDescription = null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusRequester.freeFocus()
                            focusManager.clearFocus()
                            loginViewModel.login(
                                state.selectedCountry.phoneNoCode.toString()
                            )
                        }),
                        modifier = Modifier.weight(1f),
                        focusRequester = focusRequester
                    )

//                Row(modifier = Modifier.fillMaxHeight()) {
//                    Text("+91")
//                    Image(painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24), contentDescription = null)
//                }

                }
                AnimatedVisibility(
                    !uiState?.phoneNumError.isNullOrEmpty(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Row(modifier = Modifier.align(Alignment.End)) {
                        Text(
                            uiState?.phoneNumError ?: "Please enter correct mobile number",
                            fontFamily = HAFFER_TRIAL_MEDIUM,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                        )
                    }
                }

                AnimatedVisibility(!showOtpView) {
                    GradientButton(
                        onClick = {
                            focusRequester.freeFocus()
                            focusManager.clearFocus()
                            loginViewModel.login(state.selectedCountry.phoneNoCode)
                                  },
                        text = "Continue",
                        showEndIcon = true,
                        endIcon = R.drawable.group_2, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )
                }

                AnimatedVisibility(showOtpView) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "OTP",
                            letterSpacing = 0.8.sp,
                            color = grey_4,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )

                        OTPInputTextFields(
                            otpValues = otpValues,
                            isError = !uiState?.otpError.isNullOrEmpty(),
                            onOtpInputComplete = {
                                if (!hasCompleted) {
                                    hasCompleted = true
//                        isButtonEnabled = true
//                                    onDone(otpValues.toString())
                                }
                            },
                            onUpdateOtpValuesByIndex = { index, newValue ->
//                    isButtonEnabled = false
                                otpValues[index] = newValue
                            },
                            otpLength = 6,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp, end = 20.dp)
                        )
                        TimerWithResend(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            otpValues.forEachIndexed { index, string ->
                                otpValues[index] = ""
                            }
                            focusRequester.freeFocus()
                            focusManager.clearFocus()
                            loginViewModel.login(state.selectedCountry.phoneNoCode.toString())
                        }

                        GradientButton(
                            onClick = {

                                loginViewModel.verifyOTP(otpValues.joinToString(""))
                            },
                            enabled = isButtonEn,
                            text = "Verify",
                            showEndIcon = true,
                            endIcon = R.drawable.group_2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 15.dp
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.private_iconimage16),
                        contentDescription = null
                    )
                    Text(
                        "Your number is safe with us.\nWe never share you number with anyone.",
                        fontSize = 12.sp, lineHeight = 19.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                    Text("OR", color = grey_5)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                }

                OutlineButton(
                    onClick = {
                        loginViewModel.onEvent(LoginScreenEvent.ContinueAsGuest)
                    },
                    text = "Continue as Guest",
                    borderBrush = brushGrey(),
                    icon = R.drawable.guest,
                    endIcon = R.drawable.right_arrowcontainer,
                    fontColor = Color.Black,
                    verticalPadding = 12.dp,
                    horizontalPadding = 15.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))




            }

            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                    )
                ) {
                    append("By continuing, you agree to our ")
                }

                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Terms of Service",
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = theme_color_2,
                                fontSize = 12.sp,
                            )
                        ),
                        linkInteractionListener = { link ->
//                                loginViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.TermsConditionsScreen(1)))
                        }
                    )
                ) {
                    append("Terms of Service ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                    )
                ) {
                    append("and ")
                }
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Privacy_Policy",
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = theme_color_2,
                                fontSize = 12.sp,
                            )
                        ),
                        linkInteractionListener = { link ->
//                                loginViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.TermsConditionsScreen(2)))
                        }
                    )
                ) {
                    append("Privacy Policy ")
                }
            }, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
        }

    }

    if (showCountryPicker) {
        CountrySelectionDialog(
            countryList = state.countryList,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            onDismissRequest = { showCountryPicker = false },
            onSelect = { country ->
                state.setCode(country.code)
                showCountryPicker = false
            },
        )
    }
}


/*
@Composable
@Preview
fun TestOtpView(){
//    var showOtpView by remember { mutableStateOf(false) }

    val otpValues = remember { mutableStateListOf<String>("", "", "", "", "", "") }
//    var hasCompleted by rememberSaveable { mutableStateOf(false) }

//    val isButtonEn by remember {
//        derivedStateOf {
//            otpValues.all { it.isNotEmpty() }
//        }
//    }

//    val focusRequester = remember { FocusRequester() }
//    val focusManager = LocalFocusManager.current

    OTPInputTextFields(
        otpValues = otpValues,
        onOtpInputComplete = {
//            if (!hasCompleted) {
//                hasCompleted = true
//                        isButtonEnabled = true
//                                    onDone(otpValues.toString())
//            }
        },
        onUpdateOtpValuesByIndex = { index, newValue ->
//                    isButtonEnabled = false
            otpValues[index] = newValue
        },
        otpLength = 6,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 150.dp, bottom = 10.dp, end = 20.dp)
    )
}*/
