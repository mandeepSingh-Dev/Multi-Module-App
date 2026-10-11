package com.app.feature_story.ui.post

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.core_ui.components.AsyncImageWithLoader
import com.example.storydemo.Overlay
import java.io.File

/**
 * Full reel-upload flow:
 *  1. No media yet -> choose from gallery (image or video) or record video.
 *  2. Media selected -> full-screen preview with draggable/pinch-scalable/
 *     rotatable emoji & text overlays, a caption field, and Post.
 *
 * Requires (see notes at bottom of this file for manifest / gradle setup):
 *  - androidx.activity:activity-compose (PickVisualMedia, CaptureVideo)
 *  - io.coil-kt:coil-compose (AsyncImage)
 *  - androidx.compose.material:material-icons-extended (optional, or swap icons)
 *  - a FileProvider entry in the manifest for camera video output
 */
@Composable
fun StoryUploadScreen(
    viewModel: ReelUploadViewModel = hiltViewModel(),
    onClose: () -> Unit = {},
    onPosted: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var pendingVideoUri by remember { mutableStateOf<Uri?>(null) }

    // ---- Gallery: pick an existing image OR video. No runtime permission
    // needed — the system Photo Picker grants a temporary read URI. ----
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val mime = context.contentResolver.getType(uri)
            val type = if (mime?.startsWith("video") == true) ReelMediaType.VIDEO else ReelMediaType.IMAGE
            viewModel.onMediaSelected(uri, type)
        }
    }

    // ---- Camera: record a new video into an app-owned file, shared via FileProvider. ----
    val captureVideoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CaptureVideo()
    ) { success ->
        val uri = pendingVideoUri
        if (success && uri != null) {
            viewModel.onMediaSelected(uri, ReelMediaType.VIDEO)
        }
        pendingVideoUri = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants[Manifest.permission.CAMERA] == true) {
            val uri = createReelVideoUri(context)
            pendingVideoUri = uri
            captureVideoLauncher.launch(uri)
        }
        // If denied, you'd normally show a rationale / snackbar here.
    }

    fun launchGalleryPicker() {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
        )
    }

    fun launchCameraRecorder() {
        val hasCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        val hasAudio = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
        if (hasCamera && hasAudio) {
            val uri = createReelVideoUri(context)
            pendingVideoUri = uri
            captureVideoLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
        }
    }

    LaunchedEffect(uiState.uploadSuccess) {
        if (uiState.uploadSuccess) {
            viewModel.consumeUploadSuccess()
            onPosted()
        }
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (uiState.media == null) {
            MediaPickerEntry(
                onPickFromGallery = ::launchGalleryPicker,
                onRecordVideo = ::launchCameraRecorder,
                onClose = onClose
            )
        } else {
            ReelEditorContent(
                uiState = uiState,
                onSelectOverlay = viewModel::selectOverlay,
                onTransformOverlay = viewModel::updateOverlayTransform,
                onDeleteSelected = viewModel::deleteSelectedOverlay,
                onAddEmoji = viewModel::addEmojiOverlay,
                onAddText = { viewModel.addTextOverlay(it) },
                onCaptionChange = viewModel::updateCaption,
                onDiscard = viewModel::clearMedia,
                onPost = { viewModel.postReel(onDone = onPosted) }
            )
        }

        if (uiState.isUploading) {
            UploadingIndicator()
        }

        uiState.uploadError?.let { message ->
            Text(
                text = message,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .background(Color(0xFFD32F2F), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------
// Step 1: pick from gallery or record
// ---------------------------------------------------------------------

@Composable
private fun MediaPickerEntry(
    onPickFromGallery: () -> Unit,
    onRecordVideo: () -> Unit,
    onClose: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("New Reel", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))

            Button(onClick = onPickFromGallery, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.AddCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Choose photo or video")
            }
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onRecordVideo, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Record video")
            }
        }

        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopStart).padding(8.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}

// ---------------------------------------------------------------------
// Step 2: preview + overlay editor + caption + post
// ---------------------------------------------------------------------

@Composable
private fun ReelEditorContent(
    uiState: ReelUploadUiState,
    onSelectOverlay: (String?) -> Unit,
    onTransformOverlay: (id: String, dx: Float, dy: Float, scaleFactor: Float, rotationDelta: Float) -> Unit,
    onDeleteSelected: () -> Unit,
    onAddEmoji: (String) -> Unit,
    onAddText: (String) -> Unit,
    onCaptionChange: (String) -> Unit,
    onDiscard: () -> Unit,
    onPost: () -> Unit
) {
    var showEmojiPicker by remember { mutableStateOf(false) }
    var showTextDialog by remember { mutableStateOf(false) }
    var containerSizePx by remember { mutableStateOf(IntSize.Zero) }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onSizeChanged { containerSizePx = it }
                .pointerInput(Unit) {
                    // Tap on empty canvas area deselects the current overlay.
                    detectTapGestures(onTap = { onSelectOverlay(null) })
                }
        ) {
            when (uiState.media?.type) {
                ReelMediaType.IMAGE -> AsyncImageWithLoader(
                     uiState.media.uri,
                    modifier = Modifier.fillMaxSize(),
                )
                ReelMediaType.VIDEO -> VideoPreview(
                    uri = uiState.media.uri,
                    modifier = Modifier.fillMaxSize()
                )
                null -> Unit
            }

            uiState.overlays.forEach { overlay ->
                EditableOverlayItem(
                    overlay = overlay,
                    isSelected = overlay.id == uiState.selectedOverlayId,
                    containerSize = containerSizePx,
                    onSelect = { onSelectOverlay(overlay.id) },
                    onTransform = { dx, dy, scaleFactor, rotationDelta ->
                        onTransformOverlay(overlay.id, dx, dy, scaleFactor, rotationDelta)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onDiscard) {
                    Icon(Icons.Default.Close, contentDescription = "Discard", tint = Color.White)
                }
                Row {
                    IconButton(onClick = { showTextDialog = true }) {
                        Icon(Icons.Default.AccountBox, contentDescription = "Add text", tint = Color.White)
                    }
                    IconButton(onClick = { showEmojiPicker = true }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Add emoji", tint = Color.White)
                    }
                    if (uiState.selectedOverlayId != null) {
                        IconButton(onClick = onDeleteSelected) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete overlay", tint = Color.White)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111111))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.caption,
                onValueChange = onCaptionChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Write a caption…") },
                maxLines = 3
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = onPost, enabled = !uiState.isUploading) {
                Text("Post")
            }
        }
    }

    if (showEmojiPicker) {
        EmojiPickerSheet(
            onEmojiSelected = { emoji -> onAddEmoji(emoji); showEmojiPicker = false },
            onDismiss = { showEmojiPicker = false }
        )
    }
    if (showTextDialog) {
        TextOverlayDialog(
            onConfirm = { text -> onAddText(text); showTextDialog = false },
            onDismiss = { showTextDialog = false }
        )
    }
}

/** Same visual as the story viewer's overlay, plus drag / pinch-zoom / rotate / select. */
@Composable
private fun BoxScope.EditableOverlayItem(
    overlay: Overlay,
    isSelected: Boolean,
    containerSize: IntSize,
    onSelect: () -> Unit,
    onTransform: (dx: Float, dy: Float, scaleFactor: Float, rotationDelta: Float) -> Unit
) {
    Box(
        modifier = Modifier
            .align(overlayBiasAlignment(overlay.x, overlay.y))
            .graphicsLayer {
                scaleX = overlay.scale.toFloat()
                scaleY = overlay.scale.toFloat()
                rotationZ = overlay.rotation.toFloat()
            }
            .pointerInput(overlay.id, containerSize) {
                if (containerSize.width == 0 || containerSize.height == 0) return@pointerInput
                detectTransformGestures { _, pan, zoom, rotationDelta ->
                    onSelect()
                    onTransform(
                        pan.x / containerSize.width,
                        pan.y / containerSize.height,
                        zoom,
                        rotationDelta
                    )
                }
            }
            .pointerInput(overlay.id) {
                detectTapGestures(onTap = { onSelect() })
            }
            .then(
                if (isSelected) Modifier.border(1.dp, Color.White, RoundedCornerShape(4.dp)) else Modifier
            )
            .padding(4.dp)
    ) {
        OverlayVisual(overlay)
    }
}

/**
 * VideoView doesn't fill its parent by default (it centers at the
 * video's intrinsic size). FullscreenVideoView forces it to take the
 * exact measured size instead, so it behaves like a normal fillMaxSize
 * preview. For production polish (buffering states, better lifecycle
 * handling, adaptive playback) swap this for Media3's ExoPlayer/PlayerView.
 */
@Composable
private fun VideoPreview(uri: Uri, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            FullscreenVideoView(ctx).apply {
                setVideoURI(uri)
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    start()
                }
            }
        },
        update = { view ->
            if (!view.isPlaying) {
                view.setVideoURI(uri)
                view.start()
            }
        }
    )
}

private class FullscreenVideoView(context: Context) : VideoView(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
    }
}

@Composable
private fun UploadingIndicator() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.White)
            Spacer(Modifier.height(12.dp))
            Text("Posting reel…", color = Color.White)
        }
    }
}

/**
 * Creates a content:// Uri (via FileProvider) pointing at a new file in
 * app-specific external storage — no storage permission needed, and the
 * camera app is granted temporary write access to it.
 *
 * Requires in AndroidManifest.xml (see notes below):
 *   <provider android:name="androidx.core.content.FileProvider"
 *             android:authorities="${applicationId}.fileprovider" .../>
 */
private fun createReelVideoUri(context: Context): Uri {
    val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "Reels").apply { mkdirs() }
    val file = File(dir, "REEL_${System.currentTimeMillis()}.mp4")
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}

/*
 * ---------------------------------------------------------------------
 * Setup checklist (one-time, in your app module):
 *
 * 1) Gradle dependencies:
 *      implementation("io.coil-kt:coil-compose:2.6.0")
 *      implementation("androidx.compose.material:material-icons-extended")
 *      implementation("com.google.code.gson:gson:2.10.1") // for @SerializedName on Overlay
 *
 * 2) AndroidManifest.xml:
 *      <uses-permission android:name="android.permission.CAMERA" />
 *      <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *      <!-- gallery picking via the system Photo Picker needs NO permission -->
 *
 *      <application ...>
 *          <provider
 *              android:name="androidx.core.content.FileProvider"
 *              android:authorities="${applicationId}.fileprovider"
 *              android:exported="false"
 *              android:grantUriPermissions="true">
 *              <meta-data
 *                  android:name="android.support.FILE_PROVIDER_PATHS"
 *                  android:resource="@xml/file_paths" />
 *          </provider>
 *      </application>
 *
 * 3) res/xml/file_paths.xml (included alongside this file):
 *      <paths>
 *          <external-files-path name="reel_videos" path="Movies/Reels/" />
 *      </paths>
 *
 * 4) Usage:
 *      ReelUploadScreen(
 *          onClose = { /* navigate back */ },
 *          onPosted = { /* navigate to feed, show success, etc. */ }
 *      )
 * ---------------------------------------------------------------------
 */
