package com.app.core_agora.agora

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.TextureView
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.app.core_common.R
import com.app.core_common.UserManager
import com.app.core_common.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

//@AndroidEntryPoint
class VideoCallService : Service() {

    private val myBinder: MyBinder = MyBinder()
    private var videoServiceCallback: VideoServiceCallback? = null
    private var notificationManager: NotificationManager? = null

    companion object {
        const val VIDEO_CALL_NOTIFICATION_ID = 100
    }

    private var agoraManager: AgoraManager? = null

    val timerLog = "TIMER_LOG"

    var isMessageShowedOnStartOf5Min = false
    var isMessageShowedOnEndOf5Min = false

    var isMessageShowedOn15Min = false

    @Inject
    lateinit var userManager: UserManager

    var min5FunctionalityJob: Job? = null
    var min15FunctionalityJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotification()
    }


    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    fun setCallback(videoServiceCallback: VideoServiceCallback) {
        this.videoServiceCallback = videoServiceCallback
    }

    inner class MyBinder() : Binder() {
        fun getInstance() = this@VideoCallService
    }

    var isCallWasJoinedOnce = false
    fun initializeAgora(callType: String) {

        agoraManager = AgoraManager(
            this,
            onGetLocalSurfaceView = { localSurfaceView ->
                videoServiceCallback?.onGetLocalSurfaceView(localSurfaceView)
            },
            onGetRemoteSurfaceView = { localSurfaceView ->
                Log.d("fkbnfkbnfj", "remoteSurfaceView -> from onGetRemoteSurfaceView")
                isCallWasJoinedOnce = true
                videoServiceCallback?.onGetRemoteSurfaceView(localSurfaceView)
            },
            onUserJoined = {
                videoServiceCallback?.onUserJoined()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34
                    startForeground(
                        Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_ID,
                        buildNotification(isCallOngoing = true),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL or
                                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA or
                                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // API 29-33
                    startForeground(
                        Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_ID,
                        buildNotification(isCallOngoing = true),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
                    )
                } else {
                    startForeground(
                        Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_ID,
                        buildNotification(isCallOngoing = true)
                    )
                }

            },
            onJoinChannel = { channel, uid ->
                CoroutineScope(Dispatchers.Main).launch {
                    videoServiceCallback?.onChannelJoined(channel, uid)
                }
            },
            onUserMuteVideo = { uid, muted ->
                videoServiceCallback?.onUserVideoMute(uid, muted)
            },
            onUserMuteAudio = { uid, muted ->
                videoServiceCallback?.onUserAudioMute(uid, muted)
            },
            onLeave = { shouldFinishActivity ->
                CoroutineScope(Dispatchers.Main).launch {
                    notificationManager?.notify(Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_ID, buildNotification("Call has been ended"))
                    videoServiceCallback?.onLeaveChannel(shouldFinishActivity = shouldFinishActivity)
                }
            }
        )
        destroyRtcEngine()
        agoraManager?.setUpRtcEngine(callType)
    }

    var channel: String? = null

    fun joinChannel(token: String?, channel: String?, uid: String?) {
        this.channel = channel


        agoraManager?.joinChannel(token = token, channelName = channel, uid = uid)
    }

    fun leaveChannel(shouldFinishActivity: Boolean = true) {
        agoraManager?.leaveChannel(shouldFinishActivity = shouldFinishActivity)
    }

    fun destroyRtcEngine() {
        agoraManager?.destroyRTCEngine()
    }

    fun shouldMuteAudio(shouldMute: Boolean = true) {

        agoraManager?.shouldMuteAudio(shouldMute)
    }

    fun shouldSpeakerOff(shouldDisable: Boolean = true) {
        agoraManager?.shouldDisableSpeakerPhone(shouldDisable)
    }

    fun shouldCameraOff(shouldCameraOff: Boolean = true) {
        agoraManager?.shouldCameraOff(shouldCameraOff)
    }

    fun isLocalAudioMute() = agoraManager?.isLocalAudioMute
    fun isLocalVideoOff() = agoraManager?.isLocalVideoOff
    fun isRemoteUserJoined() = agoraManager?.isRemoteUserJoined

    //    fun setUpRemoteVideo(uid: Int) = agoraManager?.setUpRemoteVideo(uid)
    fun switchCamera() {
        agoraManager?.switchCamera()
    }

    var personName: String? = null
    var personImage: String? = null
    var bookingId: String? = null
    var subTopicsList: List<String?>? = null
    var topic: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        personName = intent?.getStringExtra(Constants.IntentKeys.PERSON_NAME)
        personImage = intent?.getStringExtra(Constants.IntentKeys.PERSON_IMAGE)




        intent?.action?.let {
            if (it == Constants.IntentKeys.ACTION_LEAVE_CALL) {
                Log.d("fbknbkfnb", "Leave from ACTION_LEAVE_CALL")
                leaveChannel()
            }
        }
        return START_NOT_STICKY
    }


    private fun createNotification() {
        val channel = NotificationChannel(
            Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_ID,
            Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager?.createNotificationChannel(channel)
    }

    private fun buildNotification(
        text: String? = null,
        isCallOngoing: Boolean = false
    ): Notification {

        val onGoingCallStyle = createOnGoingStyle()

        val builder = NotificationCompat.Builder(
            this,
            Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Video Call")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        text?.let {
            builder.setContentText(text)
        }

        if (isCallOngoing) {
//            builder.setFullScreenIntent(createOpenCallActivityIntent(), false)
//            builder.setContentIntent(createOpenCallActivityIntent())
            builder.setOngoing(true)
            builder.setStyle(onGoingCallStyle)
        }


        return builder.build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        agoraManager?.leaveChannel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        notificationManager?.cancel(Constants.NotificationKeys.ONGOING_VIDEO_CALL_NOTIFICATION_ID)
        val intent = Intent(this, VideoCallService::class.java)
        stopService(intent)

        min5FunctionalityJob?.cancel()
        min5FunctionalityJob = null


        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        min5FunctionalityJob?.cancel()
        min5FunctionalityJob = null
    }

    override fun onLowMemory() {
        super.onLowMemory()

    }


    private fun createOnGoingStyle(): NotificationCompat.CallStyle {

        val caller = Person.Builder()
            .setName(personName ?: "")
            .setImportant(false)
            .build()
        return NotificationCompat.CallStyle.forOngoingCall(caller, createHangUpIntent())

    }

    private fun createHangUpIntent(): PendingIntent {
        val serviceIntent = Intent(this, VideoCallService::class.java)
        serviceIntent.action = Constants.IntentKeys.ACTION_LEAVE_CALL
        return PendingIntent.getService(
            this,
            Constants.HANGUP_PENDING_INTENT_CODE,
            serviceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

    }

}
    /*  private fun createOpenCallActivityIntent(): PendingIntent {

        val intent = Intent(this, VideoActivity::class.java)
         intent.putExtra(Constants.IntentKeys.CHANNEL_NAME, channel)
         intent.putExtra(Constants.IntentKeys.PERSON_NAME, personName)

         intent.putExtra(Constants.IntentKeys.TOPIC, topic)
         intent.putStringArrayListExtra(Constants.IntentKeys.SUBTOPICS_LIST,subTopicsList as ArrayList<String?>?)

         intent.putExtra(Constants.IntentKeys.BOOKING_ID, bookingId)
         intent.putExtra(Constants.IntentKeys.PERSON_IMAGE, personImage)
         intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
         return PendingIntent.getActivity(
             this,
             Constants.OPEN_VIDEO_CALL_ACTIVITY_PENDING_INTENT_CODE,
             intent,
             PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
         )
     }

 }*/

interface VideoServiceCallback {

    fun onGetLocalSurfaceView(surfaceView: TextureView)
    fun onGetRemoteSurfaceView(surfaceView: TextureView)
    fun onChannelJoined(channel: String?, uid: Int?)
    fun onUserJoined()
    fun onUserVideoMute(uid: Int, muted: Boolean?)
    fun onUserAudioMute(uid: Int, muted: Boolean?)
    fun onLeaveChannel(shouldFinishActivity: Boolean)

    fun onShowCustomMessage(message: String?)
}