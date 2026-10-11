package com.app.core_agora.agora

import android.graphics.Rect
import android.util.Log
import io.agora.rtc2.ClientRoleOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.LocalTranscoderConfiguration
import io.agora.rtc2.UserInfo

class RtcEventHandler(
    val mOnUserJoined : (Int) -> Unit,
    val mOnUserOffline : (Int, reason : Int) -> Unit,
    val mOnJoinChannelSuccesss : (String, Int, Int) -> Unit,
    val onErrorr : (Int) -> Unit = {},
    val onLeaveChannell : (RtcStats?) -> Unit = {},
    val onConnectionLostt : () -> Unit = {},
    val onRejoinChannelSuccesss : (toString: String, uid: Int, elapsed: Int) -> Unit = {_,_,_ ->},
    val  onActiveSpeakerr : (uid: Int) -> Unit = {},
    val  onUserMuteAudioo : (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onUserMuteVideoo :  (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onUserEnableVideoo :  (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onUserEnableLocalVideoo :  (uid: Int, muted: Boolean) -> Unit = {_,_ ->},
    val  onVideoSizeChangedd : (source: Constants.VideoSourceType?, uid: Int, width: Int, height: Int, rotation: Int) -> Unit = {_,_,_,_,_ ->},
    val onStreamInjectedStatuss : (String?,Int?,Int?) -> Unit = {e,r,t->},
    val onStreamMessagee : (Int?,Int?,ByteArray?) -> Unit= {e,r,b->},
    val onStreamMessageErrorr : (Int?,Int?,Int?,Int?,Int?) -> Unit= {a,b,c,d,e ->},

) : IRtcEngineEventHandler() {
    override fun onError(err: Int) {
        super.onError(err)
            Log.d("vdkvnjkdnvd",err.toString() + " Error")
        onErrorr(err)
    }

    override fun onCameraReady() {
        super.onCameraReady()
    }

    override fun onCameraFocusAreaChanged(rect: Rect?) {
        super.onCameraFocusAreaChanged(rect)
    }

    override fun onCameraExposureAreaChanged(rect: Rect?) {
        super.onCameraExposureAreaChanged(rect)
    }

    override fun onContentInspectResult(result: Int) {
        super.onContentInspectResult(result)
    }

    override fun onSnapshotTaken(
        uid: Int,
        filePath: String?,
        width: Int,
        height: Int,
        errCode: Int,
    ) {
        super.onSnapshotTaken(uid, filePath, width, height, errCode)
    }

    override fun onFacePositionChanged(
        imageWidth: Int,
        imageHeight: Int,
        faceRectArr: Array<out AgoraFacePositionInfo>?,
    ) {
        super.onFacePositionChanged(imageWidth, imageHeight, faceRectArr)
    }

    override fun onVideoStopped() {
        super.onVideoStopped()
    }

    override fun onLeaveChannel(stats: RtcStats?) {
        super.onLeaveChannel(stats)
        onLeaveChannell(stats)
    }


    override fun onAudioVolumeIndication(speakers: Array<out AudioVolumeInfo>?, totalVolume: Int) {
        super.onAudioVolumeIndication(speakers, totalVolume)
    }

    override fun onLastmileQuality(quality: Int) {
        super.onLastmileQuality(quality)
    }

    override fun onLastmileProbeResult(result: LastmileProbeResult?) {
        super.onLastmileProbeResult(result)
    }

    @Deprecated("Deprecated in Java")
    override fun onLocalVideoStat(sentBitrate: Int, sentFrameRate: Int) {
        super.onLocalVideoStat(sentBitrate, sentFrameRate)
    }

    override fun onFirstLocalVideoFrame(
        source: Constants.VideoSourceType?,
        width: Int,
        height: Int,
        elapsed: Int,
    ) {
        super.onFirstLocalVideoFrame(source, width, height, elapsed)
    }

    override fun onConnectionLost() {
        super.onConnectionLost()

        onConnectionLostt()
    }

    override fun onConnectionInterrupted() {
        super.onConnectionInterrupted()
    }

    override fun onConnectionStateChanged(state: Int, reason: Int) {
        super.onConnectionStateChanged(state, reason)
    }

    override fun onNetworkTypeChanged(type: Int) {
        super.onNetworkTypeChanged(type)
    }

    override fun onConnectionBanned() {
        super.onConnectionBanned()
    }

    override fun onMediaEngineLoadSuccess() {
        super.onMediaEngineLoadSuccess()
    }

    override fun onMediaEngineStartCallSuccess() {
        super.onMediaEngineStartCallSuccess()
    }

    override fun onAudioMixingFinished() {
        super.onAudioMixingFinished()
    }

    override fun onRequestToken() {
        super.onRequestToken()
    }

    override fun onLicenseValidationFailure(error: Int) {
        super.onLicenseValidationFailure(error)
    }

    override fun onAudioRouteChanged(routing: Int) {
        super.onAudioRouteChanged(routing)
    }

    override fun onAudioMixingStateChanged(state: Int, reasonCode: Int) {
        super.onAudioMixingStateChanged(state, reasonCode)
    }

    override fun onAudioMixingPositionChanged(position: Long) {
        super.onAudioMixingPositionChanged(position)
    }

    override fun onFirstLocalAudioFramePublished(elapsed: Int) {
        super.onFirstLocalAudioFramePublished(elapsed)
    }

    override fun onFirstRemoteAudioFrame(uid: Int, elapsed: Int) {
        super.onFirstRemoteAudioFrame(uid, elapsed)
    }

    override fun onFirstRemoteAudioDecoded(uid: Int, elapsed: Int) {
        super.onFirstRemoteAudioDecoded(uid, elapsed)
    }

    override fun onAudioEffectFinished(soundId: Int) {
        super.onAudioEffectFinished(soundId)
    }

    override fun onClientRoleChanged(
        oldRole: Int,
        newRole: Int,
        newRoleOptions: ClientRoleOptions?,
    ) {
        super.onClientRoleChanged(oldRole, newRole, newRoleOptions)
    }

    override fun onClientRoleChangeFailed(reason: Int, currentRole: Int) {
        super.onClientRoleChangeFailed(reason, currentRole)
    }

    override fun onRtmpStreamingStateChanged(url: String?, state: Int, errCode: Int) {
        super.onRtmpStreamingStateChanged(url, state, errCode)
    }

    override fun onRtmpStreamingEvent(url: String?, event: Int) {
        super.onRtmpStreamingEvent(url, event)
    }

    override fun onTranscodingUpdated() {
        super.onTranscodingUpdated()
    }

    override fun onTokenPrivilegeWillExpire(token: String?) {
        super.onTokenPrivilegeWillExpire(token)
    }

//    override fun onLocalPublishFallbackToAudioOnly(isFallbackOrRecover: Boolean) {
//        super.onLocalPublishFallbackToAudioOnly(isFallbackOrRecover)
//    }

    override fun onChannelMediaRelayStateChanged(state: Int, code: Int) {
        super.onChannelMediaRelayStateChanged(state, code)
    }

    override fun onIntraRequestReceived() {
        super.onIntraRequestReceived()
    }

    override fun onUplinkNetworkInfoUpdated(info: UplinkNetworkInfo?) {
        super.onUplinkNetworkInfoUpdated(info)
    }



    override fun onEncryptionError(errorType: Int) {
        super.onEncryptionError(errorType)
    }

    override fun onPermissionError(permission: Int) {
        super.onPermissionError(permission)

        onPermissionError(permission)
    }

    override fun onLocalUserRegistered(uid: Int, userAccount: String?) {
        super.onLocalUserRegistered(uid, userAccount)
    }

    override fun onUserInfoUpdated(uid: Int, userInfo: UserInfo?) {
        super.onUserInfoUpdated(uid, userInfo)
    }

    override fun onUserStateChanged(uid: Int, state: Int) {
        super.onUserStateChanged(uid, state)
    }

    override fun onFirstLocalVideoFramePublished(source: Constants.VideoSourceType?, elapsed: Int) {
        super.onFirstLocalVideoFramePublished(source, elapsed)
    }

    override fun onAudioSubscribeStateChanged(
        channel: String?,
        uid: Int,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onAudioSubscribeStateChanged(channel, uid, oldState, newState, elapseSinceLastState)
    }

    override fun onVideoSubscribeStateChanged(
        channel: String?,
        uid: Int,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onVideoSubscribeStateChanged(channel, uid, oldState, newState, elapseSinceLastState)
    }

    override fun onAudioPublishStateChanged(
        channel: String?,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onAudioPublishStateChanged(channel, oldState, newState, elapseSinceLastState)
    }

    override fun onVideoPublishStateChanged(
        source: Constants.VideoSourceType?,
        channel: String?,
        oldState: Int,
        newState: Int,
        elapseSinceLastState: Int,
    ) {
        super.onVideoPublishStateChanged(source, channel, oldState, newState, elapseSinceLastState)
    }

    override fun onUploadLogResult(requestId: String?, success: Boolean, reason: Int) {
        super.onUploadLogResult(requestId, success, reason)
    }

//    override fun onWlAccMessage(reason: Int, action: Int, wlAccMsg: String?) {
//        super.onWlAccMessage(reason, action, wlAccMsg)
//    }
//
//    override fun onWlAccStats(currentStats: WlAccStats?, averageStats: WlAccStats?) {
//        super.onWlAccStats(currentStats, averageStats)
//    }

    override fun onVideoRenderingTracingResult(
        uid: Int,
        currentEvent: Constants.MEDIA_TRACE_EVENT?,
        tracingInfo: VideoRenderingTracingInfo?,
    ) {
        super.onVideoRenderingTracingResult(uid, currentEvent, tracingInfo)
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        super.onJoinChannelSuccess(channel, uid, elapsed)
        mOnJoinChannelSuccesss(channel.toString(), uid, elapsed)
    }

    override fun onRejoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        super.onRejoinChannelSuccess(channel, uid, elapsed)
        onRejoinChannelSuccesss(channel.toString(), uid, elapsed)
    }


    override fun onUserJoined(uid: Int, elapsed: Int) {
        super.onUserJoined(uid, elapsed)
        Log.d("flbkfnbkf", "User Joined")
        mOnUserJoined(uid)
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        super.onUserOffline(uid, reason)
        mOnUserOffline(uid, reason)
    }

    override fun onActiveSpeaker(uid: Int) {
        super.onActiveSpeaker(uid)
        onActiveSpeakerr( uid)
    }



    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
    }

    override fun onFirstRemoteVideoFrame(uid: Int, width: Int, height: Int, elapsed: Int) {
        super.onFirstRemoteVideoFrame(uid, width, height, elapsed)
    }

    override fun onUserMuteAudio(uid: Int, muted: Boolean) {
        super.onUserMuteAudio(uid, muted)
        onUserMuteAudioo( uid,muted)
    }

    override fun onUserMuteVideo(uid: Int, muted: Boolean) {
        super.onUserMuteVideo(uid, muted)
        onUserMuteVideoo( uid,muted)
    }

    override fun onUserEnableVideo(uid: Int, enabled: Boolean) {
        super.onUserEnableVideo(uid, enabled)
        onUserEnableVideoo( uid,enabled)
    }

    override fun onUserEnableLocalVideo(uid: Int, enabled: Boolean) {
        super.onUserEnableLocalVideo(uid, enabled)
        onUserEnableLocalVideoo( uid,enabled)
    }

    override fun onVideoSizeChanged(
        source: Constants.VideoSourceType?,
        uid: Int,
        width: Int,
        height: Int,
        rotation: Int,
    ) {
        super.onVideoSizeChanged(source, uid, width, height, rotation)
        onVideoSizeChangedd( source, uid, width, height, rotation)
    }

    private fun onVideoSizeChangedd(source: Constants.VideoSourceType?, uid: Int, width: Int, height: Int, rotation: Int) {
    }

    override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        super.onRemoteAudioStateChanged(uid, state, reason, elapsed)
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        super.onRemoteVideoStateChanged(uid, state, reason, elapsed)
        Log.d("fkbnfkbnfj", " onRemoteVideoStateChanged ")

    }

    override fun onRemoteSubscribeFallbackToAudioOnly(uid: Int, isFallbackOrRecover: Boolean) {
        super.onRemoteSubscribeFallbackToAudioOnly(uid, isFallbackOrRecover)
    }

    override fun onAudioQuality(uid: Int, quality: Int, delay: Short, lost: Short) {
        super.onAudioQuality(uid, quality, delay, lost)
    }



    override fun onRemoteVideoStat(
        uid: Int,
        delay: Int,
        receivedBitrate: Int,
        receivedFrameRate: Int,
    ) {
        super.onRemoteVideoStat(uid, delay, receivedBitrate, receivedFrameRate)
    }

    override fun onRemoteAudioTransportStats(uid: Int, delay: Int, lost: Int, rxKBitRate: Int) {
        super.onRemoteAudioTransportStats(uid, delay, lost, rxKBitRate)
    }

    override fun onRemoteVideoTransportStats(uid: Int, delay: Int, lost: Int, rxKBitRate: Int) {
        super.onRemoteVideoTransportStats(uid, delay, lost, rxKBitRate)
    }

    override fun onRhythmPlayerStateChanged(state: Int, errorCode: Int) {
        super.onRhythmPlayerStateChanged(state, errorCode)
    }

    override fun onLocalAudioStateChanged(state: Int, error: Int) {
        super.onLocalAudioStateChanged(state, error)
    }

    override fun onLocalVideoStateChanged(
        source: Constants.VideoSourceType?,
        state: Int,
        error: Int,
    ) {
        super.onLocalVideoStateChanged(source, state, error)
        Log.d(
            "LOCAL_VIDEO",
            "state=$state error=$error"
        )
    }

    override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray?) {
        super.onStreamMessage(uid, streamId, data)
        onStreamMessagee(uid,streamId,data)
    }

    override fun onStreamMessageError(
        uid: Int,
        streamId: Int,
        error: Int,
        missed: Int,
        cached: Int,
    ) {
        super.onStreamMessageError(uid, streamId, error, missed, cached)
        onStreamMessageErrorr(uid,streamId,error,missed,cached)
    }

    override fun onProxyConnected(
        channel: String?,
        uid: Int,
        proxyType: Int,
        localProxyIp: String?,
        elapsed: Int,
    ) {
        super.onProxyConnected(channel, uid, proxyType, localProxyIp, elapsed)
    }

    override fun onLocalVideoTranscoderError(
        stream: LocalTranscoderConfiguration.TranscodingVideoStream?,
        error: Int,
    ) {
        super.onLocalVideoTranscoderError(stream, error)
    }

}