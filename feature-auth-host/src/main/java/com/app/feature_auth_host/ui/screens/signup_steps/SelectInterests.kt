package com.app.feature_auth_host.ui.screens.signup_steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.interestCard
import com.app.core_ui.components.padding
import com.app.feature_auth_host.ui.states.SignUpScreenUIState
import com.app.feature_auth_host.ui.viewmodels.SignUpViewModel
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun SelectInterests(
    modifier: Modifier = Modifier, onNext: () -> Unit,
    uiState: SignUpScreenUIState?,
    signUpViewModel: SignUpViewModel
) {


    LaunchedEffect(Unit) {
        signUpViewModel.getAllInterests()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .animateContentSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            GradientText(
                text = "Select Your Interests?", fontSize = 20.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            Text(
                "Choose topics to enjoy talking about", textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))

            AnimatedVisibility(
                (uiState?.interestLoading ?: false) && uiState.interests.isNullOrEmpty()
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3,
                ) {
                    repeat(10) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(Random.nextInt(IntRange(80, 200)).dp)
                                .height(30.dp)
                        )
                    }
                }
            }
            AnimatedVisibility(
                !uiState?.interests.isNullOrEmpty()
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3,
                ) {
                    uiState?.interests?.forEach {
                        interestCard(
                            it.name,
                            selected = it.isSelected,
                            onSelect = {
                                signUpViewModel.updateInterests(it)
                            },
                        )

                    }
                }
            }
        }
        GradientButton(
            onClick = onNext,
            text = "Next",
            showEndIcon = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, top = 2.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))
    }


}



