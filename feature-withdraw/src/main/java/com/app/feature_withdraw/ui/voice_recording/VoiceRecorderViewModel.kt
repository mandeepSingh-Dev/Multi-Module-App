package com.app.feature_withdraw.ui.voice_recording

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed interface RecState {
    data object Idle : RecState
    data class Recording(val elapsedMs: Long) : RecState
    data class Done(val file: File) : RecState
    data class Error(val message: String) : RecState
}

class VoiceRecorderViewModel(app: Application) : AndroidViewModel(app) {

    private val recorder = VoiceRecorder(app)
    private var timerJob: Job? = null

    private val _state = MutableStateFlow<RecState>(RecState.Idle)
    val state: StateFlow<RecState> = _state.asStateFlow()

    fun start() {
        if (_state.value is RecState.Recording) return
        try {
            recorder.start(onMaxReached = { finish() })
        } catch (e: Exception) {
            _state.value = RecState.Error("Could not start recording: ${e.message}")
            return
        }

        _state.value = RecState.Recording(0)
        val startTime = SystemClock.elapsedRealtime()
        timerJob = viewModelScope.launch {
            while (true) {
                val elapsed = SystemClock.elapsedRealtime() - startTime
                if (elapsed >= VoiceRecorder.MAX_DURATION_MS) {
                    finish() // auto-stop at 30 seconds
                    break
                }
                _state.value = RecState.Recording(elapsed)
                delay(100)
            }
        }
    }

    /** Stop and keep the recording (auto-stop or the tick button). */
    fun finish() {
        if (_state.value !is RecState.Recording) return
        timerJob?.cancel()
        val file = recorder.stop()
        _state.value = file?.let { RecState.Done(it) } ?: RecState.Error("Recording too short")
    }

    /** Close button: stop and throw the recording away. */
    fun cancel() {
        timerJob?.cancel()
        recorder.cancel()
        _state.value = RecState.Idle
    }

    fun reset() {
        (_state.value as? RecState.Done)?.file?.delete()
        _state.value = RecState.Idle
    }

    override fun onCleared() {
        timerJob?.cancel()
        recorder.cancel()
    }
}
