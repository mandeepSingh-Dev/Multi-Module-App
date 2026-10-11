package com.app.feature_withdraw.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.OTPBottomSheetScreen
import com.app.feature_withdraw.ui.events.IdentityVerificationScreenEvents
import com.app.feature_withdraw.ui.face_verification_ml_kit.FaceCameraVerificationScreen
import com.app.feature_withdraw.ui.viewmodels.IdentityVerificationViewModel
import com.app.feature_withdraw.ui.voice_recording.RecState
import com.app.feature_withdraw.ui.voice_recording.VoiceRecorderScreen


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay


enum class VerificationStep {
    IDENTITY,
    FACE_SCAN,
    COMPLETED
}

@Composable
@Preview
fun IdentityVerificationScreen(
    backstack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    identityVerificationViewModel: IdentityVerificationViewModel = hiltViewModel()
) {

    val uiState by identityVerificationViewModel.uiState.collectAsStateWithLifecycle()

    var currentStep by remember {
        mutableStateOf(
            VerificationStep.IDENTITY
        )
    }

    var showFaceCameraScreen by remember { mutableStateOf(false) }
    var showFaceCameraScreenCompletedDialogUrl by remember { mutableStateOf<String?>(null) }
    var showVoiceCameraScreenCompletedDialogUrl by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    var showOtpView by rememberSaveable { mutableStateOf(false) }

    ObserveUIEvents(identityVerificationViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is IdentityVerificationScreenEvents.MobileVerificationStarted -> {
                showOtpView = true
            }

            is IdentityVerificationScreenEvents.OTPVerifiedSuccessfully -> {
                showOtpView = false
                currentStep = VerificationStep.FACE_SCAN
            }

            is IdentityVerificationScreenEvents.OnGetVerifiedImageFromMLKit -> {
                identityVerificationViewModel.faceVerificationStart(
                    uIEvent.verifiedImageFile?.toUri().toString()
                )
                showFaceCameraScreen = false
            }

            is IdentityVerificationScreenEvents.ShowFaceVerificationDoneDialog -> {
                showFaceCameraScreenCompletedDialogUrl = uIEvent.imageUrl
            }

            is IdentityVerificationScreenEvents.OnFaceVerificationAllDone -> {
                showFaceCameraScreenCompletedDialogUrl = null
                currentStep = VerificationStep.COMPLETED
            }


            is IdentityVerificationScreenEvents.OnGetVoiceRecording -> {
                identityVerificationViewModel.voiceVerificationStart(
                    uIEvent.recordedVoiceFile?.toUri().toString()
                )
                showFaceCameraScreen = false
            }

            is IdentityVerificationScreenEvents.ShowVoiceVerificationDoneDialog -> {
                showVoiceCameraScreenCompletedDialogUrl = uIEvent.voiceUrl
            }

            is IdentityVerificationScreenEvents.OnVoiceVerificationAllDone -> {
                showVoiceCameraScreenCompletedDialogUrl = null
                backstack.removeLastOrNull()
            }
        }

    }

    BackHandler(showFaceCameraScreen) {
        if (showFaceCameraScreen) {
            showFaceCameraScreen = false
        } else {
            backstack.removeLastOrNull()
        }
    }

    BoxCommon(
        isLoading = uiState?.isFaceVerificationStartInLoading ?: false || uiState?.isVoiceVerificationStartInLoading ?: false,
        modifier = Modifier
            .fillMaxSize()
            .blur(if (showOtpView || !showFaceCameraScreenCompletedDialogUrl.isNullOrEmpty() || !showVoiceCameraScreenCompletedDialogUrl.isNullOrEmpty()) 5.dp else 0.dp),
        isLoaderTappable = true
    )
    {


        AnimatedContent(showFaceCameraScreen) { showFaceCamera ->
            if (showFaceCamera) {
                FaceCameraVerificationScreen(onVerified = { imageFile ->
                    identityVerificationViewModel.onEvent(
                        IdentityVerificationScreenEvents.OnGetVerifiedImageFromMLKit(
                            imageFile
                        )
                    )
                })
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp)
                )
                {
                    HeaderView2(
                        title = "Identity Verifications",
                        secTitle = "Convert your Beans into real earnings"
                        , onNotification = {
                            backstack.add(AppScreens.NotificationListScreen)
                        } )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {

                        VerificationStepper(
                            currentStep = currentStep
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        AnimatedContent(currentStep) { currentStep ->
                            when (currentStep) {
                                VerificationStep.IDENTITY -> {
                                    MobileVerification(uiState)
                                }

                                VerificationStep.FACE_SCAN -> {
                                    FaceVerificationStep()
                                }

                                VerificationStep.COMPLETED -> {
                                    VoiceRecorderScreen(
                                        onRecorded = {
                                            identityVerificationViewModel.voiceVerificationStart(
                                                it.toUri().toString()
                                            )
                                        },
                                        onState = { recState ->
                                            identityVerificationViewModel.updateState { state ->
                                                state.copy(recordingState = recState)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(currentStep != VerificationStep.COMPLETED) {
                        GradientButton(
                            onClick = {
                                when (currentStep) {
                                    VerificationStep.IDENTITY -> {
                                        identityVerificationViewModel.onEvent(
                                            IdentityVerificationScreenEvents.MobileVerificationStarted
                                        )
                                    }

                                    VerificationStep.FACE_SCAN -> {
                                        showFaceCameraScreen = true
                                    }

                                    VerificationStep.COMPLETED -> VerificationStep.COMPLETED
                                }

                            },
                            text = when (currentStep) {
                                VerificationStep.IDENTITY -> {
                                    "Continue"
                                }

                                VerificationStep.FACE_SCAN -> {
                                    "Open Camera"
                                }

                                VerificationStep.COMPLETED -> {
                                    "Start Speaking"
                                }
                            },
                            showEndIcon = true,
                            enabled = uiState?.isMobileVerificationLoading == false && uiState?.isOTPVerificationLoading == false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }


        OTPBottomSheetScreen(
            showOtpView = showOtpView,
            otpError = uiState?.otpError,
            isVerifyInProgress = uiState?.isOTPVerificationLoading ?: false,
            onCancel = {
                showOtpView = false
            },
            onDismiss = {
                showOtpView = false
            },
            onVerify = { otp ->
                identityVerificationViewModel.verifyOtp(otp)
            }
        )
    }

    ButtonsDialog(
        showDialog = !showFaceCameraScreenCompletedDialogUrl.isNullOrEmpty(),
        onDismiss = {
            showFaceCameraScreenCompletedDialogUrl = null
        },
        title = "Face Details Captured",
        showLoader = uiState?.isFaceVerificationCompleteInLoading ?: false,
        description = "Your face details have been captured successfully. Tap Done to proceed.",
        positiveBtnText = "Done",
        onPositiveClick = {
            identityVerificationViewModel.faceVerificationCompleted(
                showFaceCameraScreenCompletedDialogUrl
            )
        },
        showBothButtons = false,
    )
    ButtonsDialog(
        showDialog = !showVoiceCameraScreenCompletedDialogUrl.isNullOrEmpty(),
        onDismiss = {
            showVoiceCameraScreenCompletedDialogUrl = null
        },
        title = "Voice Verification Details Captured",
        showLoader = uiState?.isVoiceVerificationCompleteInLoading ?: false,
        description = "Your voice details have been captured successfully. Tap Done to proceed.",
        positiveBtnText = "Done",
        onPositiveClick = {
            identityVerificationViewModel.voiceVerificationCompleted(
                showVoiceCameraScreenCompletedDialogUrl
            )
        },
        showBothButtons = false,
    )
}




private val DialogPurple = Color(0xFF8B5CF6)
private val DialogViolet = Color(0xFF6D28D9)
private val DialogGold = Color(0xFFFFC66D)
private val DialogBackground = Color(0xFF141326)
private val DialogMuted = Color(0xFFAAA8C9)
private val DialogGreen = Color(0xFF65E6A0)

@Composable
@Preview
fun VerificationSubmittedDialog(
    visible: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    if (!visible) return

    var entered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(80)
        entered = true
    }

    val scale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.86f,
        animationSpec = spring(
            dampingRatio = 0.72f,
            stiffness = 300f
        ),
        label = "dialogScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(350),
        label = "dialogAlpha"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
        ) {
            val compact = maxHeight < 650.dp

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxHeight * 0.94f)
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(30.dp),
                        ambientColor = DialogPurple.copy(alpha = 0.25f),
                        spotColor = DialogPurple.copy(alpha = 0.35f)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF211D3B),
                                DialogBackground,
                                Color(0xFF191329)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                DialogPurple.copy(alpha = 0.8f),
                                Color.White.copy(alpha = 0.12f),
                                DialogGold.copy(alpha = 0.35f)
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 22.dp,
                        vertical = if (compact) 16.dp else 22.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SECURE VERIFICATION",
                        color = DialogMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(34.dp)
                            .background(
                                Color.White.copy(alpha = 0.07f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.height(if (compact) 12.dp else 20.dp))

                // Animated shield
                AnimatedShield(
                    compact = compact
                )

                Spacer(Modifier.height(18.dp))

                // Gradient title
                Text(
                    text = "Verification",
                    fontSize = if (compact) 25.sp else 29.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Submitted!",
                    fontSize = if (compact) 25.sp else 29.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    style = LocalTextStyle.current.copy(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFE9D5FF),
                                DialogGold,
                                Color(0xFFC4B5FD)
                            )
                        )
                    )
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Your mobile, face and voice verification details have been submitted successfully.",
                    color = DialogMuted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(22.dp))

                // Completed steps
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.075f),
                                    Color.White.copy(alpha = 0.025f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.08f),
                            RoundedCornerShape(22.dp)
                        )
                        .padding(16.dp)
                ) {
                    VerificationStep(
                        icon = Icons.Default.Phone,
                        title = "Mobile Verification",
                        subtitle = "Phone verification completed",
                        delayMillis = 150
                    )

                    VerificationStep(
                        icon = Icons.Default.Face,
                        title = "Face Verification",
                        subtitle = "Face details submitted",
                        delayMillis = 300
                    )

                    VerificationStep(
                        icon = Icons.Default.Face,
                        title = "Voice Verification",
                        subtitle = "Voice sample submitted",
                        delayMillis = 450,
                        isLast = true
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Under-review card
                ReviewStatusCard()

                Spacer(Modifier.height(18.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color(0xFFC4B5FD),
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "We'll notify you when the review is complete.",
                        color = DialogMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(Modifier.height(22.dp))

                // Premium CTA
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = DialogPurple,
                            spotColor = DialogPurple
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF7C3AED),
                                        Color(0xFF9F67FF),
                                        Color(0xFF6D28D9)
                                    )
                                ),
                                RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Got it",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(Modifier.width(10.dp))

                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun AnimatedShield(
    compact: Boolean
) {
    val infinite = rememberInfiniteTransition(
        label = "shieldAnimation"
    )

    val glow by infinite.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.48f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shieldGlow"
    )

    val rotation by infinite.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shieldRotation"
    )

    Box(
        modifier = Modifier
            .size(if (compact) 100.dp else 120.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Box(
            Modifier
                .size(112.dp)
                .scale(0.9f + glow * 0.15f)
                .background(
                    Brush.radialGradient(
                        listOf(
                            DialogPurple.copy(alpha = glow),
                            DialogViolet.copy(alpha = glow * 0.25f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        // Orbital rings
        Box(
            Modifier
                .size(94.dp)
                .rotate(rotation)
                .border(
                    1.dp,
                    DialogPurple.copy(alpha = 0.8f),
                    CircleShape
                )
        )

        Box(
            Modifier
                .size(78.dp)
                .rotate(-rotation)
                .border(
                    1.dp,
                    DialogGold.copy(alpha = 0.45f),
                    CircleShape
                )
        )

        // Shield surface
        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow(
                    20.dp,
                    CircleShape,
                    ambientColor = DialogGold.copy(alpha = glow),
                    spotColor = DialogGold.copy(alpha = glow)
                )
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFFFE4A3),
                            Color(0xFFFFBE55),
                            Color(0xFFDA8B26)
                        )
                    ),
                    CircleShape
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.8f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Verification submitted",
                tint = Color(0xFF42246D),
                modifier = Modifier.size(39.dp)
            )
        }

        // Floating sparkle
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-4).dp, y = 8.dp)
                .size(10.dp)
                .background(DialogGold, CircleShape)
                .shadow(8.dp, CircleShape, spotColor = DialogGold)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 8.dp, y = (-12).dp)
                .size(7.dp)
                .background(Color(0xFFC4B5FD), CircleShape)
        )
    }
}

@Composable
private fun VerificationStep(
    icon: ImageVector,
    title: String,
    subtitle: String,
    delayMillis: Long,
    isLast: Boolean = false
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delayMillis)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(350)) +
                slideInHorizontally(
                    initialOffsetX = { it / 5 },
                    animationSpec = tween(350)
                )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(43.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF8B5CF6),
                                Color(0xFF5B21B6)
                            )
                        ),
                        CircleShape
                    )
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = DialogMuted,
                    lineHeight = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Submitted",
                tint = DialogGreen,
                modifier = Modifier
                    .size(22.dp)
                    .shadow(
                        8.dp,
                        CircleShape,
                        spotColor = DialogGreen.copy(alpha = 0.6f)
                    )
            )
        }
    }
}

@Composable
private fun ReviewStatusCard() {
    val infinite = rememberInfiniteTransition(
        label = "reviewPulse"
    )

    val pulse by infinite.animateFloat(
        initialValue = 0.94f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "reviewPulseScale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = pulse
                scaleY = pulse
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF49311F),
                        Color(0xFF32253A),
                        Color(0xFF292039)
                    )
                )
            )
            .border(
                1.dp,
                DialogGold.copy(alpha = 0.48f),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFFFD68A),
                            Color(0xFFDE8F35)
                        )
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color(0xFF59351C),
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "CURRENT STATUS",
                color = DialogMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Under Review",
                color = DialogGold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Our team is reviewing your details.",
                color = Color(0xFFD3C6DB),
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }

        Spacer(Modifier.width(5.dp))

        Surface(
            color = DialogGold.copy(alpha = 0.12f),
            shape = RoundedCornerShape(50),
            border = BorderStroke(
                1.dp,
                DialogGold.copy(alpha = 0.25f)
            )
        ) {
            Text(
                text = "PENDING",
                color = DialogGold,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = 9.dp,
                    vertical = 7.dp
                )
            )
        }
    }
}
