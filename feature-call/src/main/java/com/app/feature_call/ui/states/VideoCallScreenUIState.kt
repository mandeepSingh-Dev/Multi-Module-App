package com.app.feature_call.ui.states

import android.view.TextureView
import com.app.core_common.base.UIEvent
import com.app.core_common.base.UIState
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.app.feature_wallet.remote.model.response.WalletResponse
import com.uae.core_socket.model.CallAcceptedResponse
import com.uae.core_socket.model.CurrentEarnResponse

data class VideoCallScreenUIState(
    val isLoading: Boolean = false,
    val isReviewsPointsLoading: Boolean = false,
    val isReviewBookingLoading: Boolean = false,
    val opinionsList: List<String?>? = emptyList(),
    val bookingId: String? = null,
    val localSurfaceView: TextureView? = null,
    val remoteSurfaceView: TextureView? = null,
    val showCallingLayout: Boolean? = true,
    val personName: String? = null,
    val callType: String? = null,
    val image: String? = null,
    val isSpeaker: Boolean = false,
    val isCamera: Boolean = false,
    val isMic: Boolean = true,
    val isCameraFront: Boolean = true,
    val isFromIncomingCall: Boolean = false,
    val isCallAccepted: Boolean = false,
    val callAcceptedData: CallAcceptedResponse? = null,
    val isIncomingCallAcceptingInProgress: Boolean = false,
    val isIncomingCallAccepted: Boolean = false,
    val isWalletLoading: Boolean = false,
    val walletData: WalletResponse.WalletData? = null,
    val userType: Int? = null,
    val isGiftsListLoading: Boolean = false,
    val isGiftSending: Boolean = false,
    val giftsList: List<GiftListResponse.GiftData?>? = emptyList(),
    val giftReceivedUnreadCount: Int? = null,
    val currentEarnResponse: CurrentEarnResponse? = null,
    val timerValue: String? = null,

    ) : UIState


sealed interface VideoCallScreenEvents : UIEvent {
    data class GenerateToken(val chatID: String?, val partnerId: String?) : VideoCallScreenEvents


    data class JoinChannel(val rtcToken: String?, val chatID: String?, val userId: String?) : VideoCallScreenEvents
    object OnChannelJoined : VideoCallScreenEvents

    object OnAcceptingIncomingVideoCall : VideoCallScreenEvents
    object RejectIncomingCall : VideoCallScreenEvents
    object CallRejectedFromCallee : VideoCallScreenEvents
    data class CallAcceptedFromCallee(val callAcceptedResponse: CallAcceptedResponse?) : VideoCallScreenEvents
    object CallCancelledFromCaller : VideoCallScreenEvents
    object CancelCall : VideoCallScreenEvents

    object StartCall : VideoCallScreenEvents
    object FinishCall : VideoCallScreenEvents
    data class ShowDefinedCustomMessage(val message: String?) : VideoCallScreenEvents

    data class SpeakerOnOff(val isSpeaker: Boolean) : VideoCallScreenEvents

    data class CameraOnOff(val isCameraOff: Boolean) : VideoCallScreenEvents

    data class MicOnOff(val isMic: Boolean) : VideoCallScreenEvents
    data class IsCameraFront(val isCamFront: Boolean) : VideoCallScreenEvents

    data class EndCallSuccess(val endCallDetails: Any?) : VideoCallScreenEvents

    object OnNoShow : VideoCallScreenEvents

    object ShowFeedbackScreen : VideoCallScreenEvents
    data class ReviewBookingError(val error: String?) : VideoCallScreenEvents

}