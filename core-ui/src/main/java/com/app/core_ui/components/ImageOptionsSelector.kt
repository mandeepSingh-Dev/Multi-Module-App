package com.app.core_ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.compose.ui.platform.LocalContext
import com.app.core_ui.R
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.theme_color_1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageOptionsSelector(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCamera: () -> Unit,
    onGallery: () -> Unit,
) {

    val context = LocalContext.current

    val permissionLauncher =
        androidx.activity.compose.rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                onCamera()
            }
        }

    if (showDialog) {

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(
                topStart = 28.dp,
                topEnd = 28.dp
            ),
            tonalElevation = 8.dp,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 4.dp)
                        .size(
                            width = 42.dp,
                            height = 5.dp
                        )
                        .clip(CircleShape)
                        .background(Color(0xFFD9D9D9))
                )
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        start = 22.dp,
                        end = 22.dp,
                        top = 8.dp,
                        bottom = 24.dp
                    )
            ) {

                // Header
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Choose an option",
                        fontFamily = HAFFER_TRIAL_BOLD,
                        fontSize = 22.sp,
                        color = theme_color_1
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Select how you want to add your image",
                        fontFamily = HAFFER_TRIAL_MEDIUM,
                        fontSize = 13.sp,
                        color = Color(0xFF858585)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    ImageOptionCard(
                        modifier = Modifier.weight(1f),
                        icon = R.drawable.baseline_camera_alt_24,
                        title = "Camera",
                        subtitle = "Take a photo",
                        onClick = {

                            val isGranted =
                                ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                            if (isGranted) {
                                onCamera()
                            } else {
                                permissionLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                            }
                        }
                    )

                    ImageOptionCard(
                        modifier = Modifier.weight(1f),
                        icon = R.drawable.baseline_insert_photo_24,
                        title = "Gallery",
                        subtitle = "Choose from photos",
                        onClick = {
                            onGallery()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}


@Composable
private fun ImageOptionCard(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    val cardShape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .clip(cardShape)
            .border(
                width = 1.dp,
                color = Color(0xFFE9E5E2),
                shape = cardShape
            )
            .background(Color(0xFFFFFCFA))
            .clickableDebAnim {
                onClick()
            }
            .padding(
                horizontal = 14.dp,
                vertical = 18.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Icon container
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(theme_color_1.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(27.dp),
                tint = theme_color_1
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = title,
            fontFamily = HAFFER_TRIAL_BOLD,
            fontSize = 15.sp,
            color = theme_color_1
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = subtitle,
            fontFamily = HAFFER_TRIAL_MEDIUM,
            fontSize = 11.sp,
            color = Color(0xFF898989)
        )
    }
}