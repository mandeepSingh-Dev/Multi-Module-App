package com.app.core_ui.components


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.FilledCustomButton
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.OTPInputTextFields
import com.app.core_ui.components.TimerWithResend
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_1
import com.zodiaq.ui.theme.Shape_20


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPBottomSheetScreen(
    showOtpView: Boolean ,
    otpError: String? = null,
    isVerifyInProgress : Boolean ,
    onDismiss : () -> Unit,
    onCancel : () -> Unit,
    onVerify: (otp: String) -> Unit
) {


    if (showOtpView) {
        val otpValues = remember { mutableStateListOf<String>("", "", "", "", "", "") }
        var hasCompleted by rememberSaveable { mutableStateOf(false) }


        val isButtonEn by remember {
            derivedStateOf {
                otpValues.all { it.isNotEmpty() }
            }
        }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        ModalBottomSheet(
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            dragHandle = null,
            onDismissRequest = onDismiss,

        ) {

            BackHandler { }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .dropShadowCustom()
                    .background(color = Color.White, shape = Shape_20)
                    .padding(20.dp)
            ) {

                Image(painter = painterResource(R.drawable.phoneemail), contentDescription = null,
                    modifier = Modifier.size(50.dp))

                Text("Verify your email", fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 17.sp)
                Text("We sent a 6-digit verification on your email", color = text_grey_3, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))

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
                    isError = !otpError.isNullOrEmpty(),
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
                    onVerify(otpValues.joinToString(""))
                }

                AnimatedContent(isVerifyInProgress) {isVerifyInProgress ->
                    if(isVerifyInProgress){
                        CircularProgressIndicator()
                    }else{

                GradientButton(
                    onClick = {
                        onVerify(otpValues.joinToString(""))

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
                FilledCustomButton(onClick = onCancel,
                    text = "Cancel",
                    color = Color.Transparent,
                    textColor = theme_color_1,
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp))
            }
        }
    }
}