package com.app.feature_story.ui.post

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storydemo.Overlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReelUploadViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReelUploadUiState())
    val uiState: StateFlow<ReelUploadUiState> = _uiState.asStateFlow()

    // ---- Media selection ----

    fun onMediaSelected(uri: Uri, type: ReelMediaType) {
        _uiState.update {
            it.copy(
                media = SelectedReelMedia(uri, type),
                overlays = emptyList(),
                selectedOverlayId = null,
                uploadError = null
            )
        }
    }

    /** Discard current media and go back to the picker. */
    fun clearMedia() {
        _uiState.update {
            it.copy(media = null, overlays = emptyList(), selectedOverlayId = null)
        }
    }

    // ---- Overlays ----

    fun addEmojiOverlay(emoji: String) {
        val overlay = Overlay(
            type = "emoji",
            x = 0.5, y = 0.5,
            scale = 1.0, rotation = 0.0,
            value = emoji
        )
        _uiState.update { it.copy(overlays = it.overlays + overlay, selectedOverlayId = overlay.id) }
    }

    fun addTextOverlay(
        text: String,
        color: String = "#FFFFFF",
        backgroundColor: String? = "#66000000"
    ) {
        if (text.isBlank()) return
        val overlay = Overlay(
            type = "text",
            x = 0.5, y = 0.5,
            scale = 1.0, rotation = 0.0,
            value = text,
            color = color,
            fontSize = 22.0,
            backgroundColor = backgroundColor
        )
        _uiState.update { it.copy(overlays = it.overlays + overlay, selectedOverlayId = overlay.id) }
    }

    fun selectOverlay(id: String?) {
        _uiState.update { it.copy(selectedOverlayId = id) }
    }

    /** Applies a drag/pinch/rotate delta from the editor's gesture handler. */
    fun updateOverlayTransform(id: String, dxFraction: Float, dyFraction: Float, scaleFactor: Float, rotationDeltaDeg: Float) {
        _uiState.update { state ->
            state.copy(
                overlays = state.overlays.map { ov ->
                    if (ov.id != id) return@map ov
                    ov.copy(
                        x = (ov.x + dxFraction).coerceIn(0.0, 1.0),
                        y = (ov.y + dyFraction).coerceIn(0.0, 1.0),
                        scale = (ov.scale * scaleFactor).coerceIn(0.3, 5.0),
                        rotation = (ov.rotation + rotationDeltaDeg).mod(360.0)
                    )
                }
            )
        }
    }

    fun deleteSelectedOverlay() {
        _uiState.update { state ->
            val idToRemove = state.selectedOverlayId ?: return@update state
            state.copy(
                overlays = state.overlays.filterNot { it.id == idToRemove },
                selectedOverlayId = null
            )
        }
    }

    // ---- Caption ----

    fun updateCaption(text: String) {
        _uiState.update { it.copy(caption = text) }
    }

    // ---- Post ----

    /**
     * Dummy upload. Replace the body with your real multipart request:
     *  - media file from state.media.uri (as IMAGE or VIDEO)
     *  - state.overlays, serialized straight to JSON matching the API
     *    schema (Overlay already carries @SerializedName + excludes `id`)
     *  - state.caption
     */
    fun postReel(onDone: () -> Unit = {}) {
        val state = _uiState.value
        val media = state.media ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, uploadError = null) }
            try {
                // TODO: real network call, e.g.
                // repository.uploadReel(mediaUri = media.uri, mediaType = media.type,
                //     overlays = state.overlays, caption = state.caption)
                delay(1800) // simulated network round-trip
                _uiState.update { it.copy(isUploading = false, uploadSuccess = true) }
                onDone()
            } catch (e: Exception) {
                _uiState.update { it.copy(isUploading = false, uploadError = e.message ?: "Upload failed") }
            }
        }
    }

    fun consumeUploadSuccess() {
        _uiState.update { it.copy(uploadSuccess = false) }
    }
}
