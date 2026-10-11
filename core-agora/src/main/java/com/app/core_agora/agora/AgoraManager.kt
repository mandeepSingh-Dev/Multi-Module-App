package com.app.core_agora.agora

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.TextureView
import android.view.View
import androidx.core.content.ContextCompat
import com.app.core_agora.BuildConfig
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.enums.CallType
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class
AgoraManager(
    val context : Context,
    val onGetLocalSurfaceView : (TextureView) -> Unit,
    val onGetRemoteSurfaceView : (TextureView) -> Unit,
    val onUserJoined : () -> Unit,
    val onJoinChannel : (String, Int) -> Unit,
    val onUserMuteVideo : (Int, Boolean) -> Unit,
    val onUserMuteAudio : (Int, Boolean) -> Unit,
    val onLeave : (shouldFinishActivity : Boolean) -> Unit,
) {
    private val REQUESTED_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    var isRemoteUserJoined = false

    private var rtcEngine : RtcEngine? = null

     var isLocalAudioMute = false
     var isLocalSpeakerDisable= false
     var isLocalVideoOff = false

    private var rtcEventHandler = RtcEventHandler(
        mOnUserJoined = {
            CoroutineScope(Dispatchers.Main).launch {
                isRemoteUserJoined = true
                Log.d("kfnbknbjfn", "userJoined")
                setUpRemoteVideo(it)
                onUserJoined()
            }
        },
        mOnJoinChannelSuccesss = { channel, uid, t ->
            Log.d("fbkbjfkvbvnfj", channel.toString())
            onJoinChannel(channel, uid)
        },
        mOnUserOffline = { uid, reason ->
//            leaveChannel(shouldFinishActivity = false)
        },
        onUserMuteVideoo = { uid, muted ->
            onUserMuteVideo(uid, muted)
        },
        onUserEnableVideoo = { uid, muted ->
        },
        onUserMuteAudioo = { uid, muted ->
            onUserMuteAudio(uid, muted)
        }, onErrorr = {
            Log.d("vdkvnjkdnvd", it.toString() + " Error")
        })

     fun setUpRtcEngine(callType: String) {
        try {
            rtcEngine = RtcEngine.create(context, BuildConfig.AGORA_APP_ID, rtcEventHandler)
            rtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)

            rtcEngine?.enableAudio()
            if(callType == CallType.Video.type) {
                rtcEngine?.enableVideo()
                rtcEngine?.enableLocalVideo(true)
            }

            rtcEngine?.setEnableSpeakerphone(true)

//            rtcEngine?.muteLocalVideoStream(true)
//            rtcEngine?.stopPreview()
//            localSurfaceView?.visibility = View.GONE
        }catch (e: Exception){
            Log.d("kbbkjgbfkmnkfv",e.message.toString())
        }
    }


    var localSurfaceView : TextureView ?= null
    private fun setUpLocalVideo(){
        localSurfaceView = TextureView(context)
        localSurfaceView?.visibility = View.VISIBLE
        val videoCanvas = VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0)
        rtcEngine?.setupLocalVideo(videoCanvas)
        localSurfaceView?.elevation = 200f
        localSurfaceView?.let {
            onGetLocalSurfaceView(it)
        }
    }
    var remoteSurfaceView : TextureView? = null
     fun setUpRemoteVideo(remoteUid : Int){
         Log.d("fkbnfkbnfj", remoteUid.toString() + " SetupRemoteVideo -> $remoteUid")
        remoteSurfaceView = TextureView(context)
        val videoCanvas = VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, remoteUid)
        rtcEngine?.setupRemoteVideo(videoCanvas)
         remoteSurfaceView?.visibility = View.GONE
         remoteSurfaceView?.let {
             onGetRemoteSurfaceView(it)
         }
    }

    fun joinChannel(token : String?, channelName : String?, uid : String?){
        val channelOptions = ChannelMediaOptions()

        channelOptions.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
        Log.d("flvmkdnvkd", token.toString())
        Log.d("flvmkdnvkd", channelName.toString())
        Log.d("flvmkdnvkd", uid.toString())
        val isSuccess = rtcEngine?.joinChannelWithUserAccount(token, channelName, uid, channelOptions)

        Log.d("flfvnfknvf", isSuccess.toString())
    }
    fun leaveChannel(shouldFinishActivity : Boolean = true){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                rtcEngine?.leaveChannel()
                isRemoteUserJoined = false
                destroyRTCEngine()
            } catch (e: Exception) {
                Log.d("fbknfkbnf", e.message.toString())
            }finally {
                onLeave(shouldFinishActivity)
            }
        }
    }


    fun shouldMuteAudio(shouldMute : Boolean = true){
        Log.d("fkbnfknbf", shouldMute.toString())
        rtcEngine?.muteLocalAudioStream(shouldMute)
        isLocalAudioMute = shouldMute
    }
    fun shouldDisableSpeakerPhone(shouldDisable : Boolean = true){

        Log.d("kfnbkfnb", shouldDisable.toString())
        rtcEngine?.setEnableSpeakerphone(shouldDisable)
        isLocalSpeakerDisable = shouldDisable
    }
    fun shouldCameraOff(shouldCameraOff : Boolean = true){

        Log.d("fbmnkfbnfb", shouldCameraOff.toString())
        if(!shouldCameraOff){
            if(localSurfaceView == null) {
                setUpLocalVideo()
            }
            rtcEngine?.startPreview()
            rtcEngine?.muteLocalVideoStream(false)
            localSurfaceView?.visibility = View.VISIBLE
        }else{
            rtcEngine?.muteLocalVideoStream(true)
            rtcEngine?.stopPreview()
            localSurfaceView?.visibility = View.GONE
        }

            isLocalVideoOff = shouldCameraOff

    }

    fun switchCamera( ){
        rtcEngine?.switchCamera()
    }

fun destroyRTCEngine(){
    RtcEngine.destroy()
    rtcEngine = null
}

    @SuppressLint("ObsoleteSdkInt")
    private fun checkSelfPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            REQUESTED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED }
        } else {
            true
        }
    }



}