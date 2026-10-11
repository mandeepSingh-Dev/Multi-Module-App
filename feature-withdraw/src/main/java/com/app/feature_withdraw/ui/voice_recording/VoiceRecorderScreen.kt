package com.app.feature_withdraw.ui.voice_recording

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.app.core_ui.R
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brush5
import com.app.core_ui.ui.theme.theme_color_13
import com.app.core_ui.ui.theme.theme_color_24
import com.app.feature_withdraw.ui.face_verification_ml_kit.FaceCameraVerificationScreen
import com.app.feature_withdraw.ui.states.IdentityVerificationUIState
import com.zodiaq.ui.theme.Shape_15


@Composable
fun ColumnScope.VoiceRecorderScreen(
    vm: VoiceRecorderViewModel = viewModel(),
    onRecorded: (File) -> Unit = {},
    onState: (RecState.Recording) -> Unit = {}
) {
    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) vm.start() }

    fun onStartClick() {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) vm.start() else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    // Hand the file to the caller once
    LaunchedEffect(state) {
        (state as? RecState.Done)?.let { onRecorded(it.file) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GradientText(
            text = "Face Verification", fontSize = 20.sp,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Text(
            "We need to verify that you are a real person. Please follow the instructions below.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateContentSize()
        ) {
            when (val s = state) {

                RecState.Idle -> {
                    Image(
                        painter = painterResource(R.drawable.miccentralglowingpulsecircleswithmic),
                        contentDescription = null
                    )
                    Spacer(Modifier.height(24.dp))
                    Text("Say Something", fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 13.sp)
                }

                is RecState.Recording -> {

                    Image(
                        painter = painterResource(R.drawable.miccentralglowingpulsecircleswithmic),
                        contentDescription = null
                    )
                    Spacer(Modifier.height(24.dp))
                    /*"${formatTime(s.elapsedMs)} / 00:30",*/
                    Text(
                        formatTime(s.elapsedMs),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    /*Spacer(Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { s.elapsedMs / VoiceRecorder.MAX_DURATION_MS.toFloat() },
                        modifier = Modifier.fillMaxWidth()
                    )*/
                    Spacer(Modifier.height(32.dp))

                }

                is RecState.Done -> {
                    Text("Recording Done", style = MaterialTheme.typography.titleMedium)
                    Text(s.file.name, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(24.dp))
                    OutlinedButton(onClick = vm::reset) { Text("Record again") }
                }

                is RecState.Error -> {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = vm::reset) { Text("Try again") }
                }
            }
        }
        VoiceRecordingInstructionsCard(modifier = Modifier)

        Spacer(modifier = Modifier.height(5.dp))

        if (state is RecState.Recording) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                // Cancel: discards the recording
                FilledIconButton(
                    onClick = vm::cancel,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color(
                            0xFFD32F2F
                        )
                    )
                ) { Icon(Icons.Default.Close, contentDescription = "Cancel recording") }

                // Finish early and keep it
                FilledIconButton(onClick = vm::finish) {
                    Icon(Icons.Default.Check, contentDescription = "Finish recording")
                }
            }
        }
        if (state !is RecState.Recording) {
            GradientButton(
                onClick = {
                    onStartClick()
                },
                text = "Start Speaking",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        }

    }


}

@Composable
fun VoiceRecordingInstructionsCard(modifier: Modifier) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shape_15)
    )
    {

        Image(
            painter = painterResource(R.drawable.instructioncard_bggroup1261162563),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .zIndex(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(brush = brush5(), shape = Shape_15)
                .padding(15.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)
            )
            {
                Image(
                    painter = painterResource(R.drawable.record_iconimage92),
                    contentDescription = null
                )
                Text("Speak Clearly", fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            VerticalDivider(
                thickness = 1.dp,
                modifier = Modifier
                    .width(1.dp)
                    .padding(horizontal = 3.dp)
                    .fillMaxHeight(),
                color = theme_color_13
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)
            )
            {
                Image(
                    painter = painterResource(R.drawable.record_iconimage93),
                    contentDescription = null
                )
                Text("Be in a quiet place", fontSize = 12.sp, textAlign = TextAlign.Center)
            }
            VerticalDivider(
                thickness = 1.dp,
                modifier = Modifier
                    .width(1.dp)
                    .padding(horizontal = 3.dp)
                    .fillMaxHeight(),
                color = theme_color_13
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)
            )
            {
                Image(
                    painter = painterResource(R.drawable.record_iconimage94),
                    contentDescription = null
                )
                Text("Use your natural voice", fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSec = (ms / 1000).toInt()
    return "%02d:%02d".format(totalSec / 60, totalSec % 60)
}
