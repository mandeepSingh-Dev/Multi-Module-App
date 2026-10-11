package com.app.feature_story.ui.post2

import android.net.Uri
import android.util.Log
import com.app.core_common.base.UIState
import com.app.core_common.utils.enums.StoryMediaType
import com.example.storydemo.Overlay
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import java.util.UUID



/** CAPTURED = taken with CameraX (max 1 item). GALLERY = picked (many allowed). */
enum class MediaSource { CAPTURED, GALLERY }

enum class CaptureMode { PHOTO, VIDEO }

/** One story slide. Every item carries its OWN overlays. */
data class StoryMediaItem(
    val id: String = UUID.randomUUID().toString(),
    val uri: Uri,
    val type: StoryMediaType,
    val source: MediaSource,
    val overlays: List<Overlay> = emptyList()
)

data class StoryPostUiState(
    val isLoading : Boolean = false,
    val items: List<StoryMediaItem> = emptyList(),
    val selectedIndex: Int = 0,
    val selectedOverlayId: String? = null,
    val captureMode: CaptureMode = CaptureMode.PHOTO,
    val isFrontCamera: Boolean = false,
    val isPosting: Boolean = false,
    val error: String? = null,
    val posted: Boolean = false
) : UIState{
    val selectedItem: StoryMediaItem? get() = items.getOrNull(selectedIndex)
}

/**
 * One element of the post-API body:
 * [ { "overlays": [...], "mediaType": "image" | "video", "url": "https://..." }, ... ]
 * Overlay.id is @Transient so it is NOT serialized.
 */
data class PostStoryRequestItem(
    @SerializedName("overlays") val overlays: List<Overlay>,
    @SerializedName("mediaType") val mediaType: String,
    @SerializedName("url") val url: String
)

/** Plug your real S3-upload + post-story API calls in here. */
interface StoryRepository {
    /** Uploads the file behind item.uri and returns its public URL. */
    suspend fun uploadMedia(item: StoryMediaItem): String

    /** Calls the post API with the full list. */
    suspend fun postStories(body: List<PostStoryRequestItem>)
}

/** Dummy implementation so the screen works end-to-end. Logs the exact JSON body. */
class FakeStoryRepository : StoryRepository {
    override suspend fun uploadMedia(item: StoryMediaItem): String {
        delay(800) // TODO: open item.uri via contentResolver and upload to S3 / your upload API
        val ext = if (item.type == StoryMediaType.IMAGE) "jpg" else "mp4"
        return "https://your-bucket.s3.amazonaws.com/Stories/${UUID.randomUUID()}.$ext"
    }

    override suspend fun postStories(body: List<PostStoryRequestItem>) {
        delay(500) // TODO: real POST
        Log.d("StoryPost", "POST body = ${Gson().toJson(body)}")
    }
}
