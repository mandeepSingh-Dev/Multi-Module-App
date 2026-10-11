package com.app.feature_home.ui

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush5
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.grey_8
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_14
import com.app.core_ui.ui.theme.theme_color_24
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20

@Composable
@Preview
fun HelpSupportScreen(backStack : NavBackStack<NavKey> = LocalBackStackNav.current) {


    BoxCommon(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            HeaderView2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                title = "Help & Support",
                secTitle = "Get quick answers and support whenever you need it."
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }    )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                HereToHelpCard(modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, top = 20.dp)) {
                    Image(painterResource(R.drawable.faq_iconbackground_border_shadow), contentDescription = null)
                    Column(modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("FAQ", fontSize = 16.sp)
                        Text("Find answers to common questions", color = text_grey_1)
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp, top = 15.dp)
                    .dropShadow(Shape_20, shadow = Shadow(color = Color.Black.copy(0.05f), radius = 20.dp))
                    .clip(Shape_20)
                    .border(width = 1.dp, color = grey_8, shape = Shape_20)
                    .background(color = Color.White, shape = Shape_20)
                    .innerShadow(Shape_20, shadow = Shadow(color = Color.Black.copy(0.1f), radius = 20.dp))
                )
                {
                    repeat(10){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                            .clickable{}
                            .padding(horizontal = 15.dp, vertical = 20.dp)) {
                            Text("How can I earn more coin?", modifier = Modifier.weight(1f))
                            Image(painterResource(R.drawable.right_arrow), contentDescription = null, modifier = Modifier.size(10.dp))
                        }
                        HorizontalDivider(thickness = 1.dp, color = divider_color_1, modifier = Modifier.fillMaxWidth())
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, top = 20.dp)) {
                    Image(painterResource(R.drawable.faq_iconbackground_border_shadow), contentDescription = null)
                    Column(modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Your Concern", fontSize = 16.sp)
                        Text("Can't find the answer? We're here to help!", color = text_grey_1)
                    }
                }

                ContactSupportTeamCard()

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun HereToHelpCard(modifier: Modifier){

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
            .dropShadow(shape = Shape_20, shadow = Shadow(color = theme_color_1.copy(0.8f), radius = 5.dp))
        .background(brush = brush5(), shape = Shape_20)
        .padding(10.dp)) {

        Image(painterResource(R.drawable.help_imgsvg), contentDescription = null)
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("We're Here to Help!", fontSize = 20.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
            Text("Need assistance?\nOur support team is always ready to help you.",
                lineHeight = 19.sp,
                fontSize = 13.sp)
        }
    }
}


@Composable
private fun ContactSupportTeamCard(){


    Column(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 20.dp, top = 15.dp)
        .dropShadow(Shape_15, shadow = Shadow(color = Color.Black.copy(0.05f), radius = 20.dp))
        .clip(Shape_15)
        .border(width = 1.dp, color = grey_8, shape = Shape_15)
        .background(color = Color.White, shape = Shape_15)
        .innerShadow(Shape_15, shadow = Shadow(color = Color.White, radius = 20.dp))
        .padding(15.dp)
    )
    {

        Text("Contact Out Support Team", color = Color.Black, fontSize = 16.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)


        Row(modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = theme_color_24, Shape_15)
                    .background(color = theme_color_14, Shape_15)
                    .padding(horizontal = 5.dp, vertical = 10.dp)
            ) {

                Image(painterResource(R.drawable.faq_iconlive_chat), contentDescription = null)
                Text("Live Chat",
                    textAlign = TextAlign.Center
                    )
                Text("Chat with us", color = text_grey_1, fontSize = 11.sp,
                    textAlign = TextAlign.Center
                    )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = theme_color_24, Shape_15)
                    .background(color = theme_color_14, Shape_15)
                    .padding(horizontal = 5.dp, vertical = 10.dp)

            ) {

                Image(painterResource(R.drawable.email_us), contentDescription = null)
                Text("Email Us",
                    textAlign = TextAlign.Center
                    )
                Text("kapilsharma1998@gmail.com", color = text_grey_1, fontSize = 11.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = theme_color_24, Shape_15)
                    .background(color = theme_color_14, Shape_15)
                    .padding(horizontal = 5.dp, vertical = 10.dp)
            ) {

                Image(painterResource(R.drawable.call_us), contentDescription = null)
                Text("Call Us", textAlign = TextAlign.Center)
                Text("+91 1234567890", color = text_grey_1, fontSize = 11.sp)
            }
        }
        GradientButton(onClick = {},
            text = "Your Concern",
            showEndIcon = true,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 15.dp))
    }


}