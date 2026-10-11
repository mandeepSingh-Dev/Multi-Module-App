package com.app.feature_auth_host.ui.screens.signup_steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.green_12
import com.app.core_ui.ui.theme.green_13
import com.app.core_ui.ui.theme.green_14
import com.app.core_ui.ui.theme.grey_7
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_24
import com.app.core_ui.ui.theme.theme_color_25
import com.app.feature_auth_host.ui.states.SignUpScreenUIState
import com.app.feature_auth_host.ui.viewmodels.SignUpViewModel
import com.uae.core_network.data.model.LanguageListResponse
import com.zodiaq.ui.theme.Shape_15

@Composable
fun SelectLanguage(
    modifier: Modifier,
    uiState: SignUpScreenUIState?,
    signUpViewModel: SignUpViewModel,
    onNext: () -> Unit
) {

    LaunchedEffect(Unit) {
        uiState?.profileRequestBody?.languages?.let {
            signUpViewModel.selectLanguages(it )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GradientText(
                text = "Select Language", fontSize = 20.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            Text(
                "Choose the languages you're comfortable chatting in.",
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text("Popular Languages", fontFamily = HAFFER_TRIAL_SEMI_BOLD)

            Spacer(modifier = Modifier.height(5.dp))
            AnimatedVisibility(uiState?.languagesLoading ?: false,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp))
            }

            if(!uiState?.languages.isNullOrEmpty()) {
                uiState.languages.chunked(3).forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .padding(horizontal = 20.dp, top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        it.forEach { item ->
                            languageCard(
                                modifier = Modifier.weight(1f)
                                    .fillMaxHeight(),
                                isSelected = item?.isSelected ?: false,
                                language = item,
                                onChange = {
                                    signUpViewModel.updatesLanguages(item)
                                }
                            )
                        }
                        if (it.size < 2) {
                            Box(modifier = Modifier.weight(1f))
                            Box(modifier = Modifier.weight(1f))
                        } else if (it.size < 3) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            MoreLanguageKnowCard(modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp))

            Spacer(modifier = Modifier.height(20.dp))
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

@Composable
fun languageCard(
    modifier: Modifier,
    isSelected: Boolean = false,
    onChange: () -> Unit,
    language: LanguageListResponse.Language?
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .dropShadowCustom(shape = Shape_15, color = Color.Black.copy(0.1f))
            .clip(Shape_15)
            .clickable {
                onChange()
            }
            .border(width = 1.dp, color = grey_7, shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(12.dp)
    ) {

        AnimatedContent(
            isSelected,
            modifier = Modifier.align(Alignment.TopEnd)
        ) { isSelected ->
            if (!isSelected) {
                Image(
                    painterResource(R.drawable.circle_selectorellipse5),
                    contentDescription = null
                )
            } else {
                Image(
                    painterResource(R.drawable.circle_selectedbackground),
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(color = theme_color_24, CircleShape)
                    .padding(5.dp),
            ) {
                Text(
                    language?.code?.uppercase() ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 9.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    lineHeight = 1.sp
                )
            }
            Text(language?.name ?: "", fontSize = 13.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)


            if (language?.isNative ?: false) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .background(color = green_12, Shape_15)
                        .border(width = 1.dp, color = green_13, Shape_15)
                        .padding(horizontal = 8.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painterResource(R.drawable.star_greenicon), contentDescription = null)
                    Text(
                        "Native",
                        color = green_14,
                        fontSize = 10.sp,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD
                    )
                }
            }
        }


    }
}

@Composable
private fun MoreLanguageKnowCard(modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()

            .border(width = 1.dp, color = theme_color_1, shape = Shape_15)
            .background(color = theme_color_25, shape = Shape_15)
            .padding(12.dp)
    ) {

        Image(painterResource(R.drawable.know_languaglang), contentDescription = null)
        Column(modifier = Modifier.weight(1f)) {
            Text("The more languages you know.", fontFamily = HAFFER_TRIAL_MEDIUM)
            Text("the more you can connect and earn!", fontSize = 11.sp)
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(painterResource(R.drawable.star_dectext), contentDescription = null)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.star_dectext), contentDescription = null,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

    }


}