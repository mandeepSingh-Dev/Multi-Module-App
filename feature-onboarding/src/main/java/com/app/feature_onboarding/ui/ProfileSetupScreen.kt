package com.app.feature_onboarding.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.app.core_common.extensions.createFolder
import com.app.core_ui.R
import com.app.core_ui.components.*
import com.app.core_ui.ui.theme.*
import com.zodiaq.ui.theme.Shape_15
import java.io.File

@Composable
fun ProfileSetupScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit
) {
    val context = LocalContext.current
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSelector by remember { mutableStateOf(false) }
    
    // File URI for camera
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) profilePicUri = tempUri
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) profilePicUri = uri
    }

    LaunchedEffect(Unit) {
        val fileName = "profile_temp_${System.currentTimeMillis()}.jpg"
        val folderFile = File(context.cacheDir, "images").createFolder()
        val file = File(folderFile, fileName)
        tempUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    BoxCommon(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                Image(painterResource(R.drawable.back), contentDescription = null,
                    modifier = Modifier.padding(top = 2.dp).clickable { onBack() })
                Column(modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Profile Setup", fontFamily = HAFFER_TRIAL_BOLD, fontSize = 20.sp)
                    Text("Create your profile and tell us a little about yourself.", fontSize = 12.sp, color = grey_5)
                }
                OutlineButton2(onClick = onSkip,
                    text = "Skip",
                    isBorder = true,
                    shape = CircleShape,
                    horizontalPadding = 20.dp,
                    verticalPadding = 8.dp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Stepper
                SteppersView(currentStep = 1, modifier = Modifier.padding(top = 24.dp, horizontal = 10.dp))

                GradientText(
                    text = "Add Profile Photo",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    text = "Add a clear photo so hosts can recognize you",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp, horizontal = 30.dp)
                )

                // Profile Photo Placeholder
                Box(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .strokeGapModifier(cornerRadius = 80.dp)
                            .padding(10.dp)
                    ) {
                        AsyncImageWithLoader(
                            image = profilePicUri ?: "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                    
                    // Camera Icon Overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-5).dp, y = (-5).dp)
                            .size(45.dp)
                            .clickable { showImageSelector = true }
                            .background(Color.White, CircleShape)
                            .padding(5.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.camera_icon),
                            contentDescription = "Upload Photo",
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    }
                }

                Text(
                    text = "JPG, PNG up to 5MB",
                    color = grey_5,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )

                // Tips Section
                Row(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth()
                        .background(Color.White, Shape_15)
                        .padding(15.dp)
                ) {
                    TipItem(
                        icon = R.drawable.clear_face_iconobackground,
                        title = "Clear face",
                        desc = "Make sure your face is visible",
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight().padding(vertical = 7.dp).width(1.dp),
                        color = grey_2
                    )
                    TipItem(
                        icon = R.drawable.connecting_gift_icongood_lighting,
                        title = "Good lighting",
                        desc = "Well-lit photos look best",
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight().padding(vertical = 7.dp).width(1.dp),
                        color = grey_2
                    )
                    TipItem(
                        icon = R.drawable.connecting_gift_iconbe_yourself,
                        title = "Be yourself",
                        desc = "Natural photos build trust",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

            // Bottom Button
            GradientButton(
                onClick = onNext,
                text = "Next",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                showEndIcon = true
            )
        }
    }

    ImageOptionsSelector(
        showDialog = showImageSelector,
        onDismiss = { showImageSelector = false },
        onCamera = {
            showImageSelector = false
            tempUri?.let { cameraLauncher.launch(it) }
        },
        onGallery = {
            showImageSelector = false
            galleryLauncher.launch("image/*")
        }
    )
}

@Composable
fun TipItem(
    icon: Int,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null
        )
        Text(title, fontFamily = HAFFER_TRIAL_SEMI_BOLD, textAlign = TextAlign.Center, fontSize = 12.sp)
        Text(desc, fontSize = 11.sp, textAlign = TextAlign.Center, lineHeight = 13.sp, color = grey_5)
    }
}

@Composable
fun SteppersView(
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val photoStepIcon = if (currentStep == 1) R.drawable.photo_step_selected else R.drawable.photo_step_done
        val nameStepIcon = if (currentStep == 2) R.drawable.name_step_selected else if (currentStep > 2) R.drawable.name_step_done else R.drawable.name_step_not_selected
        val genderStepIcon = if (currentStep == 3) R.drawable.gender_step_selected else if (currentStep > 3) R.drawable.gender_step_done else R.drawable.gender_step_not_selected
        val interestStepIcon = if (currentStep == 4) R.drawable.interested__step_selected else R.drawable.gender_step_not_selected

        Image(painterResource(photoStepIcon), contentDescription = null, modifier = Modifier.weight(1f).aspectRatio(1f))
        LinearProgressIndicator(progress = { 0f }, color = theme_color_1, trackColor = theme_color_1, modifier = Modifier.weight(1f, false).height(1.dp))
        Image(painterResource(nameStepIcon), contentDescription = null, modifier = Modifier.weight(1f).aspectRatio(1f))
        LinearProgressIndicator(progress = { 0f }, color = theme_color_1, modifier = Modifier.weight(1f, false).height(1.dp))
        Image(painterResource(genderStepIcon), contentDescription = null, modifier = Modifier.weight(1f).aspectRatio(1f))
        LinearProgressIndicator(progress = { 0f }, color = theme_color_1, modifier = Modifier.weight(1f, false).height(1.dp))
        Image(painterResource(interestStepIcon), contentDescription = null, modifier = Modifier.weight(1f).aspectRatio(1f))
    }
}
