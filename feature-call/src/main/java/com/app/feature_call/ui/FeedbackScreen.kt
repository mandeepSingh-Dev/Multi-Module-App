package com.app.feature_call.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncProfileImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.HeaderView3
import com.app.core_ui.components.RatingBar
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush6
import com.app.core_ui.ui.theme.grey_3
import com.app.core_ui.ui.theme.hint_color
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.feature_call.ui.events.FeedbackScreenEvents
import com.app.feature_call.ui.viewmodels.FeedbackViewModel
import com.zodiaq.ui.theme.Shape_15

@Composable
fun FeedbackScreen(
    partnerId: String?,
    callId: String?,
    profilePic: String?,
    feedbackViewModel: FeedbackViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSuccess: () -> Unit,
) {


    val uiState by feedbackViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        feedbackViewModel.updateState { state ->
            state.copy(
                reviewRequestBody = state.reviewRequestBody?.copy(
                    callId = callId,
                    partnerId = partnerId,
                )
            )
        }
    }

    ObserveUIEvents(feedbackViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                context.showToast(uIEvent.error)
            }

            is FeedbackScreenEvents.FeedbackSubmittedSuccess -> {
                context.showToast(message = uIEvent.msg)
                onSuccess()
            }
        }
    }



    BoxCommon(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            HeaderView3(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                title = "Feedback",
                onBack = onBack,
                showNotificationIcon = false
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                ProfileImageView(
                    image = profilePic,
                    modifier = Modifier.padding(top = 20.dp)
                )
                GradientText(
                    "How was your\nconversation?",
                    modifier = Modifier.padding(top = 20.dp)
                )
                RatingBar(
                    rating = uiState?.reviewRequestBody?.rating ?: 0,
                    onRatingChanged = {
                        feedbackViewModel.updateState { state ->
                            state.copy(reviewRequestBody = state.reviewRequestBody?.copy(rating = it))
                        }
                    },
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    "Your feedback helps us improve and ensure better connections",
                    modifier = Modifier.padding(horizontal = 20.dp, top = 15.dp),
                    textAlign = TextAlign.Center
                )

                WriteFeedbackCard(
                    modifier = Modifier.padding(top = 30.dp, horizontal = 20.dp),
                    message = uiState?.reviewRequestBody?.comment ?: "",
                    onChange = {
                        feedbackViewModel.updateState { state ->
                            state.copy(reviewRequestBody = state.reviewRequestBody?.copy(comment = it))
                        }
                    },
                )

                AnimatedContent(uiState?.isLoading ?: false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp, horizontal = 20.dp, bottom = 10.dp)
                ) { isLoading ->
                    if (isLoading ) {
                        CircularProgressIndicator()
                    } else {
                        GradientButton(
                            onClick = {
                                feedbackViewModel.addReview()
                            },
                            text = "Submit Rating",
                            showEndIcon = true,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun ProfileImageView(
    image: String?,
    modifier: Modifier
) {




        Box(
            modifier = modifier
                .size(130.dp)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(color = theme_color_1.copy(0.5f), radius = 10.dp)
                )
                .clip(CircleShape)
                .background(brush = brush6(), shape = CircleShape)
                .padding(3.dp)
        ) {
            AsyncProfileImageWithLoader(
                image = image,
                modifier = Modifier.fillMaxSize()
                    .clip(CircleShape)
            )
        }

}


@Composable
private fun WriteFeedbackCard(
    modifier: Modifier,
    message: String?,
    onChange: (String?) -> Unit
) {


    Column(
        modifier = modifier
            .dropShadowCustom(Shape_15, Color.Black.copy(0.1f))
            .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painterResource(R.drawable.feedback_iconbackground),
                contentDescription = null
            )
            Text(
                "Write feedback",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                "(Optional)",
                fontSize = 11.sp,
                color = text_grey_5,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .border(width = 1.dp, color = grey_3, Shape_15)
                .padding(15.dp)
        ) {
            BasicTextField(
                value = message ?: "",
                onValueChange = onChange,
                minLines = 6,
                decorationBox = {
                    if (message.isNullOrEmpty()) {
                        Text("Write feedback...", color = hint_color, fontSize = 12.sp)
                    }
                    it()
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${message?.length}/250",
                color = text_grey_5,
                modifier = Modifier.align(Alignment.End)
            )

        }

    }

}