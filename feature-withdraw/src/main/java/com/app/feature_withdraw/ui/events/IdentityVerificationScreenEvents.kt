package com.app.feature_withdraw.ui.events

import com.app.core_common.base.UIEvent
import java.io.File

interface IdentityVerificationScreenEvents  : UIEvent{

    object MobileVerificationStarted : IdentityVerificationScreenEvents
    object OTPVerifiedSuccessfully : IdentityVerificationScreenEvents
    data class OnGetVerifiedImageFromMLKit(val verifiedImageFile : File?) : IdentityVerificationScreenEvents
    data class ShowFaceVerificationDoneDialog(val imageUrl : String?) : IdentityVerificationScreenEvents
    object OnFaceVerificationAllDone : IdentityVerificationScreenEvents


    data class OnGetVoiceRecording(val recordedVoiceFile : File?) : IdentityVerificationScreenEvents
    data class ShowVoiceVerificationDoneDialog(val voiceUrl : String?) : IdentityVerificationScreenEvents
    object OnVoiceVerificationAllDone : IdentityVerificationScreenEvents

}