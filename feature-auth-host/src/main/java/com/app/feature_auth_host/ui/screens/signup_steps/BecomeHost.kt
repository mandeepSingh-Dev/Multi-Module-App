package com.app.feature_auth_host.ui.screens.signup_steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_2
import com.zodiaq.ui.theme.Shape_15


@Composable
fun BecomeHost(modifier: Modifier,
               onBack : () -> Unit,
               onNext: () -> Unit) {

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
                text = "Become a Host", fontSize = 20.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
            Text(
                "Connect, Chat and earn your terms", textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 30.dp, end = 30.dp)
            )

            Image(
                painterResource(R.drawable.phonebecomehost), contentDescription = null,
                modifier = Modifier.padding(top = 20.dp)
            )

            BecomeHostCard(
                modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp),
                icon = R.drawable.phoneearnbycalling,
                title = "Earn by chatting",
                desc = "Chat, connect and earn more!",
                onClick = {},
            )
            BecomeHostCard(
                modifier = Modifier.padding(horizontal = 20.dp, top = 10.dp),
                icon = R.drawable.phoneflexibleschedule,
                title = "Flexible Schedule",
                desc = "Be available whenever you want. You're in control.",
                onClick = {},
            )
            BecomeHostCard(
                modifier = Modifier.padding(horizontal = 20.dp, top = 10.dp),
                icon = R.drawable.phonegrowbuild,
                title = "Grow & Build Connections",
                desc = "Meet amazing people and build meaningful connections",
                onClick = {},
            )
            BecomeHostCard(
                modifier = Modifier.padding(horizontal = 20.dp, top = 10.dp),
                icon = R.drawable.phonesafesecure,
                title = "Safe & Secure",
                desc = "Your safety and privacy are our top priority",
                onClick = {},
            )

            Spacer(modifier = Modifier.height(20.dp))

        }
        GradientButton(
            onClick = onNext,
            text = "Get Started",
            showEndIcon = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, top = 2.dp)
        )


        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("Already have an account? ")
            }
            withLink(
                link = LinkAnnotation.Clickable(
                    tag = "Login",
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = theme_color_2,
                            fontFamily = HAFFER_TRIAL_MEDIUM
                        )
                    ),
                    linkInteractionListener = { link ->
                        onBack()
//                                loginViewModel.onEvent(CommonUiEvent.NavigateTo(HomeScreens.TermsConditionsScreen(1)))
                    }
                )
            ) {
                append("Login")
            }
        }, modifier = Modifier.padding(top = 10.dp))

        Spacer(modifier = Modifier.height(5.dp))
    }

}
@Composable
fun BecomeHostCard(
    modifier: Modifier,
    icon: Int,
    title: String,
    desc: String,
    onClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()

            .dropShadowCustom(shape = Shape_15, color = theme_color_1.copy(0.2f), radius = 10.dp)
            .clip(Shape_15)

            .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)
    ) {
        Image(painterResource(icon), contentDescription = null)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(title, fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 16.sp)
            Text(desc, fontSize = 12.sp, color = text_grey_1)
        }

//        Image(painterResource(R.drawable.right_arrow),modifier = Modifier.size(12.dp), colorFilter = ColorFilter.tint(color = Color.Black),
//            contentDescription = null)
    }
}