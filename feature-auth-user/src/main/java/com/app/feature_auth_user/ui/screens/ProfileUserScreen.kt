package com.app.feature_auth_user.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_ui.components.GenderCard
import com.app.core_common.extensions.createFolder
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView
import com.app.core_ui.components.ImageOptionsSelector
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.interestCard
import com.app.core_ui.components.strokeGapModifier
import com.app.core_ui.enums.GenderType
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.grey_2
import com.app.core_ui.ui.theme.grey_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.feature_auth_user.ui.events.ProfileScreenEvents
import com.app.feature_auth_user.ui.viewmodels.ProfileUserViewModel
import com.uae.core_network.data.model.Interest
import com.zodiaq.ui.theme.Shape_15
import java.io.File


enum class ProfileStepper(val value: Int) {
    PHOTO(1),
    NAME(2),
    GENDER(3),
    INTERESTS(4)
}


@Composable
fun ProfileUserScreen(
    dob: String?,
    step: Int? ,
    backstack : NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    profileUserViewModel: ProfileUserViewModel = hiltViewModel()
) {

    val localKeyboardController = LocalSoftwareKeyboardController.current

    var currentStep by remember { mutableIntStateOf((step?.plus(1)) ?: ProfileStepper.PHOTO.value) }

    val uiState by profileUserViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(dob) {
        profileUserViewModel.updateState { state ->
            state.copy(profileRequestBody = state.profileRequestBody?.copy(birthDate = dob))
        }
    }


    BackHandler(enabled = (currentStep > ProfileStepper.PHOTO.value)) {
            currentStep = currentStep.minus(1).coerceAtLeast(ProfileStepper.PHOTO.value)
    }

    ObserveUIEvents(profileUserViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is ProfileScreenEvents.ProfileStepSuccess -> {
                when (currentStep) {
                    ProfileStepper.PHOTO.value -> {
                        currentStep = ProfileStepper.NAME.value
                    }
                    ProfileStepper.NAME.value -> {
                        currentStep = ProfileStepper.GENDER.value
                    }
                    ProfileStepper.GENDER.value -> {
                        currentStep = ProfileStepper.INTERESTS.value
                    }
                    else -> {
                        backstack.removeAll(backstack)
                        backstack.add(AppScreens.HomeBottomContainerScreen)
                    }
                }
            }
        }
    }


    val context = LocalContext.current


    var showImageSelectorDialog by remember { mutableStateOf(false) }
    var fileUri: Uri? by remember { mutableStateOf(null) }

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
                    profileUserViewModel.updateState { state ->
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

                profileUserViewModel.updateState { state ->
                    state.copy(profileRequestBody = state.profileRequestBody?.copy(profilePic = uri.toString()))
                }
            }
        }




    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            HeaderView(onSkip = {
                backstack.removeAll(backstack)
                backstack.add(AppScreens.HomeBottomContainerScreen)
            },
                onBack = {
                    backstack.removeLastOrNull()
                })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SteppersView(
                    currentStep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .padding(top = 30.dp)
                )

                AnimatedContent(currentStep) { currentStep ->
                    Log.d("fbknfkbnfk", currentStep.toString())
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (currentStep) {
                            ProfileStepper.PHOTO.value -> ProfilePhotoView(
                                uiState?.profileRequestBody?.profilePic ?: "",
                                onClick = {
                                    showImageSelectorDialog = true
                                }
                            )

                            ProfileStepper.NAME.value -> ProfileNameView(
                                name = uiState?.profileRequestBody?.fullName ?: "",
                                onChangeName = {
                                    profileUserViewModel.updateState { state ->

                                        state.copy(
                                            profileRequestBody = state.profileRequestBody?.copy(
                                                fullName = it
                                            )
                                        )
                                    }
                                },
                                onDone = {
                                    localKeyboardController?.hide()
                                    profileUserViewModel.setProfile(ProfileStepper.NAME.value)
                                }
                            )

                            ProfileStepper.GENDER.value -> ProfileGenderView(uiState?.profileRequestBody?.gender ?: GenderType.MALE.type,
                                onChangeGender = {
                                    profileUserViewModel.updateState { state ->
                                        state.copy(profileRequestBody = state.profileRequestBody?.copy(gender = it))
                                    }
                                })
                            else -> ProfileInterestView(uiState?.interests, onChangeInterest = {interest ->
                                profileUserViewModel.updateState { state ->
                                    val mInterest = state.interests?.map {
                                        if(interest._id == it._id){
                                            it.copy(isSelected = !interest.isSelected)
                                        }else{
                                            it
                                        }
                                    }
                                    state.copy(interests = mInterest)
                                }
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
            GradientButton(
                onClick = {
             localKeyboardController?.hide()
                    profileUserViewModel.setProfile(step = currentStep)
//                    currentStep = when(currentStep){
//                        ProfileStepper.PHOTO.value -> ProfileStepper.NAME.value
//                        ProfileStepper.NAME.value -> ProfileStepper.GENDER.value
//                        ProfileStepper.GENDER.value -> ProfileStepper.INTERESTS.value
//                        ProfileStepper.INTERESTS.value -> ProfileStepper.INTERESTS.value
//                        else -> ProfileStepper.INTERESTS.value
//                    }
                },
                text = if (currentStep == ProfileStepper.INTERESTS.value) "Done" else "Next",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                showEndIcon = true
            )
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

}

@Composable
private fun itemCard(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    icon: Int
) {
    Column(
        modifier = modifier
            .background(color = Color.Transparent)
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null
        )
        Text(title, fontFamily = HAFFER_TRIAL_SEMI_BOLD, textAlign = TextAlign.Center)
        Text(desc, fontSize = 11.sp, textAlign = TextAlign.Center, lineHeight = 13.sp)
    }
}

@Composable
fun SteppersView(
    currentStep: Int,
    modifier: Modifier
)
{

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val photoStepIcon =
            if (currentStep == ProfileStepper.PHOTO.value) R.drawable.photo_step_selected else R.drawable.photo_step_done

        val nameStepIcon = if (currentStep == ProfileStepper.NAME.value) {
            R.drawable.name_step_selected
        } else if (currentStep > ProfileStepper.NAME.value) {
            R.drawable.name_step_done
        } else {
            R.drawable.name_step_not_selected
        }

        val genderStepIcon = if (currentStep == ProfileStepper.GENDER.value) {
            R.drawable.gender_step_selected
        } else if (currentStep > ProfileStepper.GENDER.value) {
            R.drawable.gender_step_done
        } else {
            R.drawable.gender_step_not_selected
        }

        val interestStepIcon = if (currentStep == ProfileStepper.INTERESTS.value) {
            R.drawable.interested__step_selected
        } else {
            R.drawable.gender_step_not_selected
        }


        Image(
            painter = painterResource(photoStepIcon), contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
        LinearProgressIndicator(
            progress = { 0f },
            color = theme_color_1,
            gapSize = 0.dp,
            trackColor = theme_color_1,
            drawStopIndicator = {},
            modifier = Modifier
                .weight(1f, false)
                .height(1.dp)
        )
        Image(
            painter = painterResource(nameStepIcon), contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
        LinearProgressIndicator(
            progress = { 0f },
            color = theme_color_1, gapSize = 0.dp,
            modifier = Modifier
                .weight(1f, false)
                .height(1.dp),
            drawStopIndicator = {},
        )
        Image(
            painter = painterResource(genderStepIcon),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
        LinearProgressIndicator(
            progress = { 0f },
            color = theme_color_1,
            gapSize = 0.dp,
            drawStopIndicator = {},
            modifier = Modifier
                .weight(1f, false)
                .height(1.dp)
        )
        Image(
            painter = painterResource(interestStepIcon),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )

    }
}


@Composable
private fun ColumnScope.ProfilePhotoView(
    profilePic: String,
    onClick: () -> Unit
) {
    GradientText(
        text = "Add Profile Photo", fontSize = 20.sp,
        modifier = Modifier.padding(top = 30.dp)
    )
    Text(
        "Add a clear photo so hosts can recognize you", textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
    )

    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 30.dp)
            .padding(horizontal = 30.dp)
    )
    {
        Box(
            modifier = Modifier
                .width(150.dp)
                .aspectRatio(1f)
                .strokeGapModifier(cornerRadius = 200.dp)
                .padding(10.dp)
        ) {
            AsyncImageWithLoader(
                profilePic ?: "",
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape),
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(x = (-5).dp, y = (-5).dp)
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
                painter = painterResource(R.drawable.camera_icon), contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClick()
                    })
        }
    }

    Text(
        "JPG, PNG up to 5MB",
        color = grey_5,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 20.dp)
    )


    Row(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .dropShadow(
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 10.dp),
                shape = Shape_15
            )
            .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)
    )
    {

        itemCard(
            modifier = Modifier.weight(1f),
            title = "Clear Face",
            desc = "Make sure your face is visible",
            icon = R.drawable.clear_face_iconobackground,
        )
        VerticalDivider(
            thickness = 1.dp,
            color = grey_2,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 7.dp)
        )
        itemCard(
            modifier = Modifier.weight(1f),
            title = "Good lighting",
            desc = "Well-lit photos look best",
            icon = R.drawable.connecting_gift_icongood_lighting,
        )
        VerticalDivider(
            thickness = 1.dp,
            color = grey_2,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 7.dp)
        )
        itemCard(
            modifier = Modifier.weight(1f),
            title = "Be Yourself",
            desc = "Natural photos build trust",
            icon = R.drawable.connecting_gift_iconbe_yourself,
        )
    }
}

@Composable
private fun ProfileNameView(
    name: String?,
    onChangeName: (String?) -> Unit,
    onDone: () -> Unit,
) {
    GradientText(
        text = "What's your name?", fontSize = 20.sp,
        modifier = Modifier.padding(top = 30.dp)
    )
    Text(
        "This is how others will see you in Kahoji", textAlign = TextAlign.Center,
        fontSize = 13.sp,
        modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
    )
    TextFieldOuterLabel(
        label = "Full Name",
        isRequired = true,
        isBorder = true,
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.placeholderbackground),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
        },
        keyboardActions = KeyboardActions(onDone = {onDone()}),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.PersonName, imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.Words),
        hint = "Enter your full name",
        value = name ?: "",
        onValueChange = onChangeName,
        modifier = Modifier.padding(top = 25.dp)
    )

}


@Composable
private fun ProfileGenderView(selectedGender : Int,
                              onChangeGender : (Int) -> Unit) {
    GradientText(
        text = "Select your Gender", fontSize = 20.sp,
        modifier = Modifier.padding(top = 30.dp)
    )
    Text(
        "This help us create a safe and comfortable community", textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
    )
    Spacer(modifier = Modifier.height(15.dp))
    GenderType.entries.forEach { gender ->
        GenderCard(
            modifier = Modifier.padding(top = 10.dp),
            genderType = gender.type,
            isSelected = selectedGender == gender.type,
            onClick = {
                onChangeGender(gender.type)
            })

    }
}





@Composable
private fun ProfileInterestView(interests: List<Interest>?,
                                            onChangeInterest : (Interest) -> Unit) {

    GradientText(
        text = "What are you interested In?", fontSize = 20.sp,
        modifier = Modifier.padding(top = 30.dp)
    )
    Text(
        "Choose up to 5 interests", textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
    )
    Spacer(modifier = Modifier.height(15.dp))

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3,
    ) {
        interests?.forEach {
            interestCard(
                name = it.name ?: "",
                selected = it.isSelected,
                onSelect = {
                    onChangeInterest(it)
                }
            )
        }
    }

}


