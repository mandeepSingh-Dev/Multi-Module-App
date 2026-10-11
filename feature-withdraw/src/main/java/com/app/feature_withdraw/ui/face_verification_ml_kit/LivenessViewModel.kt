package com.app.feature_withdraw.ui.face_verification_ml_kit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Step { FACE, BLINK, TURN, DONE }

data class LivenessUiState(
    val step: Step = Step.FACE,
    val message: String = "Keep your face in the frame",
    val faceOk: Boolean = false,
    val blinkOk: Boolean = false,
    val turnOk: Boolean = false
)

class LivenessViewModel : ViewModel() {

    private val _state = MutableStateFlow(LivenessUiState())
    val state: StateFlow<LivenessUiState> = _state.asStateFlow()

    private var trackingId: Int? = null
    private var stableFrames = 0
    private var eyesOpenSeen = false
    private var eyesClosedSeen = false
    private var turnedPositive = false
    private var turnedNegative = false

    fun onSignal(s: FaceSignal) {
        if (_state.value.step == Step.DONE) return

        // Anti-swap: a different face or multiple faces resets the whole check
        if (s.faceCount > 1) {
            reset("Only one face allowed")
            return
        }
        if (s.faceCount == 0 || !s.inFrame) {
            stableFrames = 0
            _state.value = _state.value.copy(message = "Keep your face in the frame")
            return
        }
        if (trackingId != null && s.trackingId != null && trackingId != s.trackingId) {
            reset("Face changed, starting over")
            return
        }
        trackingId = s.trackingId

        when (_state.value.step) {
            Step.FACE -> {
                stableFrames++
                if (stableFrames >= 10) {
                    _state.value = _state.value.copy(
                        step = Step.BLINK, faceOk = true, message = "Blink your eyes naturally"
                    )
                }
            }

            Step.BLINK -> {
                val l = s.leftEyeOpen ?: return
                val r = s.rightEyeOpen ?: return
                val open = l > 0.7f && r > 0.7f
                val closed = l < 0.3f && r < 0.3f
                if (open && !eyesClosedSeen) eyesOpenSeen = true
                if (closed && eyesOpenSeen) eyesClosedSeen = true
                if (open && eyesClosedSeen) {
                    _state.value = _state.value.copy(
                        step = Step.TURN, blinkOk = true, message = "Turn your head left and right"
                    )
                }
            }

            Step.TURN -> {
                // Front-camera yaw sign can be mirrored; we simply require both directions.
                if (s.yaw > 20f) turnedPositive = true
                if (s.yaw < -20f) turnedNegative = true
                if (turnedPositive && turnedNegative) {
                    _state.value = _state.value.copy(
                        step = Step.DONE, turnOk = true, message = "Verified"
                    )
                }
            }

            Step.DONE -> Unit
        }
    }

    fun reset(message: String = "Keep your face in the frame") {
        trackingId = null
        stableFrames = 0
        eyesOpenSeen = false
        eyesClosedSeen = false
        turnedPositive = false
        turnedNegative = false
        _state.value = LivenessUiState(message = message)
    }
}
