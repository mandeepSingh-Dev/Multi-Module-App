package com.app.feature_change_password.ui

import android.widget.Space
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.green_8
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.red_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_13
import com.app.feature_change_password.ui.viewmodel.ChangePasswordViewModel
import com.zodiaq.ui.theme.Shape_15


@Composable
fun ChangePasswordScreen(
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()
) {

    val uiState by changePasswordViewModel.uiState.collectAsStateWithLifecycle()

    var showImage by rememberSaveable { mutableStateOf(false) }

    var currentPwd by rememberSaveable { mutableStateOf<String?>(null) }
    var newPwd by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmNewPwd by rememberSaveable { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    ObserveUIEvents(changePasswordViewModel.uiEvent) { uIEvent ->
        when(uIEvent){
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
            is ChangePasswordEvent.PwdChangedSuccessfully -> {
                context.showToast(uIEvent.msg)
                backStack.removeLastOrNull()
            }
        }
    }

    BoxCommon(
        uiState?.isLoading ?: false,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {

            HeaderView2(
                title = "Change Password",
                secTitle = "For your security, please set a new password for your account."
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            )
            {

                AnimatedVisibility(
                    showImage,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Image(
                        painter = painterResource(R.drawable.change_pwdimage98),
                        contentDescription = null,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                TextFieldOuterLabel(
                    label = "Current Password",
                    isRequired = true,
                    isPassword = true,
                    isBorder = true,
                    value = currentPwd ?: "",
                    hint = "Enter your current password",
                    error = uiState?.currentPwdError,
                    onValueChange = {
                        currentPwd = it
                        if (changePasswordViewModel.isChangePwdClicked) {
                            changePasswordViewModel.updateState { state ->
                                state.copy(currentPwdError = if (it.isEmpty()) "Please enter current password!" else null)
                            }

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onVisibilityChanged {
                            if (it) {
                                showImage = true
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    )

                )
                Spacer(modifier = Modifier.height(15.dp))
                TextFieldOuterLabel(
                    label = "New Password",
                    isRequired = true,
                    isPassword = true,
                    isBorder = true,
                    hint = "Enter new password",
                    value = newPwd ?: "",
                    onValueChange = {
                        newPwd = it
                        if (changePasswordViewModel.isChangePwdClicked) {
                            changePasswordViewModel.updateState { state ->
                                state.copy(newPwdError = if (it.isEmpty()) "Please enter new password!" else null)
                            }
                            if (it.isNotEmpty()) {
                                changePasswordViewModel.updateState { state ->
                                    state.copy(passwordValidation = validatePassword(it))
                                }
                            }
                        }
                    },
                    error = uiState?.newPwdError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    )

                )
                Spacer(modifier = Modifier.height(15.dp))


                AnimatedVisibility(uiState?.passwordValidation != null && (uiState?.passwordValidation?.isValid == false)) {
                    PasswordValidationCard(passwordValidation = uiState?.passwordValidation)
                }

                Spacer(modifier = Modifier.height(15.dp))
                TextFieldOuterLabel(
                    label = "Confirm New Password",
                    isRequired = true,
                    isPassword = true,
                    hint = "Enter confirm password",
                    error = uiState?.confirmPwdError,
                    isBorder = true,
                    value = confirmNewPwd ?: "",
                    onValueChange = {
                        confirmNewPwd = it
                        if (changePasswordViewModel.isChangePwdClicked) {
                            changePasswordViewModel.updateState { state ->
                                state.copy(confirmPwdError = if (it.isEmpty()) "Please enter confirm new password!" else null)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {

                    })
                )

                Spacer(modifier = Modifier.height(25.dp))
            }

            GradientButton(
                onClick = {
                    changePasswordViewModel.changePassword(
                        currentPwd = currentPwd,
                        newPwd = newPwd,
                        confirmNewPwd = confirmNewPwd
                    )
                },
                text = "Update Password",
                showEndIcon = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }

}

@Composable
fun PasswordValidationCard(

    passwordValidation: PasswordValidation?
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .border(width = 1.dp, color = theme_color_1, shape = Shape_15)
            .background(color = theme_color_13, shape = Shape_15)
            .padding(15.dp)
    ) {

        Image(
            painterResource(R.drawable.pwd_validation_iconwarmambershieldbadge_margin),
            contentDescription = null
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Password must contain:", fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 15.sp,
                lineHeight = 10.sp
            )


            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(passwordValidation?.minLength ?: false) { isMinLength ->
                    if (isMinLength) {
                        Icon(
                            Icons.Default.Check, contentDescription = null,
                            tint = green_8
                        )
                    } else {
                        Icon(
                            Icons.Default.Close, contentDescription = null,
                            tint = red_4
                        )
                    }
                }
                Text("At least 8 characters", fontSize = 12.sp, color = grey_10)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(passwordValidation?.hasUppercase ?: false) { hasUppercase ->
                    if (hasUppercase) {
                        Icon(
                            Icons.Default.Check, contentDescription = null,
                            tint = green_8
                        )
                    } else {
                        Icon(
                            Icons.Default.Close, contentDescription = null,
                            tint = red_4
                        )
                    }
                }
                Text("One uppercase letter (A-Z)", fontSize = 12.sp, color = grey_10)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(passwordValidation?.hasLowercase ?: false) { hasLowercase ->
                    if (hasLowercase) {
                        Icon(
                            Icons.Default.Check, contentDescription = null,
                            tint = green_8
                        )
                    } else {
                        Icon(
                            Icons.Default.Close, contentDescription = null,
                            tint = red_4
                        )
                    }
                }
                Text("One lowercase letter (a-z)", fontSize = 12.sp, color = grey_10)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(passwordValidation?.hasNumber ?: false) { hasNumber ->
                    if (hasNumber) {
                        Icon(
                            Icons.Default.Check, contentDescription = null,
                            tint = green_8
                        )
                    } else {
                        Icon(
                            Icons.Default.Close, contentDescription = null,
                            tint = red_4
                        )
                    }
                }
                Text("One number (0-9)", fontSize = 12.sp, color = grey_10)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(
                    passwordValidation?.hasSpecialCharacter ?: false
                ) { hasSpecialCharacter ->
                    if (hasSpecialCharacter) {
                        Icon(
                            Icons.Default.Check, contentDescription = null,
                            tint = green_8
                        )
                    } else {
                        Icon(
                            Icons.Default.Close, contentDescription = null,
                            tint = red_4
                        )
                    }
                }
                Text("One special character (e.g.!@#$%)", fontSize = 12.sp, color = grey_10)
            }

        }
    }
}
