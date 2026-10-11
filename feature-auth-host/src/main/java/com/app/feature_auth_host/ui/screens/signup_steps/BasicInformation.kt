package com.app.feature_auth_host.ui.screens.signup_steps

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.app.core_common.extensions.createFolder
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_common.utils.localeUtils.toLocalDateOrNull
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.CitySelectionScreen
import com.app.core_ui.components.DatePickerCustomDialog
import com.app.core_ui.components.DropDownTextField
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.ImageOptionsSelector
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.TextFieldOuterLabel2
import com.app.core_ui.components.TextWithAsterisk
import com.app.core_ui.components.padding
import com.app.core_ui.components.strokeGapModifier
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_8
import com.app.feature_auth_host.ui.states.SignUpScreenUIState
import com.app.feature_auth_host.ui.viewmodels.SignUpViewModel
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInformation(
    modifier: Modifier,
    uiState: SignUpScreenUIState?,
    signUpViewModel: SignUpViewModel,
    onNext: (countryCode: String) -> Unit,
    isForUpdate: Boolean = false,
) {

    val coroutineScope = rememberCoroutineScope()
    var password by remember { mutableStateOf("") }
    var showCitiesList by remember { mutableStateOf(false) }



    val focusRequester = remember { FocusRequester() }

    var showDOBDialog by remember { mutableStateOf(false) }
    val dobPickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val state = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "IN"
    )


    val context = LocalContext.current


    var showImageSelectorDialog by remember { mutableStateOf(false) }
    var fileUri: Uri? by remember { mutableStateOf(null) }

    LaunchedEffect(uiState?.profileRequestBody?.birthDate) {
        dobPickerState.setSelectedDate(uiState?.profileRequestBody?.birthDate?.toLocalDateOrNull())
    }


    LaunchedEffect(Unit) {
        var fileName = "profile_image_${System.currentTimeMillis()}"
        if (!fileName.isNullOrEmpty()) {
            val folderFile = File(context.cacheDir, fileName).createFolder()
            val file = File(folderFile, fileName)
            fileUri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", file
            )
        }
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                fileUri?.let { it1 ->
                    signUpViewModel.updateState { state ->
                        state.copy(
                            profileRequestBody = state.profileRequestBody?.copy(
                                profilePic = fileUri.toString()
                            )
                        )
                    }
                }
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                signUpViewModel.updateState { state ->
                    state.copy(profileRequestBody = state.profileRequestBody?.copy(profilePic = uri.toString()))
                }
            }
        }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    )
    {
        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GradientText(
                text = "Basic Information", fontSize = 20.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            Text(
                "Let's start with the basic", textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .padding(top = 30.dp)
            )
            {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .strokeGapModifier(cornerRadius = 200.dp)
                        .padding(10.dp)
                ) {
                    Log.d("fbknfkbnf", uiState?.profileRequestBody?.profilePic.toString())
                    AsyncImageWithLoader(
                        image = uiState?.profileRequestBody?.profilePic ?: "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .dropShadow(
                            CircleShape,
                            shadow = Shadow(radius = 10.dp, color = Color.Black.copy(0.2f))
                        )
                        .background(color = Color.White, shape = CircleShape)
                        .align(
                            Alignment.BottomEnd
                        )
                        .padding(5.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                showImageSelectorDialog = true
                            }
                    )
                }
            }

            TextFieldOuterLabel(
                label = "Full Name",
                isRequired = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.placeholderbackground),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                },
                isBorder = true,
                borderColor = theme_color_1,
                showShadows = false,
                hint = "Enter your full name",
                value = uiState?.profileRequestBody?.fullName ?: "",
                onValueChange = {
                    signUpViewModel.updateState { state ->
                        state.copy(
                            profileRequestBody = state.profileRequestBody?.copy(
                                fullName = it
                            )
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.PersonName,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                modifier = Modifier.padding(top = 25.dp)
            )

            Text(
                "Please enter your date of birth",
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )

            DropDownTextField(
                list = listOf(""),
                getText = {
                    ""
                },
                value = dobPickerState.selectedDateMillis?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_9)
                    ?: "",
                hint = "Select Date",
                enabled = false,
                onSelect = {},
                trailingIcon = R.drawable.baseline_keyboard_arrow_down_24,
                leadingIcon = R.drawable.calendarimage18,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showDOBDialog = true
                    }
            )
            Text(
                "Location",
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )

            DropDownTextField(
                list = uiState?.citiesList ?: emptyList(),
                getText = {
                    it?.city ?: ""
                },
                value = uiState?.profileRequestBody?.city?.replaceFirstChar { it.uppercase() } ?: "",
                hint = "Select Location",
                enabled = false,
                onSelect = {},
                trailingIcon = R.drawable.baseline_keyboard_arrow_down_24,
                leadingIcon = R.drawable.calendarimage18,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showCitiesList = true
                    }
            )


            TextWithAsterisk(
                text = "Mobile Number",
                fontSize = 13.sp,
                modifier = Modifier
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
                    state = state, text = "", showOnlyCountryCodePicker = true,
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
                    showShadows = false,
                    hint = "Enter phone number",
                    value = uiState?.profileRequestBody?.phone ?: "",
                    onValueChange = {
                        signUpViewModel.updateState { state ->
                            state.copy(
                                profileRequestBody = state.profileRequestBody?.copy(
                                    phone = it
                                )
                            )
                        }
                    },
                    enabled = !isForUpdate,
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.phonefa7_solid_phone),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Phone
                    )
                )


//                Row(modifier = Modifier.fillMaxHeight()) {
//                    Text("+91")
//                    Image(painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24), contentDescription = null)
//                }

            }
            TextFieldOuterLabel(
                hint = "Enter your email",
                showShadows = false,
                label = "Email",
                isRequired = true,
                isBorder = true,
                enabled = !isForUpdate,
                value = uiState?.profileRequestBody?.email ?: "",
                onValueChange = {
                    signUpViewModel.updateState { state ->
                        state.copy(
                            profileRequestBody = state.profileRequestBody?.copy(
                                email = it
                            )
                        )
                    }
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.emailcontainer),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            TextFieldOuterLabel2(
                value = uiState?.profileRequestBody?.about ?: "",
                onValueChange = {
                    signUpViewModel.updateState { state ->
                        state.copy(profileRequestBody = state.profileRequestBody?.copy(about = it))
                    }
                },
                label = "Bio",
                isRequired = true,
                isBorder = true,
                leadingIcon = {
                    Image(painterResource(R.drawable.bioframe5), contentDescription = null)
                },
                minLines = 5,
                hint = "Enter about your self",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            )

            if(!isForUpdate) {
                TextFieldOuterLabel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    label = "Password",
                    value = uiState?.profileRequestBody?.password ?: "",
                    onValueChange = {
                        signUpViewModel.updateState { state ->
                            state.copy(
                                profileRequestBody = state.profileRequestBody?.copy(
                                    password = it
                                )
                            )
                        }
                    },
                    singleLine = true,
                    hint = "Enter Password",
                    isBorder = true,
                    isRequired = true,
                    isPassword = true,
                    leadingIcon = {
                        Image(painterResource(R.drawable.phonepassword), contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                )
                TextFieldOuterLabel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    label = "Confirm Password",
                    value = uiState?.confirmPassword ?: "",
                    onValueChange = {
                        signUpViewModel.updateState { state ->
                            state.copy(
                                confirmPassword = it
                            )
                        }
                    },
                    singleLine = true,
                    hint = "Enter your confirm Password",
                    isBorder = true,
                    isRequired = true,
                    isPassword = true,

                    leadingIcon = {
                        Image(painterResource(R.drawable.phonepassword), contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        coroutineScope.launch {
                            val dob = dobPickerState.selectedDateMillis
                            val dobFormatted =
                                dob?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_2)
                            signUpViewModel.updateState { state ->
                                state.copy(
                                    profileRequestBody = state.profileRequestBody?.copy(
                                        birthDate = dobFormatted
                                    )
                                )
                            }
                            onNext(state.selectedCountry.phoneNoCode.toString())
                        }
                    })
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }


        GradientButton(
            onClick = {

                coroutineScope.launch {
                    val dob = dobPickerState.selectedDateMillis
                    val dobFormatted =
                        dob?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_2)
                    signUpViewModel.updateState { state ->
                        state.copy(profileRequestBody = state.profileRequestBody?.copy(birthDate = dobFormatted))
                    }

                    onNext(state.selectedCountry.phoneNoCode.toString())

                }

            },
            text = "Save & Continue",
            showEndIcon = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, top = 2.dp, bottom = 6.dp)
        )


        Spacer(modifier = Modifier.height(5.dp))
    }

    CitySelectionScreen(
        showShow = showCitiesList,
        cities = uiState?.citiesList?.filterNot { it?.city.isNullOrEmpty() }
            ?.map { (it?.city ?: "").replaceFirstChar { it.uppercase() } } ?: emptyList(),
        isCitiesLoading = uiState?.isCitiesLoading,
        selectedCity = uiState?.profileRequestBody?.city,
        onDismiss = {
            showCitiesList = false
        },
        onCitySelected = {city ->
            showCitiesList = false
            signUpViewModel.updateState { state ->
                state.copy(profileRequestBody = state.profileRequestBody?.copy(city = city))
            }
        },
    )

    ImageOptionsSelector(showDialog = showImageSelectorDialog, onDismiss = {
        showImageSelectorDialog = false
    }, onCamera = {
        showImageSelectorDialog = false
        fileUri?.let {
            takePictureLauncher.launch(it)
        }
    }, onGallery = {
        showImageSelectorDialog = false
        galleryLauncher.launch("image/*")
    })



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