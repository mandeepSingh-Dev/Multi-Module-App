/*
package com.app.feature_auth_host.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.createFolder
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_common.utils.localeUtils.toLocalDateOrNull
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.DatePickerCustomDialog
import com.app.core_ui.components.DropDownTextField
import com.app.core_ui.components.GenderCard2
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ImageOptionsSelector
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.TextFieldOuterLabel2
import com.app.core_ui.components.TextWithAsterisk
import com.app.core_ui.components.interestCard
import com.app.core_ui.components.padding
import com.app.core_ui.components.strokeGapModifier
import com.app.core_ui.enums.GenderType
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_8
import com.app.feature_auth_host.ui.screens.signup_steps.languageCard
import com.app.feature_auth_host.ui.viewmodels.ProfileHostUpdateScreenViewModel
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.zodiaq.ui.theme.Shape_20
import java.io.File
import java.util.Date
import kotlin.collections.chunked
import kotlin.collections.forEach


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHostUpdateScreen(
    backstack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    profileUpdateScreenViewModel: ProfileHostUpdateScreenViewModel = hiltViewModel()
) {

    val localKeyboardController = LocalSoftwareKeyboardController.current
    val uiState by profileUpdateScreenViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    ObserveUIEvents(profileUpdateScreenViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
        }
    }
    var showDOBDialog by remember { mutableStateOf(false) }


    val dobPickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    LaunchedEffect(uiState?.profileRequestBody?.birthDate) {
        dobPickerState.setSelectedDate(uiState?.profileRequestBody?.birthDate?.toLocalDateOrNull())
    }



    val state = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "IN"
    )

    var showImageSelectorDialog by remember { mutableStateOf(false) }
    var fileUri: Uri? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        var fileName = "profile_image_${System.currentTimeMillis()}"
        if (fileName.isNotEmpty()) {
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
                    profileUpdateScreenViewModel.updateState { state ->
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
                profileUpdateScreenViewModel.updateState { state ->
                    state.copy(profileRequestBody = state.profileRequestBody?.copy(profilePic = uri.toString()))
                }
            }
        }


    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {

            HeaderView2(title = "Profile")

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            )
            {
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
                        AsyncImageWithLoader(
                            image = uiState?.profileRequestBody?.profilePic ?: "",
                            modifier = Modifier
                                .matchParentSize()
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
                        profileUpdateScreenViewModel.updateState { state ->
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
                    fontFamily = HAFFER_TRIAL_MEDIUM,
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
                        ?: uiState?.profileRequestBody?.birthDate?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_9) ?: "",
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


                TextWithAsterisk(
                    text = "Mobile Number",
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
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
                        enabled = false,
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
                        enabled = false,
                        hint = "Enter phone number",
                        value = uiState?.profileRequestBody?.phone ?: "",
                        onValueChange = {
                            profileUpdateScreenViewModel.updateState { state ->
                                state.copy(
                                    profileRequestBody = state.profileRequestBody?.copy(
                                        phone = it
                                    )
                                )
                            }
                        },
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
                }

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

                TextWithAsterisk(
                    text = "Gender",
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GenderType.entries.forEach { gender ->
                        GenderCard2(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp),
                            genderType = gender.type,
                            isSelected = uiState?.profileRequestBody?.gender == gender.type,
                            onClick = {
                                profileUpdateScreenViewModel.updateState { state ->
                                    state.copy(
                                        profileRequestBody = state.profileRequestBody?.copy(
                                            gender = gender.type
                                        )
                                    )
                                }
                            })
                    }
                }


                Text("Popular Languages", fontFamily = HAFFER_TRIAL_SEMI_BOLD)

                Spacer(modifier = Modifier.height(5.dp))
                AnimatedVisibility(uiState?.languagesLoading ?: false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }

                if(!uiState?.languages.isNullOrEmpty()) {
                    uiState.languages.chunked(3).forEach {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                                .padding(horizontal = 20.dp, top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            it.forEach { item ->
                                languageCard(
                                    modifier = Modifier.weight(1f)
                                        .fillMaxHeight(),
                                    isSelected = item?.isSelected ?: false,
                                    language = item,
                                    onChange = {
                                        signUpViewModel.updatesLanguages(item)
                                    }
                                )
                            }
                            if (it.size < 2) {
                                Box(modifier = Modifier.weight(1f))
                                Box(modifier = Modifier.weight(1f))
                            } else if (it.size < 3) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                TextWithAsterisk(text = "Interests", modifier = Modifier.fillMaxWidth().padding(top = 20.dp),)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3,
                )
                {
                    uiState?.interests?.forEach { interest ->
                        interestCard(
                            interest.name,
                            selected = interest.isSelected,
                            onSelect = {
                                val mList = uiState?.interests?.toMutableList()
                                val index = mList?.indexOfFirst { it._id == interest._id } ?: -1
                                if (index != -1) {
                                    mList?.set(index, interest.copy(isSelected = !interest.isSelected))
                                    profileUpdateScreenViewModel.updateState { state ->
                                        state.copy(interests = mList)
                                    }
                                }

                            },
                        )

                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }

            GradientButton(onClick = {
                profileUpdateScreenViewModel.updateProfile(countryCode = state.getCountryPhoneCode())
            },
                text = "Save",
                showEndIcon = true,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 5.dp))
        }

    }

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

}*/