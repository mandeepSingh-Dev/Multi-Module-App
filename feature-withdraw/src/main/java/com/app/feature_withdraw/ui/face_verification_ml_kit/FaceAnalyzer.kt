package com.app.feature_withdraw.ui.face_verification_ml_kit

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

/** One frame's worth of data extracted from ML Kit. */
data class FaceSignal(
    val faceCount: Int,
    val inFrame: Boolean,
    val leftEyeOpen: Float?,   // 0..1
    val rightEyeOpen: Float?,  // 0..1
    val yaw: Float,            // headEulerAngleY (left/right turn)
    val trackingId: Int?
)

class FaceAnalyzer(
    private val onSignal: (FaceSignal) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // eye-open probability
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setMinFaceSize(0.3f)
            .enableTracking()
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val rotation = imageProxy.imageInfo.rotationDegrees
        // Bounding boxes are returned in the *upright* image coordinate space
        val (w, h) = if (rotation == 90 || rotation == 270)
            imageProxy.height to imageProxy.width
        else
            imageProxy.width to imageProxy.height

        val input = InputImage.fromMediaImage(mediaImage, rotation)

        detector.process(input)
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    onSignal(FaceSignal(0, false, null, null, 0f, null))
                } else {
                    val face = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }!!
                    val box = face.boundingBox
                    val inFrame = box.left > w * 0.05f &&
                            box.right < w * 0.95f &&
                            box.top > h * 0.05f &&
                            box.bottom < h * 0.95f &&
                            box.width() > w * 0.30f
                    onSignal(
                        FaceSignal(
                            faceCount = faces.size,
                            inFrame = inFrame,
                            leftEyeOpen = face.leftEyeOpenProbability,
                            rightEyeOpen = face.rightEyeOpenProbability,
                            yaw = face.headEulerAngleY,
                            trackingId = face.trackingId
                        )
                    )
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}
