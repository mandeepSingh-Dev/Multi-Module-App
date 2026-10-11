package com.app.feature_referral.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.OutlineButton2
import com.app.core_ui.components.OutlineCustomButton
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.TextFieldOuterLabel2
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP_MEDIUM
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.grey_7
import com.app.core_ui.ui.theme.theme_color_13
import com.app.core_ui.ui.theme.theme_color_23
import com.app.feature_referral.ui.events.ApplyReferralScreenEvents
import com.app.feature_referral.ui.viewmodels.ApplyReferralCodeViewModel
import com.zodiaq.ui.theme.Shape_20

@Composable
@Preview
fun ApplyReferralCodeScreen(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    applyReferralCodeViewModel: ApplyReferralCodeViewModel = hiltViewModel()
) {

    val uiState by applyReferralCodeViewModel.uiState.collectAsStateWithLifecycle()

    ObserveUIEvents(applyReferralCodeViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

            is ApplyReferralScreenEvents.ReferralApplied -> {

                backStack.removeAll(backStack)
                backStack.add(AppScreens.HomeBottomContainerScreen)

            }

            is ApplyReferralScreenEvents.ReferralSkipped -> {

                backStack.removeAll(backStack)
                backStack.add(AppScreens.HomeBottomContainerScreen)

            }
        }
    }

    BoxCommon(modifier = Modifier.imePadding()) {


        Column(modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 20.dp)
        ) {
            OutlineButton2(
                onClick = {
                    applyReferralCodeViewModel.onEvent(ApplyReferralScreenEvents.ReferralSkipped)
                },
                text = "Skip",
                isBorder = true,
                shape = CircleShape,
                horizontalPadding = 20.dp,
                verticalPadding = 10.dp,
                modifier = Modifier.align(Alignment.End)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                Image(
                    painterResource(R.drawable.referral_code_img2), contentDescription = null,
                    modifier = Modifier.padding(top = 20.dp)
                )

                GradientText("Have a Referral Code?")
                Text(
                    "Enter a referral code to join with exclusive benefits and earn rewards together!",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                TextFieldOuterLabel(
                    showShadows = true,
                    leadingIcon = {
                        Image(
                            painterResource(R.drawable.gift_smallcontainer),
                            contentDescription = null
                        )
                    },
                    value = uiState?.referralCode ?: "",
                    onValueChange = {
                        applyReferralCodeViewModel.updateState { state ->
                            state.copy(referralCode = it)
                        }
                    },
                    hint = "Enter referral code (Optional)",
                )

                AnimatedContent(uiState?.isLoading ?: false,) {isLoading ->

                    if(!isLoading){
                        GradientButton(
                            onClick = {
                                applyReferralCodeViewModel.applyReferralCode() },
                            enabled = !uiState?.referralCode.isNullOrEmpty(),
                            text = "Continue",
                            showEndIcon = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        )
                    }else{
                        CircularProgressIndicator(modifier = Modifier.size(25.dp))
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                    Text("OR", color = grey_4)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                }
                DoNotHaveCodeCard()

            }
        }

    }
}

@Composable
fun DoNotHaveCodeCard() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20)
            .background(theme_color_13, Shape_20)
            .border(1.dp, theme_color_23, Shape_20)
            .padding(15.dp)
    ) {
        Image(painterResource(R.drawable.dont_have_code_iconsvg), contentDescription = null)
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Don't have a code?",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                fontSize = HEADING_ROW_SP_MEDIUM
            )
            Text(
                "You can skip this step and continue. You can still enjoy Kahoji!",
                fontSize = 12.sp
            )
        }
    }
}