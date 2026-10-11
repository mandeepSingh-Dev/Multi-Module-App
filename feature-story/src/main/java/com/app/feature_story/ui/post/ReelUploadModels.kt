package com.app.feature_story.ui.post

import android.net.Uri
import com.example.storydemo.Overlay

enum class ReelMediaType { IMAGE, VIDEO }

data class SelectedReelMedia(
    val uri: Uri,
    val type: ReelMediaType
)

data class ReelUploadUiState(
    val media: SelectedReelMedia? = null,
    val overlays: List<Overlay> = emptyList(),
    val selectedOverlayId: String? = null,
    val caption: String = "",
    val isUploading: Boolean = false,
    val uploadError: String? = null,
    val uploadSuccess: Boolean = false
)
