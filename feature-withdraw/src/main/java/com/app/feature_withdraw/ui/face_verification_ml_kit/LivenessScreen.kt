package com.app.feature_withdraw.ui.face_verification_ml_kit

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.core_ui.components.GradientButton
import java.io.File
import java.util.concurrent.Executors

@Composable
fun LivenessScreen(
    vm: LivenessViewModel = viewModel(),
    onVerified: (File) -> Unit
) {

    LaunchedEffect(Unit) {
        vm.reset()
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by vm.state.collectAsStateWithLifecycle()

    val imageCapture = remember { ImageCapture.Builder().build() }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) { onDispose { analysisExecutor.shutdown() } }

    // When all challenges pass, grab a still to send to your backend
    LaunchedEffect(state.step) {
        if (state.step == Step.DONE) {
            val file = File(context.cacheDir, "verified_face_${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(
                ImageCapture.OutputFileOptions.Builder(file).build(),
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) =
                        onVerified(file)

                    override fun onError(e: ImageCaptureException) {
                        Log.e("Liveness", "Capture failed", e)
                    }
                }
            )
        }
    }

    Box(Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).also { previewView ->
                    val providerFuture = ProcessCameraProvider.getInstance(ctx)
                    providerFuture.addListener({
                        val provider = providerFuture.get()

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { it.setAnalyzer(analysisExecutor, FaceAnalyzer(vm::onSignal)) }

                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview, analysis, imageCapture
                        )
                    }, ContextCompat.getMainExecutor(ctx))
                }
            }
        )

        // Instruction card (matches your design)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            Text(state.message, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            StepRow("Look at the camera", state.faceOk)
            StepRow("Blink your eyes naturally", state.blinkOk)
            StepRow("Turn your head left and right", state.turnOk)

            if (state.step != Step.DONE) {
                GradientButton(
                    onClick = { vm.reset() },
                    text = "Restart"
                )
            }
        }
    }
}

@Composable
private fun StepRow(text: String, done: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            if (done) "✓" else "○",
            fontSize = 20.sp,
            color = if (done) Color(0xFF2E7D32) else Color.DarkGray
        )
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 16.sp, color = Color.Black)
    }
}
