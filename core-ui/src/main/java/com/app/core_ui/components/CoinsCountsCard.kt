package com.app.core_ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.theme_color_10
import com.app.core_ui.ui.theme.theme_color_13

@Composable
fun CoinsCountsCard(balance: Int?,
                    modifier: Modifier = Modifier,
                    isWalletLoading : Boolean = false,
                    onClickCoins : () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickableDebAnim{
                onClickCoins()
            }
            .border(width = 1.dp, color = theme_color_10, shape = CircleShape)
            .background(color = theme_color_13, shape = CircleShape)
            .padding(horizontal = 5.dp, vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.coinsbackground_shadow),
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp)
        )
        AnimatedContent(balance) {balance ->
            if(balance == null && isWalletLoading){
             ShimmerBox(modifier = Modifier.width(40.dp).height(15.dp))
            }else {
                Text(
                    balance.toString(), fontSize = 13.sp,
                    fontFamily = HAFFER_TRIAL_MEDIUM
                )
            }
        }
        Image(painter = painterResource(R.drawable.addbutton), contentDescription = null)
    }

}