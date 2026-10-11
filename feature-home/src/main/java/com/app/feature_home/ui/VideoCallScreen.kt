package com.app.feature_home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_18
import com.app.core_ui.ui.theme.theme_color_22
import com.zodiaq.ui.theme.Shape_20

@Composable
@Preview
fun VideoCallScreen() {


    BoxCommon(
        modifier = Modifier.fillMaxSize(),
        isAppearanceLightStatusBars = false
    ) {

        Image(
            painterResource(R.drawable.girl_large),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            CallScreenHeader(modifier = Modifier.padding(top = 20.dp))
            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(color = Color.Black.copy(0.4f), shape = CircleShape)
                    .padding(horizontal = 15.dp, vertical = 8.dp)
            ) {
                Image(painterResource(R.drawable.icon_videoverified), contentDescription = null)
                Text(
                    "End-to-end Encrypted",
                    color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f, false)
                )
                Image(painterResource(R.drawable.icon_videolock), contentDescription = null)
            }
            VideoCallButtonControllers(modifier = Modifier.padding(horizontal = 20.dp, top = 10.dp))
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun RatesPerMinCard(rateFormatted : String){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.clip(CircleShape)
        .background(shape = CircleShape, color = theme_color_22)
        .border(width = 1.dp, color = theme_color_1,CircleShape)
        .padding(horizontal = 15.dp, vertical = 4.dp)) {

        Image(painterResource(R.drawable.multi_coinsimage46), modifier = Modifier.size(12.dp), contentDescription = null)
        Text(rateFormatted, fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 13.sp, color = Color.Black)
    }
}

@Composable
private fun CallScreenHeader(modifier: Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(color = Color.Black.copy(0.4f), shape = CircleShape)
                .padding(horizontal = 5.dp, vertical = 5.dp)
        )
        {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
                    .padding(1.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.placeholderbackground),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(
                modifier = Modifier.padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    "Aisha", fontFamily = HAFFER_TRIAL_BOLD,
                    color = Color.White, fontSize = 15.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(7.dp)
                            .background(green_4)
                    )
                    Text("Online", color = Color.White, fontSize = 11.sp)
                }
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = Color.Black.copy(0.4f), shape = CircleShape)
                    .padding(horizontal = 15.dp, vertical = 5.dp)
            ) {
                Text("00:12:45", color = Color.White, fontSize = 13.sp)
            }
        }


        Image(
            painter = painterResource(R.drawable.icon_videobuttonpictureinpicture),
            contentDescription = null,
        )
        Image(
            painter = painterResource(R.drawable.icon_videobuttonsecurity),
            contentDescription = null,
        )

    }

}


@Composable
fun VideoCallButtonControllers(modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.call_iconscamera_switch),
                contentDescription = null
            )
            Text(
                "Switch",
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.call_iconsmic_off),
                contentDescription = null
            )
            Text("Mute", color = Color.White, fontSize = 13.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.call_iconsspeaker_close),
                contentDescription = null
            )
            Text(
                "Speaker",
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(painter = painterResource(R.drawable.call_iconsgift), contentDescription = null)
            Text("Gift", color = Color.White, fontSize = 13.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.call_iconsend_call),
                contentDescription = null
            )
            Text(
                "End Call",
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
        }
    }
}