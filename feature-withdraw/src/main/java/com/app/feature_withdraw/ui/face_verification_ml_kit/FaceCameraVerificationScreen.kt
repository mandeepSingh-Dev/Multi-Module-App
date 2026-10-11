package com.app.feature_withdraw.ui.face_verification_ml_kit

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.File


@Composable
fun FaceCameraVerificationScreen(
    onVerified: (verifiedImageFile: File?) -> Unit
) {

    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    var verifiedFile by remember { mutableStateOf<File?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { hasPermission = it }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    when {
        !hasPermission -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Camera permission is required")
        }

        verifiedFile != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            onVerified(verifiedFile)
            Text("Liveness verified ✅\n${verifiedFile!!.name}")
        }

        else -> LivenessScreen(onVerified = { verifiedFile = it })
    }
}