package com.app.core_ui.components

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun UIRequirePermissions(
    permissions: Array<String>,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    manualLaunchPermissionsOnClick: ((() -> Unit) -> Unit)
) {
    val context = LocalContext.current

    var grantState by rememberSaveable {
        mutableStateOf(
            permissions.all {
                ContextCompat.checkSelfPermission(context, it) ==
                        PackageManager.PERMISSION_GRANTED
            }
        )
    }

    Log.d("PERM", "Initial grantState = $grantState")

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        grantState = !result.containsValue(false)

//        val unGrantedPermissions = result.entries.filter { !it.value }.map {
//            it.key
//        }

        if (!grantState) {
            onPermissionDenied()
        } else {
            onPermissionGranted()
        }
    }

    // Run only once when composable first appears
    LaunchedEffect(Unit) {

        if (grantState) {
            // Permissions already granted
            onPermissionGranted()
        } else {
            // Request permissions now
            launcher.launch(permissions)
        }
    }

    // Save launcher for manual trigger
    LaunchedEffect(Unit) {
        manualLaunchPermissionsOnClick {
            launcher.launch(permissions)
        }
    }
}
@Composable
fun CheckLocationProviders(){
    val context = LocalContext.current

    val locationManager = context.getSystemService(LocationManager::class.java)


}