package com.app.core_ui.components
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
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.theme_color_2
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.delay

const val OTP_RESEND_TIME = 5/*59*/

@Composable
fun TimerWithResend(
    modifier: Modifier = Modifier,
    onResendClick: () -> Unit
) {

    var timerThresholdValue = OTP_RESEND_TIME
    var timer by rememberSaveable { mutableStateOf(timerThresholdValue) }
    var isResendVisibile by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(timer) {
        delay(1000)
        if (timer > 0) {
            timer--
        } else {
            isResendVisibile = true
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
//        if (!isResendVisibile) {
//            LoadingIndicator(modifier = Modifier.size(20.dp), color = theme_color_1)
//        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(!isResendVisibile) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Text(
                        "Resend OTP in ",
                        color = Color.Black,
                        fontSize = 13.sp,
                    )
                    Text(
                        "${String.format("00:%02d", timer)}",
                        color = theme_color_2,
                        fontSize = 13.sp,
                    )
                }

            }
            AnimatedVisibility(isResendVisibile) {
                resendText {
                    timer = timerThresholdValue
                    isResendVisibile = false
                    onResendClick()
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun resendText(onClick: () -> Unit) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("I didn't receive a code. ")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = HAFFER_TRIAL_BOLD,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Resend")
            }

        },
        fontSize = 14.sp,
        modifier = Modifier
            .clickableDebAnim {
                onClick()
            })
}
