package com.app.feature_call.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.SocketHostEventsHandler
import com.app.core_common.utils.enums.CallType
import com.app.core_common.utils.enums.UserType
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.AsyncProfileImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.FilledCustomButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.UIRequirePermissions
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush5
import com.app.core_ui.ui.theme.green_3
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.red
import com.app.core_ui.ui.theme.red_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_12
import com.app.core_ui.ui.theme.theme_color_22
import com.app.core_ui.ui.theme.theme_color_6
import com.app.core_ui.ui.theme.theme_color_9
import com.app.feature_call.ui.states.VideoCallScreenEvents
import com.app.feature_call.ui.states.VideoCallScreenUIState
import com.app.feature_call.ui.viewmodels.VideoCallViewModel
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.app.feature_gift.ui.GiftNotificationCard
import com.app.feature_gift.ui.GiftSentScreen
import com.app.feature_gift.ui.SendGiftScreen
import com.app.feature_gift.ui.events.GiftEvents
import com.uae.core_socket.model.CurrentEarnResponse
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_20
import kotlin.math.absoluteValue

@Composable
fun CallingScreen(
    videoCallViewModel: VideoCallViewModel,
    getAreUsersJoined: () -> Boolean,
    onFinish: () -> Unit
) {

    val context = LocalContext.current

    val uiState by videoCallViewModel.uiState.collectAsStateWithLifecycle()

    var manualPermissionsLauncher by remember { mutableStateOf<(() -> Unit)?>(null) }

    var showPermissionDisclaimer by rememberSaveable { mutableStateOf(false) }

    var showEndCallDialog by rememberSaveable { mutableStateOf(false) }

    var showSendGiftSheet by remember { mutableStateOf(false) }
    var showGiftSentSuccessfully by remember { mutableStateOf<GiftListResponse.GiftData?>(null) }


    LaunchedEffect(uiState?.localSurfaceView) {
        Log.d("dlvmdkvmnd", uiState?.localSurfaceView.toString())
    }

    LaunchedEffect(uiState?.isCallAccepted) {
        if(uiState?.isCallAccepted == true) {
            videoCallViewModel.startTimer()
        }
    }
    /* LaunchedEffect(uiState?.remoteSurfaceView) {
         Log.d("dlvmdkvmnd___222", uiState?.remoteSurfaceView.toString())
         if (uiState?.remoteSurfaceView != null) {
             videoCallViewModel.updateState { state ->
                 state?.copy(showCallingLayout = false)
             }
         }
     }*/

    var showEndCallDialogOnBackPress by remember { mutableStateOf<Pair<String, String>?>(null) }

    BackHandler(true) {
        if(uiState?.isCallAccepted == true){
            showEndCallDialogOnBackPress = Pair("End Call?" , "Are you sure you want to end this call?")
        }else{
            showEndCallDialogOnBackPress = Pair("Exit Call Screen?" , "Do you want to leave this screen?")
        }
    }


    val receivedGift by SocketHostEventsHandler.receiveGift.collectAsStateWithLifecycle(null)

    var showGiftNotification by remember(receivedGift) { mutableStateOf(receivedGift ) }
    var showFeedbackScreen by remember { mutableStateOf(false) }

    LaunchedEffect(showFeedbackScreen) {
        videoCallViewModel.isCurrentlyOnFeedbackScreen = showFeedbackScreen
    }

    ObserveUIEvents(videoCallViewModel.uiEvent) { uiEvent ->
        when (uiEvent) {

            is VideoCallScreenEvents.EndCallSuccess -> {
                Log.d("fkbnfjkbnf", uiEvent.endCallDetails.toString())
//                showCallSummary = uiEvent.endCallDetails
            }

            is CommonUIEvents.ShowMessage -> {
                context.showToast(uiEvent.message)
            }

            is VideoCallScreenEvents.ShowDefinedCustomMessage -> {
//                showCustomMessage = uiEvent.message
            }

            is GiftEvents.GiftSentSuccessfully -> {
                videoCallViewModel.getWallet()
                showGiftSentSuccessfully = uiEvent.gift
            }
            is GiftEvents.GiftSentError -> {
                context.showToast(message = uiEvent.msg)
            }
            is GiftEvents.GiftListFetchError -> {
                context.showToast(message = uiEvent.msg)
            }
            is VideoCallScreenEvents.ShowFeedbackScreen -> {
                videoCallViewModel.isFeedbackScreenShowed = true
                showFeedbackScreen = true
            }

            is CommonUIEvents.ShowError -> {
                context.showToast(message = uiEvent.error)
            }

        }
    }

    UIRequirePermissions(
        permissions = permissions,
        onPermissionGranted = {
            Log.d("fkvfkvf", "Granted")
            if (uiState?.isFromIncomingCall == false) {
                videoCallViewModel.onEvent(VideoCallScreenEvents.StartCall)
            }else{
                videoCallViewModel.onEvent(VideoCallScreenEvents.OnAcceptingIncomingVideoCall)
            }
            showPermissionDisclaimer = false
        },
        onPermissionDenied = {
            context.showToast("Permissions are required for call. Please allow all permissions")
            showPermissionDisclaimer = true

            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }, manualLaunchPermissionsOnClick = { trigger ->
            manualPermissionsLauncher = trigger
        })


    BoxCommon(
        isLoading = uiState?.isLoading ?: false,
        contentAlignment = Alignment.Center,
        containerBrush = Brush.verticalGradient(listOf(Color.White, theme_color_22, Color.White)),
        modifier = Modifier.blur(radius = if (showEndCallDialog) 50.dp else 0.dp)
    )
    {

//        if (uiState?.remoteSurfaceView != null) {

        key(uiState?.remoteSurfaceView?.hashCode()) {

            Log.d("Compose", "Creating AndroidView")

            uiState?.remoteSurfaceView?.let {
                AndroidView(
                    factory = {
                        Log.d("Compose", "factory")
                        uiState?.remoteSurfaceView!!
                    },
                    update = {
                        Log.d("Compose", "update")
                    }
                )
            }
        }
//        Log.d("kbnkfnbf", uiState?.remoteSurfaceView.hashCode().toString())
//            uiState?.remoteSurfaceView?.let { remoteView ->
//                AndroidView(factory = { remoteView }, Modifier.fillMaxSize(), update = {
//                    remoteView
//                })
//            }
//        }

        CallControllersScreen(
            uiState = uiState,
            showCallingLayout = uiState?.showCallingLayout ?: true,
            videoCallViewModel = videoCallViewModel,
            onEnd = {
                Log.d("fkbnkfb", "End Call")
                if (getAreUsersJoined()) {
                    showEndCallDialog = true
                } else {
                    videoCallViewModel.onEvent(VideoCallScreenEvents.FinishCall)
                }
            }, onAcceptIncomginCall = {
                if (uiState?.isFromIncomingCall == true) {
                    videoCallViewModel.onEvent(VideoCallScreenEvents.OnAcceptingIncomingVideoCall)
                }
            }, onDeclineIncomingCall = {
                videoCallViewModel.onEvent(VideoCallScreenEvents.RejectIncomingCall)
            },
            onGift = {
                if (uiState?.userType == UserType.USER.value) {
                    videoCallViewModel.resetGiftCounts()
                    showSendGiftSheet = true
                }
            })

        AnimatedVisibility(
            showPermissionDisclaimer,
            enter = slideInVertically(),
            exit = slideOutVertically(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .dropShadow(
                        shadow = Shadow(radius = 10.dp, color = theme_color_1),
                        shape = Shape_10
                    )
                    .background(color = theme_color_6, shape = Shape_10)
                    .innerShadow(
                        shape = Shape_10,
                        shadow = Shadow(color = theme_color_12.copy(alpha = 0.5f), radius = 10.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    "Permissions are required for call. Please allow all permissions",
                    color = Color.Black,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    modifier = Modifier.weight(1f)
                )
                FilledCustomButton(onClick = {
                    manualPermissionsLauncher?.invoke()
                }, text = "Allow", textColor = theme_color_12)
            }
        }
    }


    GiftNotificationCard(
        giftNotificationData = showGiftNotification,
        onDismiss = {
            showGiftNotification = null
        },
        modifier = Modifier
            .systemBarsPadding()
            .padding(horizontal = 20.dp, top = 10.dp)
    )



    ButtonsDialog(
        showDialog = showEndCallDialog,
        title = "End Call?",
        description = "Are you sure you want to end the call?",
        onDismiss = {
            showEndCallDialog = false
        },
        onPositiveClick = {
            showEndCallDialog = false
            videoCallViewModel.onEvent(VideoCallScreenEvents.FinishCall)
        },
        onNegativeClick = {
            showEndCallDialog = false
        })

    ButtonsDialog(
        showDialog = showEndCallDialogOnBackPress != null,
        title = showEndCallDialogOnBackPress?.first,
        description = showEndCallDialogOnBackPress?.second,
        onDismiss = {
            showEndCallDialogOnBackPress = null
        },
        onPositiveClick = {
            showEndCallDialogOnBackPress = null
            videoCallViewModel.onEvent(VideoCallScreenEvents.FinishCall)
        },
        onNegativeClick = {
            showEndCallDialogOnBackPress = null
        })

    SendGiftScreen(
        show = showSendGiftSheet,
        onDismiss = {
            showSendGiftSheet = false
        },
        isGiftsListLoading = uiState?.isGiftsListLoading,
        isGiftSending = uiState?.isGiftSending,
        giftsList = uiState?.giftsList,
        onSend = { gift ->
            videoCallViewModel.onEvent(CommonUIEvents.ShowMessage("Sending your gift to ${uiState?.personName}..."))
            videoCallViewModel.sendGift(
                receiverId = videoCallViewModel.partnerID,
                gift
            )
        },
    )

    GiftSentScreen(
        giftData = showGiftSentSuccessfully,
        onDismiss = {
            showGiftSentSuccessfully = null
        })


    AnimatedVisibility(showFeedbackScreen) {
        FeedbackScreen(
            partnerId = videoCallViewModel.partnerID,
            callId = videoCallViewModel.callID,
            profilePic = uiState?.image,
            onBack = {
                showFeedbackScreen = false
                onFinish()
            },
            onSuccess = {
                showFeedbackScreen = false
                onFinish()
            }
        )
    }


}

@Composable
@Preview
fun CallControllersScreen(
    uiState: VideoCallScreenUIState? = null,
    showCallingLayout: Boolean = true,
    videoCallViewModel: VideoCallViewModel = viewModel(),
    onEnd: () -> Unit = {},
    onDeclineIncomingCall: () -> Unit = {},
    onAcceptIncomginCall: () -> Unit = {},
    onGift: () -> Unit = {},
) {

    var showPreviewDigest by rememberSaveable { mutableStateOf(false) }
    var userType by remember { mutableStateOf(UserType.USER) }

    LaunchedEffect(Unit) {
        videoCallViewModel.onEvent(VideoCallScreenEvents.CameraOnOff(false))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

        if (uiState?.callType == CallType.Video.type) {
            VideoCallScreenHeader(
                partnerName = uiState.personName,
                profilePic = uiState.image,
                callType = uiState.callType,
                modifier = Modifier.padding(horizontal = 20.dp),
                timerValue = uiState.timerValue ?: ""

            )
            AnimatedVisibility(!uiState.callAcceptedData?.rateLabel.isNullOrEmpty()) {
                RatesPerMinCard(
                    uiState.callAcceptedData?.rateLabel ?: "",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        } else {

            if (uiState?.userType == UserType.USER.value) {
                AudioCallUserScreenHeader(
                    isWalletLoading = uiState.isWalletLoading ?: false,
                    coinsRemaining = uiState.walletData?.balance,
                    onRefreshWallet = {
                        videoCallViewModel.getWallet()
                    }
                )
            } else {
                AudioCallHostScreenHeader(
                    currentEarn = uiState?.currentEarnResponse,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            AnimatedContent(
                showCallingLayout,
                modifier = Modifier.align(Alignment.Center)
            )
            { showCallingLayout ->
                if (showCallingLayout && uiState?.isCallAccepted == false) {
                    callingWaitingLayout(uiState)
                } else {
                    if (uiState?.callType == CallType.Audio.type) {
                        AudioCallRunningLayout(uiState)
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
            )
            {
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .weight(1f, false)
                        .widthIn(max = 150.dp)
                        .height(200.dp)
                )
                {
                    uiState?.localSurfaceView?.let {
                        AndroidView(
                            factory = {
                                Log.d(
                                    "Fkbnfkbnfk",
                                    "FACTORY BLOCK : Local Android View Holder updated"
                                )
                                uiState.localSurfaceView
                            },
                            update = {
                                Log.d(
                                    "Fkbnfkbnfk",
                                    "UPDATE BLOCK : Local Android View Holder updated"
                                )
                                uiState.localSurfaceView
                            },
                            modifier = Modifier
                                .padding(horizontal = 5.dp)

                                .clip(Shape_10)
                                .background(
                                    color = Color.White.copy(alpha = 0.4f),
                                    shape = Shape_10
                                )
                        )
                    }
                }
            }



            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            )
            {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
                {


                    //INCOMING CALL UI to decline and accept call....
                   /* AnimatedVisibility((uiState?.isFromIncomingCall == true) && (!uiState.isIncomingCallAccepted && !uiState.isIncomingCallAcceptingInProgress), modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                        incomingCallControllers(
                            callType = uiState?.callType,
                            onEnd = onDeclineIncomingCall,
                            onAccept = onAcceptIncomginCall
                        )
                    }*/
                    AnimatedVisibility(
                        (uiState?.isFromIncomingCall == false) || uiState?.isIncomingCallAccepted == true,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        {

                            if(uiState?.callType == CallType.Video.type) {
                                ControlIcon(
                                    modifier = Modifier.weight(1f),
                                    icon = R.drawable.video_call_iconswitch_camera,
                                    text = "Switch",
                                    onClick = {
                                        videoCallViewModel.onEvent(
                                            VideoCallScreenEvents.IsCameraFront(
                                                uiState.isCameraFront ?: true
                                            )
                                        )

                                    })
                            }

                            ControlIcon(
                                modifier = Modifier.weight(1f),
                                icon = if (uiState?.isMic == true) R.drawable.video_call_iconmic_off else
                                    R.drawable.video_call_iconmic_on,
                                text = if (uiState?.isMic ?: false) "Mute" else "Unmute",
                                onClick = {
                                    videoCallViewModel.onEvent(
                                        VideoCallScreenEvents.MicOnOff(
                                            uiState?.isMic ?: true
                                        )
                                    )
                                })

                            ControlIcon(
                                modifier = Modifier.weight(1f),
                                icon = if (uiState?.isSpeaker == true) R.drawable.video_call_iconspeaker_off else R.drawable.video_call_iconspeaker_on,
                                text = "Speaker",
                                onClick = {
                                    videoCallViewModel.onEvent(
                                        VideoCallScreenEvents.SpeakerOnOff(
                                            uiState?.isSpeaker ?: true
                                        )
                                    )
                                })

                            ControlIcon(
                                modifier = Modifier.weight(1f),
                                icon = R.drawable.video_call_icongift,
                                text = "Gift",
                                count = uiState?.giftReceivedUnreadCount,
                                onClick = onGift
                            )


                            ControlIcon(
                                modifier = Modifier.weight(1f),
                                R.drawable.video_call_iconend_phone,
                                color = red_4,
                                text = "End Call",
                                onClick = {
                                    onEnd()
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatesPerMinCard(
    rateFormatted: String,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .padding(top = 12.dp, horizontal = 20.dp)
            .clip(CircleShape)
            .background(shape = CircleShape, color = theme_color_22)
            .border(width = 1.dp, color = theme_color_1, CircleShape)
            .padding(horizontal = 15.dp, vertical = 4.dp)
    ) {

        Image(
            painterResource(R.drawable.multi_coinsimage46),
            modifier = Modifier.size(15.dp),
            contentDescription = null
        )
        Text(
            rateFormatted,
            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
            fontSize = 13.sp,
            color = Color.Black
        )
    }
}

@Composable
fun callingWaitingLayout(uiState: VideoCallScreenUIState?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        GradientText(
            if (uiState?.isFromIncomingCall == true && !uiState.isIncomingCallAccepted) {
                "Incoming call"
            } else if (uiState?.isFromIncomingCall == true && uiState.isIncomingCallAccepted) {
                ""
            } else {
                "Calling"
            },
            fontFamily = HAFFER_TRIAL_MEDIUM,
            fontSize = 18.sp
        )
        AsyncProfileImageWithLoader(
            image = uiState?.image,
            modifier = Modifier
                .size(140.dp)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
        Text(
            uiState?.personName ?: "",
            fontFamily = HAFFER_TRIAL_MEDIUM,
            fontSize = 20.sp
        )
        if (uiState?.userType == UserType.USER.value) {
            AnimatedVisibility(!uiState.callAcceptedData?.rateLabel.isNullOrEmpty()) {
                RatesPerMinCard(uiState.callAcceptedData?.rateLabel ?: "", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun AudioCallRunningLayout(uiState: VideoCallScreenUIState?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        AsyncProfileImageWithLoader(
            image = uiState?.image,
            modifier = Modifier
                .size(140.dp)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        )
        Text(
            uiState?.personName ?: "",
            fontFamily = HAFFER_TRIAL_MEDIUM,
            fontSize = 20.sp
        )
        Text(
            text = uiState?.timerValue ?: "",
            style = TextStyle(
                letterSpacing = 1.1.sp, color = Color.Black,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(top = 12.dp)
        )

        if (uiState?.userType == UserType.USER.value) {
            AnimatedVisibility(!uiState.callAcceptedData?.rateLabel.isNullOrEmpty()) {
                RatesPerMinCard(uiState.callAcceptedData?.rateLabel ?: "", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun incomingCallControllers(
    callType: String?,
    onEnd: () -> Unit,
    onAccept: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
                .aspectRatio(1f)
                .clickableDebAnim {
                    onEnd()
                }
                .background(color = red, shape = CircleShape)
                .padding(5.dp)) {
            Image(
                painterResource(R.drawable.svghangupcall_iconsdownwardreceivericon),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
                .aspectRatio(1f)
                .clickableDebAnim {
                    onAccept()
                }
                .background(color = green_3, shape = CircleShape)
                .padding(5.dp)) {
            val icon = if(callType == CallType.Audio.type) R.drawable.baseline_local_phone_24 else R.drawable.call_iconsaticons_videocamera
            Image(
                painterResource(icon),
                contentDescription = null
            )
        }

    }
}

@Composable
fun ControlIcon(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    count : Int? = null,
    color: Color = Color.Black.copy(0.3f),
    onClick: () -> Unit = {}
) {



    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        BadgedBox(badge = {
                  count?.takeIf { it > 0 }?.let {
                          Badge(
                              containerColor = Color.Red,
                              contentColor = Color.White
                          ) {
                              Text("$count")
                          }
                  }
                }
        ){
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
                    .dropShadowCustom()
                    .clip(CircleShape)
                    .clickableDebAnim {
                        onClick()
                    }
                    .background(color = color, CircleShape)
                    .padding(7.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(icon),
                    contentDescription = null,
                )
            }
        }

        Text(
            text, color = Color.White, fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }

}


@Composable
private fun VideoCallScreenHeader(
    modifier: Modifier = Modifier,
    partnerName: String? = "Aliya Host Host",
    profilePic: String? = null,
    isOnline: Int? = 1,
    callType: String?,
    isPartnerFav: Int? = 1,
    onBack: () -> Unit = {},
    timerValue : String? = null
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .clip(Shape_20)
                .background(Color.Black.copy(0.4f), Shape_20)
                .padding(horizontal = 14.dp, vertical = 5.dp)
        )
        {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
                    .padding(1.dp)
            ) {
                AsyncImageWithLoader(
                    profilePic,
                    modifier = Modifier.size(25.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier,
            )
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                ) {
                    BasicText(
                        text = partnerName ?: "",
                        style = TextStyle(
                            fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 16.sp,
                            color = Color.White,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis, autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp, maxFontSize = 12.sp
                        )
                    )

                    Image(
                        painter = painterResource(R.drawable.verifies_iconsymbol),
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                ) {
                    if (isOnline == 1) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(7.dp)
                                .background(green_4)
                        )
                    }
                    BasicText(
                        if (isOnline == 1) "Online" else "Offline",
                        maxLines = 1,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 11.sp
                        ),
                        autoSize = TextAutoSize.StepBased(minFontSize = 8.sp, 10.sp),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }


        if(callType == CallType.Video.type && !timerValue.isNullOrEmpty()) {
            Box(
                    modifier = Modifier
                        .weight(1f, false)
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.4f), CircleShape)
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .animateContentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = timerValue ?: "",
                        style = TextStyle(letterSpacing = 1.1.sp, color = Color.White),
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 9.sp, maxFontSize = 14.sp
                        )
                    )
                }
        }
        else{
         Box(modifier = Modifier.weight(1f, false))
        }

        Row(
            modifier = Modifier.weight(1f).alpha(0f),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painterResource(R.drawable.pip_icon),
                contentDescription = null,
                modifier = Modifier.width(30.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painterResource(R.drawable.secutiry_icon),
                contentDescription = null,
                modifier = Modifier.width(30.dp)
            )
        }
    }

}

@Composable
fun AudioCallUserScreenHeader(
    isWalletLoading: Boolean = false,
    coinsRemaining: Int?,
    onRefreshWallet : () -> Unit
) {


    val infiniteAnimation = rememberInfiniteTransition()

    val rotation by infiniteAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        ),
        label = "",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .dropShadowCustom(CircleShape)
            .clip(CircleShape)
            .border(width = 1.dp, color = theme_color_9, CircleShape)
            .background(brush = brush5(), CircleShape)
            .padding(10.dp)
    ) {


        Image(painterResource(R.drawable.multi_coinsimage46), contentDescription = null)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text("Coins Remaining", color = grey_4)
            AnimatedContent(isWalletLoading) { isWalletLoading ->
                if (isWalletLoading) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(50.dp)
                            .height(18.dp)
                    )
                } else {
                    Text(
                        (coinsRemaining ?: 0).toString(),
                        color = Color.Black,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                        fontSize = 16.sp
                    )

                }
            }
        }

        Image(
            painterResource(R.drawable.outline_refresh_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(theme_color_12),
            modifier = Modifier.clickableDebAnim{
                onRefreshWallet()
            }.graphicsLayer{
                rotationZ = if(isWalletLoading) rotation else rotation.absoluteValue
            }
        )


    }
}

@Composable
fun AudioCallHostScreenHeader(
    currentEarn: CurrentEarnResponse?,
) {

    val earnedBeans = remember {
        Animatable(0f)
    }
    LaunchedEffect(currentEarn?.totalCallBeans?.toFloat() ?: 0f) {
        earnedBeans.animateTo(currentEarn?.totalCallBeans?.toFloat() ?: 0f, tween(1500, easing = FastOutSlowInEasing))
    }


    currentEarn?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .dropShadowCustom(CircleShape)
                .clip(CircleShape)
                .border(width = 1.dp, color = theme_color_9, CircleShape)
                .background(brush = brush5(), CircleShape)
                .padding(10.dp)
        ) {


            Image(painterResource(R.drawable.multi_coinsimage46), contentDescription = null)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text("Current Earned", color = grey_4)
                Text(
                    (earnedBeans.value.toInt() ?: 0).toString(),
                    color = Color.Black,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 16.sp
                )
            }
        }
    }
}