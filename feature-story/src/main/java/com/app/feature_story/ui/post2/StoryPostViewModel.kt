package com.app.feature_story.ui.post2

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.enums.StoryMediaType
import com.app.core_story.domain.usecases.StoryAllUseCases
import com.app.core_story.remote.model.StoryRequestBody
import com.example.storydemo.Overlay
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.internal.http.hasBody
import kotlin.coroutines.resume

@HiltViewModel
class StoryPostViewModel @Inject constructor(
    private val commonAllUseCases: CommonAllUseCases,
    private val storyAllUseCases: StoryAllUseCases) : BaseViewModel<StoryPostUiState>(StoryPostUiState()) {


    // ------------------------------------------------------------------
    // Camera controls
    // ------------------------------------------------------------------

    fun setCaptureMode(mode: CaptureMode) = updateState { state -> state.copy(captureMode = mode) }

    fun toggleCamera() =  updateState { state -> state.copy(isFrontCamera = !state.isFrontCamera) }

    // ------------------------------------------------------------------
    // Media selection
    // ------------------------------------------------------------------

    /** Captured photo/video: exactly ONE item, replacing everything else. */
    fun onCaptured(uri: Uri, type: StoryMediaType) {
        updateState { state -> state.copy(
                items = listOf(
                    StoryMediaItem(
                        uri = uri,
                        type = type,
                        source = MediaSource.CAPTURED
                    )
                ),
                selectedIndex = 0,
                selectedOverlayId = null,
                error = null
            )
        }
    }

    /**
     * Gallery picks: MANY items allowed and they accumulate. A previously
     * captured item is dropped (captured and gallery are separate flows).
     */
    fun onGalleryPicked(picked: List<Pair<Uri, StoryMediaType>>) {
        if (picked.isEmpty()) return
        updateState { state ->
            val existing = state.items.filter { it.source == MediaSource.GALLERY }
            val knownUris = existing.map { it.uri }.toSet()
            val added = picked
                .filter { (uri, _) -> uri !in knownUris }
                .map { (uri, type) ->
                    StoryMediaItem(
                        uri = uri,
                        type = type,
                        source = MediaSource.GALLERY
                    )
                }
            val all = existing + added
            state.copy(
                items = all,
                selectedIndex = if (added.isNotEmpty()) existing.size
                else state.selectedIndex.coerceIn(0, (all.lastIndex).coerceAtLeast(0)),
                selectedOverlayId = null
            )
        }
    }

    fun selectItem(index: Int) {
        updateState { state ->
            if (index in state.items.indices) state.copy(
                selectedIndex = index,
                selectedOverlayId = null
            ) else state
        }
    }

    fun removeItem(index: Int) {
        updateState { state -> state
            if (index !in state.items.indices) return@updateState state
            val list = state.items.toMutableList().also { it.removeAt(index) }
            val newSelected =
                (if (index < state.selectedIndex) state.selectedIndex - 1 else state.selectedIndex)
                    .coerceIn(0, list.lastIndex.coerceAtLeast(0))
            state.copy(items = list, selectedIndex = newSelected, selectedOverlayId = null)
        }
    }

    /** Discard everything and go back to the live camera. */
    fun clearAll() {
        updateState { state ->
            state.copy(
                items = emptyList(),
                selectedIndex = 0,
                selectedOverlayId = null
            )
        }
    }

    // ------------------------------------------------------------------
    // Overlays (always applied to the currently selected item)
    // ------------------------------------------------------------------

    fun addEmojiOverlay(emoji: String) {
        addOverlay(
            Overlay(
                type = "emoji",
                x = 0.5,
                y = 0.5,
                scale = 1.0,
                rotation = 0.0,
                value = emoji
            )
        )
    }

    /**
     * "Text" tool  -> big, centered, no background.
     * "Caption" tool -> smaller, near the bottom, on a translucent bar.
     * Both are sent as type "text". If your backend has a separate
     * "caption" type, change it right here.
     */
    fun addTextOverlay(text: String, isCaption: Boolean) {
        if (text.isBlank()) return
        addOverlay(
            Overlay(
                type = "text",
                x = 0.5,
                y = if (isCaption) 0.85 else 0.5,
                scale = 1.0,
                rotation = 0.0,
                value = text.trim(),
                color = "#FFFFFF",
                fontSize = if (isCaption) 16.0 else 28.0,
                backgroundColor = if (isCaption) "#99000000" else null
            )
        )
    }

    private fun addOverlay(overlay: Overlay) {
        updateState { state ->
            val index = state.selectedIndex
            val item = state.items.getOrNull(index) ?: return@updateState state
            state.copy(
                items = state.items.toMutableList()
                    .also { it[index] = item.copy(overlays = item.overlays + overlay) },
                selectedOverlayId = overlay.id
            )
        }
    }

    fun selectOverlay(id: String?) =  updateState { state -> state.copy(selectedOverlayId = id) }

    /** Called by the drag / pinch / rotate gesture. Deltas are already normalized. */
    fun updateOverlayTransform(
        id: String,
        dxFraction: Float,
        dyFraction: Float,
        scaleFactor: Float,
        rotationDeltaDeg: Float
    ) {
        updateSelectedItemOverlays { overlays ->
            overlays.map { ov ->
                if (ov.id != id) ov
                else ov.copy(
                    x = (ov.x + dxFraction).coerceIn(0.0, 1.0),
                    y = (ov.y + dyFraction).coerceIn(0.0, 1.0),
                    scale = (ov.scale * scaleFactor).coerceIn(0.3, 5.0),
                    rotation = (ov.rotation + rotationDeltaDeg).mod(360.0)
                )
            }
        }
    }

    fun deleteSelectedOverlay() {
        val id = uiState.value?.selectedOverlayId ?: return
        updateSelectedItemOverlays { overlays -> overlays.filterNot { it.id == id } }
         updateState { state -> state.copy(selectedOverlayId = null) }
    }

    private fun updateSelectedItemOverlays(transform: (List<Overlay>) -> List<Overlay>) {
        updateState { state  ->
            val index = state.selectedIndex
            val item = state.items.getOrNull(index) ?: return@updateState state
            state.copy(
                items = state.items.toMutableList()
                    .also { it[index] = item.copy(overlays = transform(item.overlays)) }
            )
        }
    }

    // ------------------------------------------------------------------
    // Post: upload every file -> get URL -> build body -> call post API
    // ------------------------------------------------------------------

    fun postStory() {
        val state = uiState.value
        if ((state?.items?.isEmpty() ?: false) || (state?.isPosting ?: false)) return


        state?.items?.forEach {
            Log.d("fbfknbkfb", it.overlays.toString())
        }

        viewModelScope.launch {
             updateState { state -> state.copy(isPosting = true, error = null) }
            try {
                // Uploads run in parallel; awaitAll() keeps the ORIGINAL order.
                val body = coroutineScope {
                    state?.items?.map { item ->
                        async {
                            PostStoryRequestItem(
                                overlays = item.overlays,
                                mediaType = item.type.apiValue,
                                url = uploadMedia(item.uri.toString()) ?: ""
                            )
                        }
                    }?.awaitAll()
                }

                //TODO USE OWN API

                body ?: return@launch
                Log.d("fjbnjfnbf", "story posting started")
                postStoryApi(body)
                Log.d("fjbnjfnbf", "story posting stopped")
//                repository.postStories(body)
                 updateState { state -> state.copy(
                        isPosting = false,
                        posted = true,
                        items = emptyList(),
                        selectedIndex = 0
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                 updateState { state -> state.copy(
                        isPosting = false,
                        error = e.message ?: "Couldn't post your story"
                    )
                }
            }
        }
    }

    suspend fun uploadMedia(uri: String?): String? = coroutineScope{
       return@coroutineScope suspendCancellableCoroutine<String?> { continuation ->
            commonAllUseCases.imageUploadUseCase(uri, folderName = "Story").onEach {
                it.getCallbacks(
                    onLoading = {},
                    onSuccess = {
                        continuation.resume(it.data?.data?.file)
                    },
                    onError = {
                        continuation.resume(null)
                        errorEvent(it.error)
                    }
                )
            }.launchIn(this)
        }
    }




    suspend fun postStoryApi(body: List<PostStoryRequestItem>): Boolean = coroutineScope {

        val requestBody = body.map {
            StoryRequestBody.StoryRequestBodyItem(
                mediaType = it.mediaType,
                overlays = it.overlays.map { overlay ->
                    StoryRequestBody.StoryRequestOverlay(
                    id = overlay.id,
                    type = overlay.type,
                    x = overlay.x,
                    y = overlay.y,
                    scale = overlay.scale,
                    rotation = overlay.rotation,
                    value = overlay.value,
                    color = overlay.color,
                    fontSize = overlay.fontSize,
                    backgroundColor = overlay.backgroundColor
                ) },
                url = it.url
            )
        }

        Log.d("fkbnjfbnf", body.toString())

        Log.d("fkbnkfnbf", requestBody.toString())

       return@coroutineScope suspendCancellableCoroutine<Boolean> { continuation ->
           storyAllUseCases.postStoriesUseCase(requestBody).onEach {
                it.getCallbacks(
                    onLoading = {

                    },
                    onSuccess = {
                        continuation.resume(true)
                    },
                    onError = {
                        continuation.resume(false)
                        errorEvent(it.error)
                    }
                )
            }.launchIn(this)
        }
    }





    fun consumePosted() =  updateState { state -> state.copy(posted = false) }
    fun consumeError() =  updateState { state -> state.copy(error = null) }
}
