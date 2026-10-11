package com.app.feature_withdraw.ui.states

import com.app.core_common.base.UIState
import com.app.feature_withdraw.ui.voice_recording.RecState

data class IdentityVerificationUIState(
    val isMobileVerificationLoading : Boolean = false,
    val isOTPVerificationLoading : Boolean = false,
    val isFaceVerificationStartInLoading : Boolean = false,
    val isFaceVerificationCompleteInLoading : Boolean = false,
    val isVoiceVerificationStartInLoading : Boolean = false,
    val isVoiceVerificationCompleteInLoading : Boolean = false,
    val recordingState : RecState? = null,
    val otpError: String? = null,
    val phone: String? = null,

    ) : UIState