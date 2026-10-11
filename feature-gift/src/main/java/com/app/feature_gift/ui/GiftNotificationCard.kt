package com.app.feature_gift.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_common.utils.fromJson
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.brushGrey2
import com.app.core_ui.ui.theme.theme_color_1
import com.uae.core_socket.model.GiftReceivedResponseX
import com.zodiaq.ui.theme.Shape_20


@Composable
fun GiftNotificationCard(
    giftNotificationData: Any? = false,
    title: String = "You received a gift!",
    subtitle: String = "Sent a Rose",
    emoji: String = "🌹",
    rewardText: String = "+50 Beans",
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        giftNotificationData != null,
        enter = slideInVertically(initialOffsetY = {
            -it
        }, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)),
        exit = slideOutVertically(targetOffsetY = {
            -it
        }, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    ) {

        val giftData = giftNotificationData?.toString()?.fromJson<GiftReceivedResponseX>()

        Box(
            modifier = modifier
                .fillMaxWidth()
                .dropShadowCustom(shape = Shape_20)
                .clip(Shape_20)
                .background(brush = brushGrey2())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                // Gift box icon

                Image(
                    painterResource(R.drawable.gift_smallimage63), contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Title + subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontFamily = HAFFER_TRIAL_BOLD,
                        color = Color.Black
                    )
                    Text(
                        text = "Sent a ${giftData?.gift?.name}",
                        fontSize = 14.sp,
                        color = Color(0xFF8A8A8A)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Emoji + reward text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImageWithLoader(
                        giftData?.gift?.image,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${giftData?.gift?.coinCost ?: 0} Beans",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }


            Image(
                painterResource(R.drawable.baseline_close_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(theme_color_1),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .clickable {
                        onDismiss()
                    })

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GiftNotificationCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
//        GiftNotificationCard()
    }
}
 