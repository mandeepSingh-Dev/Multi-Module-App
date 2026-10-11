package com.app.feature_story.ui.post2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.video.VideoFrameDecoder
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.StoryMediaType
import com.app.core_ui.R
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.example.storydemo.Overlay
import java.io.File
import kotlin.math.cos
import kotlin.math.sin

private const val MAX_VIDEO_MS = 30_000L
private val Orange = Color(0xFFF5A623)
private val Panel = Color(0xFF1C1C1E)

private enum class TextInputKind { TEXT, CAPTION }

@Composable
fun StoryCameraScreen(
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    viewModel: StoryPostViewModel = viewModel(),
    onPosted: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // ---------------- Permissions (camera is required, mic is optional) ----------------
    fun granted(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    var hasCamera by remember { mutableStateOf(granted(Manifest.permission.CAMERA)) }
    var hasAudio by remember { mutableStateOf(granted(Manifest.permission.RECORD_AUDIO)) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasCamera = granted(Manifest.permission.CAMERA)
        hasAudio = granted(Manifest.permission.RECORD_AUDIO)
    }
    LaunchedEffect(Unit) {
        if (!hasCamera) permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
    }

    ObserveUIEvents(viewModel.uiEvent) { uIEvent ->
        when(uIEvent){
            is CommonUIEvents.ShowError -> {
             snackbarHostState.showSnackBarWithDismiss(uIEvent.error)
            }
            is CommonUIEvents.NavigateTo -> {}
        }
    }

    // ---------------- CameraX controller ----------------
    val controller = remember {
        LifecycleCameraController(context).apply {
            videoCaptureQualitySelector = QualitySelector.from(
                Quality.HD, FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
            )
        }
    }
    LaunchedEffect(uiState?.captureMode) {
        controller.setEnabledUseCases(
            if (uiState?.captureMode == CaptureMode.PHOTO) CameraController.IMAGE_CAPTURE
            else CameraController.VIDEO_CAPTURE
        )
    }
    LaunchedEffect(uiState?.isFrontCamera) {
        runCatching {
            controller.cameraSelector =
                if (uiState?.isFrontCamera ?: false) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    // ---------------- Recording state ----------------
    var activeRecording by remember { mutableStateOf<Recording?>(null) }
    var recordedMs by remember { mutableLongStateOf(0L) }
    val isRecording = activeRecording != null
    DisposableEffect(Unit) { onDispose { activeRecording?.stop() } }

    fun takePhoto() {
        val file = newMediaFile(context, "jpg")
        val metadata = ImageCapture.Metadata().apply { isReversedHorizontal = (uiState?.isFrontCamera ?: false)  } // un-mirror selfies
        val options = ImageCapture.OutputFileOptions.Builder(file).setMetadata(metadata).build()
        controller.takePicture(
            options,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewModel.onCaptured(Uri.fromFile(file), StoryMediaType.IMAGE)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Couldn't capture photo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    @SuppressLint("MissingPermission") // AudioConfig is only enabled when hasAudio is true
    fun startRecording() {
        val file = newMediaFile(context, "mp4")
        val options = FileOutputOptions.Builder(file)
            .setDurationLimitMillis(MAX_VIDEO_MS) // CameraX stops automatically at 30s
            .build()
        val audio = if (hasAudio) AudioConfig.create(true) else AudioConfig.AUDIO_DISABLED

        activeRecording = controller.startRecording(options, audio, ContextCompat.getMainExecutor(context)) { event ->
            when (event) {
                is VideoRecordEvent.Status -> {
                    recordedMs = event.recordingStats.recordedDurationNanos / 1_000_000
                }
                is VideoRecordEvent.Finalize -> {
                    activeRecording = null
                    recordedMs = 0L
                    // Hitting the 30s limit reports an "error" code, but the file is complete and playable.
                    val ok = !event.hasError() ||
                        event.error == VideoRecordEvent.Finalize.ERROR_DURATION_LIMIT_REACHED
                    if (ok) {
                        viewModel.onCaptured(Uri.fromFile(file), StoryMediaType.VIDEO)
                    } else {
                        file.delete()
                        Toast.makeText(context, "Couldn't record video", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> Unit
            }
        }
    }

    // ---------------- Gallery: multiple images + videos ----------------
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        val picked = uris.map { uri ->
            val isVideo = context.contentResolver.getType(uri)?.startsWith("video") == true
            uri to if (isVideo) StoryMediaType.VIDEO else StoryMediaType.IMAGE
        }
        viewModel.onGalleryPicked(picked)
    }

    // ---------------- One-shot events ----------------
    LaunchedEffect(uiState?.error) {
        uiState?.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.consumeError()
        }
    }
    LaunchedEffect(uiState?.posted) {
        if (uiState?.posted ?: false) {
            viewModel.consumePosted()
            onPosted()
        }
    }

    // ---------------- Local UI state ----------------
    var showEmojiPicker by remember { mutableStateOf(false) }
    var textInputKind by remember { mutableStateOf<TextInputKind?>(null) }
    val imageLoader = remember {
        ImageLoader.Builder(context).components { add(VideoFrameDecoder.Factory()) }.build()
    }

    val selectedItem = uiState?.selectedItem
    val hasItems = uiState?.items?.isNotEmpty()
    val toolsEnabled = selectedItem != null && !(uiState?.isPosting ?: false)

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        Column(Modifier.fillMaxSize().systemBarsPadding()) {

            // =============== Preview / editor area ===============
            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (selectedItem == null) {
                    if (hasCamera) {
                        CameraViewfinder(cameraController = controller, modifier = Modifier.fillMaxSize())
                    } else {
                        PermissionPlaceholder {
                            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
                        }
                    }
                } else {
                    key(selectedItem.id) {
                        MediaEditorCanvas(
                            item = selectedItem,
                            selectedOverlayId = uiState?.selectedOverlayId,
                            imageLoader = imageLoader,
                            onSelectOverlay = viewModel::selectOverlay,
                            onTransform = viewModel::updateOverlayTransform
                        )
                    }
                }

                // Close / discard
                IconButtonBare(
                    modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                    enabled = !isRecording && !(uiState?.isPosting ?: false),
                    onClick = { if (hasItems ?: false) viewModel.clearAll() else {
                        backStack.removeLastOrNull()
                    } }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }

                // Recording timer
                if (isRecording) {
                    RecordingTimer(recordedMs, Modifier.align(Alignment.TopCenter).padding(top = 20.dp))
                }

                // Right-hand tools: Text / Sticker / Caption (+ Delete when an overlay is selected)
                Column(
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ToolButton("Text", toolsEnabled, { textInputKind = TextInputKind.TEXT }) {
                        Text("Aa", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    ToolButton("Sticker", toolsEnabled, { showEmojiPicker = true }) {
                        Image(painter = painterResource(R.drawable.emoji), null,)
                    }
                    ToolButton("Caption", toolsEnabled, { textInputKind = TextInputKind.CAPTION }) {
                        Icon(Icons.Outlined.Edit, null, tint = Color.White)
                    }
                    if (uiState?.selectedOverlayId != null) {
                        ToolButton("Delete", true, viewModel::deleteSelectedOverlay) {
                            Icon(Icons.Default.Delete, null, tint = Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // =============== Photo / Video toggle ===============
            ModeToggle(
                mode = uiState?.captureMode ?: CaptureMode.PHOTO,
                enabled = !(hasItems ?: false) && !isRecording,
                onModeChange = viewModel::setCaptureMode,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(14.dp))

            // =============== Gallery | Shutter | Flip camera ===============
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SideButton("Gallery", !isRecording && !(uiState?.isPosting ?: false) , {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                }) {
                    Image(painter = painterResource(R.drawable.gallerysvg), null,)
                }

                ShutterButton(
                    isRecording = isRecording,
                    progress = (recordedMs / MAX_VIDEO_MS.toFloat()).coerceIn(0f, 1f),
                    enabled = hasCamera && !(hasItems ?: false) && !(uiState?.isPosting ?: false),
                    onClick = {
                        when (uiState?.captureMode ?: false) {
                            CaptureMode.PHOTO -> takePhoto()
                            CaptureMode.VIDEO -> if (isRecording) activeRecording?.stop() else startRecording()
                        }
                    }
                )

                SideButton("Camera", !(hasItems ?: false) && !isRecording, viewModel::toggleCamera) {
                    Image(painter = painterResource(R.drawable.camerasvg), null,)
                }
            }

            // =============== Thumbnail strip (selected items) ===============
            if (hasItems ?: false) {
                Spacer(Modifier.height(14.dp))
                ThumbnailStrip(
                    items = uiState?.items ?: emptyList(),
                    selectedIndex = uiState?.selectedIndex ?: 0,
                    imageLoader = imageLoader,
                    onSelect = viewModel::selectItem,
                    onRemove = viewModel::removeItem
                )
            }

            Spacer(Modifier.height(14.dp))

            // =============== Post Story ===============
            PostStoryButton(
                enabled = (hasItems ?: false) && !isRecording && !(uiState?.isPosting ?: false),
                onClick = viewModel::postStory
            )

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Edit, null, tint = Color(0xFF9A9A9A), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "Your story will be automatically deleted after 24 hours.",
                    color = Color(0xFF9A9A9A),
                    fontSize = 11.sp
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        if (uiState?.isPosting ?: false) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(Modifier.height(12.dp))
                    Text("Posting your story…", color = Color.White)
                }
            }
        }
    }

    if (showEmojiPicker) {
        EmojiPickerSheet(
            onEmojiSelected = { viewModel.addEmojiOverlay(it); showEmojiPicker = false },
            onDismiss = { showEmojiPicker = false }
        )
    }
    textInputKind?.let { kind ->
        TextOverlayDialog(
            title = if (kind == TextInputKind.CAPTION) "Add caption" else "Add text",
            onConfirm = { text ->
                viewModel.addTextOverlay(text, isCaption = kind == TextInputKind.CAPTION)
                textInputKind = null
            },
            onDismiss = { textInputKind = null }
        )
    }
}

// =====================================================================
// Camera preview
// =====================================================================

@Composable
private fun CameraViewfinder(cameraController: LifecycleCameraController, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(cameraController, lifecycleOwner) {
        cameraController.bindToLifecycle(lifecycleOwner)
        onDispose { cameraController.unbind() }
    }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                controller = cameraController
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        }
    )
}

@Composable
private fun PermissionPlaceholder(onRequest: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Camera permission is needed to take photos and videos.\nYou can still pick from your gallery.",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onRequest) { Text("Allow camera", color = Orange) }
    }
}

// =====================================================================
// Editor canvas: media + draggable overlays
// =====================================================================

@Composable
private fun MediaEditorCanvas(
    item: StoryMediaItem,
    selectedOverlayId: String?,
    imageLoader: ImageLoader,
    onSelectOverlay: (String?) -> Unit,
    onTransform: (id: String, dx: Float, dy: Float, scale: Float, rotation: Float) -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged { size = it }
            .pointerInput(Unit) { detectTapGestures(onTap = { onSelectOverlay(null) }) }
    ) {
        when (item.type) {
            StoryMediaType.IMAGE -> AsyncImage(
                model = item.uri,
                imageLoader = imageLoader,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            StoryMediaType.VIDEO -> VideoPreview(item.uri, Modifier.fillMaxSize())
        }

        OverlayLayout(overlays = item.overlays) { overlay ->
            EditableOverlayItem(
                overlay = overlay,
                isSelected = overlay.id == selectedOverlayId,
                containerSize = size,
                onSelect = { onSelectOverlay(overlay.id) },
                onTransform = { dx, dy, s, r -> onTransform(overlay.id, dx, dy, s, r) }
            )
        }
    }
}

/**
 * Drag / pinch-zoom / rotate. The gesture sits INSIDE graphicsLayer, so
 * `pan` arrives in the overlay's own scaled + rotated space. We convert it
 * back to screen space (scale, then rotate) before normalizing, so the
 * overlay follows the finger exactly at any scale or rotation.
 */
@Composable
private fun EditableOverlayItem(
    overlay: Overlay,
    isSelected: Boolean,
    containerSize: IntSize,
    onSelect: () -> Unit,
    onTransform: (dx: Float, dy: Float, scaleFactor: Float, rotationDelta: Float) -> Unit
) {
    val latest by rememberUpdatedState(overlay)

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = overlay.scale.toFloat()
                scaleY = overlay.scale.toFloat()
                rotationZ = overlay.rotation.toFloat()
            }
            .pointerInput(overlay.id, containerSize) {
                if (containerSize.width == 0 || containerSize.height == 0) return@pointerInput
                detectTransformGestures { _, pan, zoom, rotationDelta ->
                    val theta = Math.toRadians(latest.rotation)
                    val s = latest.scale.toFloat()
                    val c = cos(theta).toFloat()
                    val sn = sin(theta).toFloat()
                    val screenDx = s * (pan.x * c - pan.y * sn)
                    val screenDy = s * (pan.x * sn + pan.y * c)
                    onSelect()
                    onTransform(
                        screenDx / containerSize.width,
                        screenDy / containerSize.height,
                        zoom,
                        rotationDelta
                    )
                }
            }
            .pointerInput(overlay.id) { detectTapGestures(onTap = { onSelect() }) }
            .then(if (isSelected) Modifier.border(1.dp, Color.White, RoundedCornerShape(4.dp)) else Modifier)
            .padding(4.dp)
    ) {
        OverlayVisual(overlay)
    }
}

/** Media3 player set to "zoom" (center-crop) so video fills the frame without stretching. */
@SuppressLint("UnsafeOptInUsageError")
@Composable
private fun VideoPreview(uri: Uri, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = true
            prepare()
        }
    }
    DisposableEffect(player) { onDispose { player.release() } }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                this.player = player
            }
        },
        update = { it.player = player }
    )
}

// =====================================================================
// Bottom controls
// =====================================================================

@Composable
private fun ModeToggle(
    mode: CaptureMode,
    enabled: Boolean,
    onModeChange: (CaptureMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .alpha(if (enabled) 1f else 0.5f)
            .clip(RoundedCornerShape(50))
            .background(Panel)
            .padding(3.dp)
    ) {
        listOf(CaptureMode.PHOTO to "Photo", CaptureMode.VIDEO to "Video").forEach { (m, label) ->
            val selected = m == mode
            Box(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) Color.White else Color.Transparent)
                    .clickable(enabled = enabled) { onModeChange(m) }
                    .padding(horizontal = 22.dp, vertical = 8.dp)
            ) {
                Text(
                    label,
                    color = if (selected) Color.Black else Color(0xFFBBBBBB),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ShutterButton(
    isRecording: Boolean,
    progress: Float,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val innerSize by animateDpAsState(if (isRecording) 28.dp else 62.dp, label = "shutterInner")
    val innerShape: Shape = if (isRecording) RoundedCornerShape(8.dp) else CircleShape
    val innerColor = if (isRecording) Color.Red else Color.White

    Box(
        modifier = Modifier
            .size(80.dp)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 4.dp.toPx()
            val topLeft = Offset(stroke / 2, stroke / 2)
            val arcSize = Size(size.width - stroke, size.height - stroke)
            drawArc(
                color = if (isRecording) Orange.copy(alpha = 0.3f) else Orange,
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = topLeft, size = arcSize, style = Stroke(stroke)
            )
            if (isRecording) {
                // 30-second progress ring
                drawArc(
                    color = Orange,
                    startAngle = -90f, sweepAngle = 360f * progress, useCenter = false,
                    topLeft = topLeft, size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round)
                )
            }
        }
        Box(Modifier.size(innerSize).clip(innerShape).background(innerColor))
    }
}

@Composable
private fun SideButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Column(
        Modifier.alpha(if (enabled) 1f else 0.4f).width(56.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(44.dp).clip(CircleShape).background(Panel).clickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center
        ) { icon() }
        Spacer(Modifier.height(4.dp))
        Text(label, color = Color(0xFFBBBBBB), fontSize = 11.sp)
    }
}

@Composable
private fun ToolButton(label: String, enabled: Boolean, onClick: () -> Unit, icon: @Composable () -> Unit) {
    Column(
        Modifier.alpha(if (enabled) 1f else 0.4f).clickable(enabled = enabled, onClick = onClick).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) { icon() }
        Spacer(Modifier.height(2.dp))
        Text(label, color = Color.White, fontSize = 11.sp)
    }
}

@Composable
private fun IconButtonBare(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier.size(40.dp).alpha(if (enabled) 1f else 0.4f).clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) { content() }
}

@Composable
private fun RecordingTimer(recordedMs: Long, modifier: Modifier = Modifier) {
    fun fmt(ms: Long) = "%d:%02d".format(ms / 60_000, (ms / 1000) % 60)
    Row(
        modifier.clip(RoundedCornerShape(50)).background(Color.Black.copy(alpha = 0.5f)).padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(Color.Red))
        Spacer(Modifier.width(8.dp))
        Text("${fmt(recordedMs)} / ${fmt(MAX_VIDEO_MS)}", color = Color.White, fontSize = 13.sp)
    }
}

@Composable
private fun ThumbnailStrip(
    items: List<StoryMediaItem>,
    selectedIndex: Int,
    imageLoader: ImageLoader,
    onSelect: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(items, key = { _, it -> it.id }) { index, item ->
            val shape = RoundedCornerShape(12.dp)
            Box(
                Modifier
                    .size(54.dp)
                    .clip(shape)
                    .then(if (index == selectedIndex) Modifier.border(2.dp, Orange, shape) else Modifier)
                    .clickable { onSelect(index) }
            ) {
                AsyncImage(
                    model = item.uri,
                    imageLoader = imageLoader,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (item.type == StoryMediaType.VIDEO) {
                    Icon(
                        Icons.Default.PlayArrow, null, tint = Color.White,
                        modifier = Modifier.align(Alignment.BottomStart).padding(2.dp).size(16.dp)
                    )
                }
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable { onRemove(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, "Remove", tint = Color.White, modifier = Modifier.size(11.dp))
                }
            }
        }
    }
}

@Composable
private fun PostStoryButton(enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(54.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFB86A2E), Color(0xFFE9A94B), Color(0xFFB86A2E))))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Post Story", color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.Black, modifier = Modifier.size(18.dp))
        }
    }
}

private fun newMediaFile(context: Context, extension: String): File {
    val dir = File(context.cacheDir, "story_media").apply { mkdirs() }
    return File(dir, "STORY_${System.currentTimeMillis()}.$extension")
}

/*
 * ---------------------------------------------------------------------
 * Setup
 *
 * Gradle:
 *   val camerax = "1.3.4"
 *   implementation("androidx.camera:camera-core:$camerax")
 *   implementation("androidx.camera:camera-camera2:$camerax")
 *   implementation("androidx.camera:camera-lifecycle:$camerax")
 *   implementation("androidx.camera:camera-video:$camerax")
 *   implementation("androidx.camera:camera-view:$camerax")
 *   implementation("androidx.media3:media3-exoplayer:1.3.1")
 *   implementation("androidx.media3:media3-ui:1.3.1")
 *   implementation("io.coil-kt:coil-compose:2.6.0")
 *   implementation("io.coil-kt:coil-video:2.6.0")          // video thumbnails
 *   implementation("androidx.compose.material:material-icons-extended")
 *   implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
 *   implementation("com.google.code.gson:gson:2.10.1")
 *
 * Manifest:
 *   <uses-feature android:name="android.hardware.camera.any" android:required="false" />
 *   <uses-permission android:name="android.permission.CAMERA" />
 *   <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *   (No FileProvider or storage permission needed anymore: CameraX writes to
 *    the app's cache dir, and the gallery uses the system Photo Picker.)
 *
 * Usage:
 *   StoryCameraScreen(onClose = { navController.popBackStack() }, onPosted = { ... })
 * ---------------------------------------------------------------------
 */
