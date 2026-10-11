package com.app.feature_call.ui

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.app.core_agora.agora.VideoCallService
import com.app.core_agora.agora.VideoServiceCallback
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.Constants
import com.app.core_common.utils.SocketHostEventsHandler
import com.app.core_common.utils.enums.CallType
import com.app.core_common.utils.enums.UserType
import com.app.core_ui.components.padding
import com.app.feature_call.ui.states.VideoCallScreenEvents
import com.app.feature_call.ui.viewmodels.VideoCallViewModel
import com.app.feature_gift.ui.GiftNotificationCard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

val permissions = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.CAMERA,
    Manifest.permission.POST_NOTIFICATIONS,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@AndroidEntryPoint
class VideoActivity : ComponentActivity(), VideoServiceCallback {

    companion object {
        const val TAG = "VideoActivity_Call_Logs"
    }

    private var videoCallService: VideoCallService? = null
    private var isServiceBound = false

    private val videoCallViewModel: VideoCallViewModel by viewModels()

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            videoCallService = (binder as VideoCallService.MyBinder).getInstance()
            videoCallService?.setCallback(this@VideoActivity)
            isServiceBound = true
            videoCallService?.initializeAgora(videoCallViewModel.callType ?: CallType.Audio.type)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        videoCallViewModel.chatID = intent.getStringExtra(Constants.IntentKeys.CHAT_ID)
        videoCallViewModel.partnerID = intent.getStringExtra(Constants.IntentKeys.PARTNER_ID)
        videoCallViewModel.callType = intent.getStringExtra(Constants.IntentKeys.CALL_TYPE)
        videoCallViewModel.isFromIncomingCall =
            intent.getBooleanExtra(Constants.IntentKeys.IS_FROM_INCOMING_CALL, false)
        videoCallViewModel.callID = intent.getStringExtra(Constants.IntentKeys.CALL_ID)

        val name = intent.getStringExtra(Constants.IntentKeys.PERSON_NAME)
        val image = intent.getStringExtra(Constants.IntentKeys.PERSON_IMAGE)

        videoCallViewModel.updateState { state ->
            state.copy(
                isFromIncomingCall = videoCallViewModel.isFromIncomingCall,
                personName = name, image = image,
                callType = videoCallViewModel.callType
            )
        }

        lifecycleScope.launch {
            videoCallViewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is VideoCallScreenEvents.StartCall -> {
                        Log.d("vkndkvnd", "Start Call")
                        agoraCallingOperation()
                    }

                    is VideoCallScreenEvents.OnAcceptingIncomingVideoCall -> {
                        Log.d("vkndkvnd", "On Accepting Video Call")
                        videoCallViewModel.updateState { state ->
                            state.copy(isIncomingCallAcceptingInProgress = true)
                        }
                        showToast(
                            message = "Accepting the call, please wait...",
                            length = Toast.LENGTH_SHORT
                        )
                        agoraCallingOperation()
                    }

                    is VideoCallScreenEvents.GenerateToken -> {
                        Log.d("vkndkvnd", "Generate Token")
                        videoCallViewModel.generateToken(
                            chatID = videoCallViewModel.chatID,
                            partnerID = videoCallViewModel.partnerID
                        )
                    }

                    is VideoCallScreenEvents.JoinChannel -> {
                        videoCallService?.joinChannel(
                            token = uiEvent.rtcToken,
                            channel = uiEvent.chatID,
                            uid = uiEvent.userId
                        )
                    }

                    VideoCallScreenEvents.OnChannelJoined -> {
                        Log.d(TAG, "Channel Joined Event")

                        if (!videoCallViewModel.isFromIncomingCall) {
                            videoCallViewModel.sendCallUserSocketEvent(
                                partnerID = videoCallViewModel.partnerID,
                                chatID = videoCallViewModel.chatID,
                                callType = videoCallViewModel.callType,
                            )
                        } else {
                            videoCallViewModel.sendAcceptCallSocketEvent(
                                callID = videoCallViewModel.callID
                            )
                        }
                    }

                    is VideoCallScreenEvents.SpeakerOnOff -> {
                        videoCallService?.shouldSpeakerOff(!uiEvent.isSpeaker)
                        videoCallViewModel.updateState { state ->
                            state.copy(isSpeaker = !uiEvent.isSpeaker)
                        }
                    }

                    is VideoCallScreenEvents.CameraOnOff -> {
                        Log.d("lvmkfmbf", uiEvent.isCameraOff.toString())
                        videoCallService?.shouldCameraOff(uiEvent.isCameraOff)
                        videoCallViewModel.updateState { state ->
                            state.copy(isCamera = !uiEvent.isCameraOff)
                        }
                    }

                    is VideoCallScreenEvents.MicOnOff -> {
                        videoCallService?.shouldMuteAudio(!uiEvent.isMic)
                        videoCallViewModel.updateState { state ->
                            state.copy(isMic = !uiEvent.isMic)
                        }
                    }

                    is VideoCallScreenEvents.IsCameraFront -> {
                        videoCallService?.shouldCameraOff(false)
                        videoCallService?.switchCamera()
                        videoCallViewModel.updateState { state ->
                            state.copy(isCameraFront = !uiEvent.isCamFront, isCamera = true)
                        }
                    }

                    is VideoCallScreenEvents.FinishCall -> {
                        Log.d("fbknfkbnf", "finash call")
                        val isUserJoined = videoCallService?.isRemoteUserJoined() ?: false

                        leaveChannel()
                    }


                    is VideoCallScreenEvents.RejectIncomingCall -> {
                        Log.d("fbknfkbnf", "Decline Incoming call")
                        val isUserJoined = videoCallService?.isRemoteUserJoined() ?: false
                        videoCallViewModel.sendRejectCallSocketEvent()
                        leaveChannel()
                    }

                    is VideoCallScreenEvents.CallRejectedFromCallee -> {
                        showToast(message = "$name has declined your call")
                        leaveChannel()
                    }

                    is VideoCallScreenEvents.CallAcceptedFromCallee -> {

                        showToast(message = "$name has accepted your call")
                    }

                    is VideoCallScreenEvents.CallCancelledFromCaller -> {
                        showToast(message = "$name has cancelled this call")
                        leaveChannel()
                    }

                    is VideoCallScreenEvents.ReviewBookingError -> {
                        showToast(message = uiEvent.error)
                    }

                    is CommonUIEvents.ShowError -> {
                        showToast(message = uiEvent.error)
                        finish()
                    }
                }
            }
        }


        setContent {
            Scaffold(
                containerColor = Color.White
            ) { paddingValues ->
                paddingValues

//                if (chatID != null) {

                CallingScreen(
                    videoCallViewModel = videoCallViewModel,
                    getAreUsersJoined = {
                        videoCallService?.isRemoteUserJoined() ?: false
                    },
                    onFinish = {
                        leaveChannel()
                    }
                )

            }
        }
    }

    private fun stopVideoService() {
        val intent = Intent(this, VideoCallService::class.java)
        stopService(intent)
        if (isServiceBound) {
            try {
                unbindService(serviceConnection)
            } catch (e: IllegalArgumentException) {
            } finally {
                isServiceBound = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRtcAndStopService()
    }

    private fun destroyRtcAndStopService() {
        try {
            videoCallService?.destroyRtcEngine()
            unbindService(serviceConnection)
            stopVideoService()
        } catch (e: Exception) {
        }
    }

    override fun onGetLocalSurfaceView(surfaceView: TextureView) {
        runOnUiThread {
            Log.d("flvmfvmf", "localSurface view")
            videoCallViewModel.updateState { state ->
                state.copy(localSurfaceView = surfaceView)
            }
        }
    }

    override fun onGetRemoteSurfaceView(surfaceView: TextureView) {
        runOnUiThread {
            Log.d("fkbnkfbnf", "onGetRemoteSurfaceView")
            videoCallViewModel.updateState { state ->
                state.copy(remoteSurfaceView = surfaceView, showCallingLayout = true,
                    isCallAccepted = true)
            }
            surfaceView.post {
                surfaceView.visibility =
                    View.GONE // By default make RemoteSurface view HIDE first time then in onUserVideoMute automatically toggle on/off the remote video.
            }
        }
    }

    override fun onChannelJoined(channel: String?, uid: Int?) {
        Log.d(TAG, "Channel Joined")
        videoCallViewModel.onEvent(VideoCallScreenEvents.OnChannelJoined)

        /*  bookingId = intent.getStringExtra(Constants.IntentKeys.BOOKING_ID)
        videoCallViewModel.joinCallDetails(id = bookingId)*/
    }

    override fun onUserJoined() {
        /*  bookingId = intent.getStringExtra(Constants.IntentKeys.BOOKING_ID)
        videoCallViewModel.joinedCallApi(id = bookingId)
        videoCallViewModel.updateState { state -> state?.copy(bookingId = bookingId) }
  */
    }

    override fun onUserVideoMute(uid: Int, muted: Boolean?) {
        runOnUiThread {
            videoCallViewModel.updateState { state ->
                val remoteSurfaceView = state?.remoteSurfaceView
                remoteSurfaceView?.visibility = if (muted == true) View.GONE else View.VISIBLE

                state.copy(remoteSurfaceView = remoteSurfaceView, showCallingLayout = muted)
            }
        }
    }

    override fun onUserAudioMute(uid: Int, muted: Boolean?) {

    }

    override fun onLeaveChannel(shouldFinishActivity: Boolean) {
        stopAndClearEverything(shouldFinishActivity = shouldFinishActivity)
    }

    override fun onShowCustomMessage(message: String?) {

        videoCallViewModel.onEvent(VideoCallScreenEvents.ShowDefinedCustomMessage(message = message))
    }


    fun leaveChannel() {
        if (videoCallService != null) {
            videoCallService?.leaveChannel(true)
        } else {
            stopAndClearEverything(true)
        }
    }

    var isCurrentlyOnFeedbackScreen = false
    private fun stopAndClearEverything(shouldFinishActivity: Boolean) {
        runOnUiThread {

            Log.d("fbkmnfkbf", "Leave now")
            //TODO remove destroyRtcEngine line if problem arises
            videoCallService?.destroyRtcEngine()
            
            stopVideoService()

            videoCallViewModel.sendEndCallSocketEvent()
            videoCallViewModel.cancelTimer()


            if (shouldFinishActivity) {
                if(videoCallViewModel.uiState.value?.userType == UserType.HOST.value){
                    finish()
                }else {
                    if (videoCallService?.isCallWasJoinedOnce == true) {
                        if (!videoCallViewModel.isFeedbackScreenShowed) {
                            videoCallViewModel.isFeedbackScreenShowed = true
                            videoCallViewModel.onEvent(VideoCallScreenEvents.ShowFeedbackScreen)
                        } else {
                            if (!videoCallViewModel.isCurrentlyOnFeedbackScreen) {
                                finish()
                            }
                        }
                    } else {
                        finish()
                    }
                }

            } else {
//                videoCallViewModel.sendEndCallSocketEvent()
            }
        }
    }

    var notificationManager: NotificationManager? = null
    private fun agoraCallingOperation() {
        Log.d("ksnccjsdjknsjdns", "1")
        notificationManager = getSystemService<NotificationManager>()
        notificationManager?.cancel(Constants.NotificationKeys.CALL_INITIATED_NOTIFICATION_ID)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        videoCallViewModel.chatID = intent.getStringExtra(Constants.IntentKeys.CHAT_ID)
        videoCallViewModel.partnerID = intent.getStringExtra(Constants.IntentKeys.PARTNER_ID)
        videoCallViewModel.callType = intent.getStringExtra(Constants.IntentKeys.CALL_TYPE)
//        bookingId = intent.getStringExtra(Constants.IntentKeys.BOOKING_ID)
        val name = intent.getStringExtra(Constants.IntentKeys.PERSON_NAME)
        val image = intent.getStringExtra(Constants.IntentKeys.PERSON_IMAGE)


        videoCallViewModel.updateState { state ->
            state.copy(
                personName = name, image = image,
                callType = videoCallViewModel.callType
            )
        }

//        binding.personNameTv.text = audiologistName

//        if (chatID != null) {
        try {
            val intent = Intent(this, VideoCallService::class.java)
            intent.putExtra(Constants.IntentKeys.PERSON_NAME, name)

            startService(intent)
//                ContextCompat.startForegroundService(this, intent)
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)

            lifecycleScope.launch {
//                    if (!chatID.isNullOrEmpty()) {
                videoCallViewModel.onEvent(
                    VideoCallScreenEvents.GenerateToken(
                        chatID = videoCallViewModel.chatID,
                        partnerId = videoCallViewModel.partnerID
                    )
                )
//                    }
            }


        } catch (e: Exception) {
        }
//        } else {
//            showToast("Something went wrong")
//            stopAndClearEverything(shouldFinishActivity = true)
//        }
    }


}

