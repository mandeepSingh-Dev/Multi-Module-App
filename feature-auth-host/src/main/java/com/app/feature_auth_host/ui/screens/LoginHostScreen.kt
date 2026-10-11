package com.app.feature_auth_host.ui.screens

import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.app.core_auth.utils.SignUpHostSteps
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.Constants
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_navigation.HostScreens
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
import com.app.feature_auth_host.ui.screens.events.LoginScreenEvent
import com.app.feature_auth_host.ui.states.LoginUIState
import com.app.feature_auth_host.ui.viewmodels.LoginViewModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.errorprone.annotations.RestrictedApi
import com.joelkanyi.jcomposecountrycodepicker.component.CountrySelectionDialog
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.delay

enum class LoginType(val value: Int) {
    PHONE(1),
    EMAIL(2),

}

@OptIn(
    RestrictedApi::class,
    com.joelkanyi.jcomposecountrycodepicker.annotation.RestrictedApi::class
)
@Composable
fun LoginHostScreen(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    var selectedLoginType by remember { mutableStateOf(LoginType.PHONE.value) }



    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    var showOtpView by remember { mutableStateOf(false) }

    ObserveUIEvents(loginViewModel.uiEvent) { uiEVent ->
        when (uiEVent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uiEVent.error)
            }
            is CommonUIEvents.NavigateTo -> {
                backStack.add(uiEVent.navKey)
            }
            is LoginScreenEvent.ShowHideOtpView -> {
                Log.d("dbknfkbnfknb", "event ${uiEVent.isShow}")
                showOtpView = uiEVent.isShow
            }
            is LoginScreenEvent.OTPSuccess -> {
                val step = uiEVent.otpData?.step

                val isStepsPending = if(step != null) step < SignUpHostSteps.BANK_DETAILS.value else false

                if(uiEVent.otpData?.setProfile == 1 && !isStepsPending){
                    backStack.removeAll(backStack)
                    backStack.add(AppScreens.HomeBottomContainerHostScreen)
                }else {
                    backStack.add(
                        HostScreens.AuthHostScreens.SignUpHostScreen(
                            step = uiEVent.otpData?.step,
                            phone = uiEVent.phone,
                            countryCode = uiEVent.countryCode
                        )
                    )
                }
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


/*
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
                    backStack.removeAll(backStack)
                    backStack.add(UserScreens.HomeBottomContainerScreens)
                }else {
                    backStack.add(
                        UserScreens.AuthScreens.ProfileScreen(dob = dob, step = uiEVent.otpData?.step)
                    )
                }
            }
        }
    }
    */

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
                    "Login to manage profile, calls & earnings",
                    color = Color.Black,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    modifier = Modifier.padding(top = 15.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                AnimatedContent(selectedLoginType) { selectedLoginType ->
                    if (selectedLoginType == LoginType.PHONE.value) {
                        MobileNumberSection(
                            uiState = uiState,
                            showOtpView = showOtpView,
                            loginViewModel = loginViewModel,
                            focusManager = focusManager,
                            onShowOTPView = {
                                showOtpView = it
                            }
                        )
                    } else {
                        EmailPwdSection(modifier = Modifier)
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

                AnimatedVisibility(selectedLoginType == LoginType.PHONE.value) {
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
                    Text("OR", color = grey_4)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                }

                OutlineButton(
                    onClick = {
                        selectedLoginType = if (selectedLoginType == LoginType.PHONE.value) {
                            LoginType.EMAIL.value
                        } else {
                            LoginType.PHONE.value
                        }
                    },
                    text = if (selectedLoginType == LoginType.PHONE.value) "Continue as Email" else "Continue as Phone",
                    borderBrush = brushGrey(),
                    icon = if (selectedLoginType == LoginType.PHONE.value) R.drawable.emailcontainer else R.drawable.phonemargin,
                    endIcon = R.drawable.right_arrowcontainer,
                    fontColor = Color.Black,
                    verticalPadding = 12.dp,
                    horizontalPadding = 15.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("Need Host? ")
                }
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Create Account",
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = theme_color_2,
                                fontFamily = HAFFER_TRIAL_MEDIUM
                            )
                        ),
                        linkInteractionListener = { link ->
                            loginViewModel.navigationEvent(HostScreens.AuthHostScreens.SignUpHostScreen(
                                phone = null,
                                countryCode = null,
                                step = null
                            ))
//                                loginViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.TermsConditionsScreen(1)))
                        }
                    )
                ) {
                    append("Create Account")
                }
            }, modifier = Modifier.padding(top = 2.dp),
                fontSize = 12.sp)

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

@Composable
private fun MobileNumberSection(
    uiState: LoginUIState?,
    showOtpView: Boolean,
    loginViewModel : LoginViewModel,
    focusManager : FocusManager,
    onShowOTPView : (Boolean) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val state = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "IN"
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
                    onShowOTPView(false)
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


}

@Composable
private fun EmailPwdSection(modifier: Modifier) {


    val emailFocusRequester = remember { FocusRequester() }
    val pwdFocusRequester = remember { FocusRequester() }
    val keyboardController  = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextFieldOuterLabel(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            singleLine = true,
            hint = "Enter email address",
            isBorder = true,
            focusRequester = emailFocusRequester,
            leadingIcon = {
                Image(painter = painterResource(R.drawable.phoneemail), contentDescription = null)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )

        TextFieldOuterLabel(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            singleLine = true,
            hint = "Enter Password",
            isBorder = true,
            focusRequester = pwdFocusRequester,
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.phonepassword),
                    contentDescription = null
                )
            }, trailingIcon = {
                Image(
                    painter = painterResource(R.drawable.right_arrowcontainer),
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
        )

        Text("Forgot Password?", fontFamily = HAFFER_TRIAL_MEDIUM,
            color = theme_color_1,
            fontSize = 13.sp,
            modifier = Modifier.align(
            Alignment.End))
    }
}
