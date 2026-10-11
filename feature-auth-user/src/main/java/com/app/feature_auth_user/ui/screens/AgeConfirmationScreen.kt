package com.app.feature_auth_user.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_navigation.AppScreens
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.DatePickerCustomDialog
import com.app.core_ui.components.DropDownTextField
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.TextFieldOuterLabel2
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.theme_color_2
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.launch
import java.util.Date

@Composable
@Preview
fun AgeConfirmationScreen(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current
) {

    val coroutineScope = rememberCoroutineScope()

    var showDOBDialog by remember { mutableStateOf(false) }
    val dobPickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })


    BoxCommon(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            )
            {
                Image(
                    painter = painterResource(R.drawable.logo_1image1),
                    contentDescription = null,
                    modifier = Modifier
                )
                Text("Talk . Connect Enjoy")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .fillMaxWidth()
//                        .weight(1f)
                        .border(width = 1.dp, brush = brush2(), shape = Shape_20)
                        .background(Color.White, Shape_20)
                        .padding(15.dp)
                )
                {

                    Image(
                        painter = painterResource(R.drawable._18plusicon18_shieldicon),
                        contentDescription = null
                    )
                    GradientText(
                        "Age Confirmation",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 25.sp
                    )
                    Text(
                        "You must be 18 years or older to use Kahoji", textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color.Black, modifier = Modifier.padding(top = 10.dp),
                        fontFamily = HAFFER_TRIAL_MEDIUM,
                    )
                    Text(
                        "Please enter your date of birth", textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color.Black, modifier = Modifier.padding(top = 20.dp),
                    )

                    TextFieldOuterLabel(
                        value = dobPickerState.selectedDateMillis?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_9) ?: "",
                        onValueChange = {},
                        isBorder = true,
                        enabled = false,
                        hint = "Select Date",
                        hintColor = Color.Black,
                        trailingIcon = {
                            Image(painterResource(R.drawable.baseline_keyboard_arrow_down_24), contentDescription = null)
                        },
                        leadingIcon = {
                            Image(painterResource(R.drawable.calendarimage18), contentDescription = null)
                        },
                        modifier = Modifier.padding(top = 15.dp)
                            .clip(Shape_15)
                            .clickable{
                                showDOBDialog = true
                            }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 70.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.private_iconimage16),
                            contentDescription = null
                        )
                        Text(
                            "Your date of birth is not shared with anyone.",
                            fontFamily = HAFFER_TRIAL_MEDIUM,
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                            )
                    }
                    GradientButton(
                        onClick = {
                            coroutineScope.launch {
                                val dob = dobPickerState.selectedDateMillis
                                if (dob == null) {
                                    snackbarHostState.showSnackBarWithDismiss(message = "Please select your date of birth.")
                                }else {
                                    val dobFormatted = dob.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_2)
                                    backStack.add(UserScreens.AuthScreens.LoginScreen(dob = dobFormatted))
                                }
                            }
                        },
                        text = "Continue",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
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
            }, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 2.dp))

        }

        DatePickerCustomDialog(
            shouldShow = showDOBDialog,
            title = "Date of Birth",
            onDismiss = {
                showDOBDialog = false
            },
            datePickerState = dobPickerState,
            onDone = {
                val dob = it ?: Date().time

//            bookingFormFillingViewModel.updateState { state ->
//                val formattedDate = dob.convertToDateFormat(DateFormats.DATE_FORMAT_2) ?: ""
//                val formattedDateForPrediction =
//               dob.convertToDateFormat(DateFormats.DATE_FORMAT_8) ?: ""
//                state?.copy(bookingRequestBody = state.bookingRequestBody?.copy(dob = formattedDate))
//                state?.copy(
//                    createPredictionSingleUserRequestBody = state.createPredictionSingleUserRequestBody?.copy(
//                        dob = formattedDateForPrediction
//                    )
//                )
//            }
                showDOBDialog = false
            },
            onCancel = {
                showDOBDialog = false
            })


    }
}