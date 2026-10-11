package com.app.feature_withdraw.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import com.app.core_ui.components.GradientText
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brush5
import com.app.core_ui.ui.theme.theme_color_24
import com.app.feature_withdraw.ui.face_verification_ml_kit.FaceCameraVerificationScreen
import com.app.feature_withdraw.ui.states.IdentityVerificationUIState
import com.zodiaq.ui.theme.Shape_15

@Composable
@Preview
fun ColumnScope.FaceVerificationStep() {

            Column(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
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

                Image(
                    painter = painterResource(R.drawable.face_ver_imagegroup1261162560),
                    contentDescription = null
                )

                FaceRecogInstructionsCard(modifier = Modifier)

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

@Composable
fun FaceRecogInstructionsCard(modifier: Modifier) {


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
                .background(brush = brush5(), shape = Shape_15)
                .padding(15.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            )
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.face_recog_instructions_iconscameralook),
                        contentDescription = null
                    )
                    Text(
                        "Look at the camera",
                        fontSize = 12.sp,
                        fontFamily = HAFFER_TRIAL_MEDIUM
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.face_recog_instructions_iconsblink),
                        contentDescription = null
                    )
                    Text(
                        "Blink your eyes naturally",
                        fontSize = 12.sp,
                        fontFamily = HAFFER_TRIAL_MEDIUM
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.face_recog_instructions_iconsturn),
                        contentDescription = null
                    )
                    Text(
                        "Turn your head left and right",
                        fontSize = 12.sp,
                        fontFamily = HAFFER_TRIAL_MEDIUM
                    )
                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.keep_face_framebackground_border),
                    contentDescription = null
                )
                Text(
                    "Keep your face\nin the frame", fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}