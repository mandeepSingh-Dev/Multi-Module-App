package com.app.feature_withdraw.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.decode.ImageSource
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.padding
import okhttp3.internal.http2.Header
@Composable
fun VerificationStepper(
    currentStep: VerificationStep,
    modifier: Modifier = Modifier
)
{

    val steps = listOf(
        VerificationStep.IDENTITY,
        VerificationStep.FACE_SCAN,
        VerificationStep.COMPLETED
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        steps.forEachIndexed { index, step ->

            StepItem(
                step = step,
                currentStep = currentStep,
            )

            if (index < steps.lastIndex) {

                AnimatedStepLine(
                    isCompleted = currentStep.ordinal > index,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun StepItem(
    step: VerificationStep,
    currentStep: VerificationStep,
)
{

    val isCompleted =
        currentStep.ordinal > step.ordinal

    val isActive =
        currentStep == step

    val scale by animateFloatAsState(
        targetValue =
            if (isActive) 1.08f else 1f,
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
        label = "stepScale"
    )

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "pulse"
        )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis = 1100,
                        easing = FastOutSlowInEasing
                    ),
                repeatMode =
                    RepeatMode.Reverse
            ),
        label = "pulseAnimation"
    )

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(54.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            contentAlignment =
                Alignment.Center
        ) {

            /*
             * Glow behind active step
             */
            if (isActive) {

                Box(
                    modifier = Modifier
                        .size(
                            (54 * pulse).dp
                        )
                        .clip(CircleShape)
                        .background(
                            Color(0xFF111111)
                                .copy(alpha = 0.08f)
                        )
                )
            }

            /*
             * Outer circle
             */
            /*Canvas(
                modifier = Modifier
                    .matchParentSize()
            ) {

                val strokeWidth =
                    2.5.dp.toPx()

                val color =
                    when {
                        isCompleted ->
                            Color(0xFF22C55E)

                        isActive ->
                            Color(0xFF111111)

                        else ->
                            Color(0xFFE3E3E3)
                    }

                drawCircle(
                    color = color,
                    style = Stroke(
                        width = strokeWidth
                    )
                )
            }*/

            /*
             * Inner circle
             */
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape),
                contentAlignment =
                    Alignment.Center
            ) {

                AnimatedStepIcon(
                    isCompleted = isCompleted,
                    isActive = isActive,
                    step = step
                )
            }
        }
    }
}

@Composable
private fun AnimatedStepIcon(
    isCompleted: Boolean,
    isActive: Boolean,
    step: VerificationStep
)
{

    AnimatedContent(
        targetState = isCompleted,
        transitionSpec = {

            (scaleIn(
                animationSpec =
                    tween(250)
            ) + fadeIn())
                .togetherWith(
                    scaleOut(
                        animationSpec =
                            tween(150)
                    ) + fadeOut()
                )
        },
        label = "iconTransition"
    ) { completed ->

        if (completed) {
            val img = when (step) {
                VerificationStep.IDENTITY -> painterResource(R.drawable.phone_done_)
                VerificationStep.FACE_SCAN -> painterResource(R.drawable.face_done)
                VerificationStep.COMPLETED -> painterResource(R.drawable.voice_selected)
            }
            Image(
                img,
                contentDescription = null,
                modifier =
                    Modifier.size(35.dp)
            )

        } else {
            val img = when (step) {
                VerificationStep.IDENTITY -> painterResource(R.drawable.phone_selected)
                VerificationStep.FACE_SCAN -> {
                    if(isActive){
                        painterResource(R.drawable.face_selected)
                    }else{
                        painterResource(R.drawable.face_not_selected)
                    }
                }
                VerificationStep.COMPLETED -> {
                    if(isActive){
                        painterResource(R.drawable.voice_selected)
                    }else{
                        painterResource(R.drawable.voice_not_selected)
                    }
                }
            }
            Image(
                img,
                contentDescription = null,
                modifier =
                    Modifier.size(35.dp)
            )
        }
    }
}

@Composable
private fun AnimatedStepLine(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) 
{

    val progress by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Canvas(
        modifier = modifier
            .height(4.dp)
    ) {

        val centerY =
            size.height / 2f

        /*
         * Background line
         */
        drawLine(
            color = Color(0xFFE8E8E8),

            start = Offset(
                0f,
                centerY
            ),

            end = Offset(
                size.width,
                centerY
            ),

            strokeWidth = 3.dp.toPx(),

            cap = StrokeCap.Round
        )

        /*
         * Animated progress
         */
        if (progress > 0f) {

            drawLine(
                color = Color(0xFF111111),

                start = Offset(
                    0f,
                    centerY
                ),

                end = Offset(
                    size.width * progress,
                    centerY
                ),

                strokeWidth = 3.dp.toPx(),

                cap = StrokeCap.Round
            )
        }
    }
}