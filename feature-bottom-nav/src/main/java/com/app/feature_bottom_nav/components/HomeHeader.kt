package com.app.feature_bottom_nav.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.CoinsCountsCard
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.brush3
import com.app.core_ui.ui.theme.brushBlackAndFade
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_5

@Composable
fun HomeHeader(
    balance: Int?,
    modifier: Modifier = Modifier,
    onClickProfile: () -> Unit,
    onClickCoins: () -> Unit,
    onNotificationClick: () -> Unit,
    profilePic: String?,
    notificationCount: Int?,
    isWalletLoading: Boolean?
) {

    Log.d("fkbnkfnbf", profilePic.toString())

    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
                .padding(4.dp)
        ) {
            ProfileImage(
                image = profilePic,
                modifier = Modifier.fillMaxSize()
                    .clickableDebAnim{
                        onClickProfile()
                    }
            )
        }

        CoinsCountsCard(balance,
            isWalletLoading = isWalletLoading ?: false,
            onClickCoins = {
            onClickCoins()
        })
        Spacer(modifier = Modifier.weight(1f))
        BadgedBox(badge = {
            notificationCount?.let {count ->
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White,


                ) {
                    Text("$count", color = Color.White, fontSize = 9.sp)
                }
            }
        }) {
            Image(
                painter = painterResource(R.drawable.notificationcontainer),
                contentDescription = null,
                modifier = Modifier.clickableDebAnim{
                    onNotificationClick()
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(width = 1.dp, brush = brush3(), shape = CircleShape)
                .background(brush = brushBlackAndFade(), shape = CircleShape)
                .padding(horizontal = 5.dp, vertical = 4.dp)
        ) {
            Image(painter = painterResource(R.drawable.vip_iconimage37), contentDescription = null)
            Text(
                "VIP", fontSize = 13.sp, fontFamily = HAFFER_TRIAL_MEDIUM,
                color = theme_color_5,
                modifier = Modifier.padding(end = 5.dp)
            )
        }
    }

}