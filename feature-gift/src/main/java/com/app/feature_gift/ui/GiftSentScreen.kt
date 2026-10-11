package com.app.feature_gift.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.GradientText
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush7
import com.app.core_ui.ui.theme.brush8
import com.app.core_ui.ui.theme.theme_color_5
import com.app.feature_gift.remote.model.response.GiftListResponse


@Composable
fun GiftSentScreen(
    giftData: GiftListResponse.GiftData?,
    onDismiss: () -> Unit
) {


    if(giftData != null) {
        Dialog(onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = true)
            ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {



                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                )
                {

                    GradientText(
                        text = "Gift Sent",
                        brush = brush8()
                    )

                    AsyncImageWithLoader(
                        giftData.image,
                        modifier = Modifier.size(130.dp).padding(top = 10.dp)
                    )

                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                                )
                            ) {
                                append("You sent ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    brush = brush7(),
                                    fontSize = 17.sp,
                                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                                )
                            ) {
                                append(giftData.name)
                            }
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Text("A special gift to make your moment shine.", color = Color.White, fontSize = 13.sp)
                        Image(painterResource(R.drawable.single_coin), contentDescription = null)
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 25.dp)
                            .border(width = 1.dp, color = theme_color_5, shape = CircleShape)
                            .background(color = Color.Black, shape = CircleShape)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.multi_coinsimage46),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                                .size(20.dp)
                        )
                            Text(
                                "-${giftData.coinCost ?: 0} Coins",
                                color = Color.White,
                                fontFamily = HAFFER_TRIAL_MEDIUM
                            )
                    }

                }

                Box(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp).clip(CircleShape)
                    .clickable{
                        onDismiss()
                    }.background(Color.White, CircleShape)
                    .padding(5.dp),
                    contentAlignment = Alignment.Center){
                    Image(painterResource(R.drawable.baseline_close_24), contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Black))
                }
            }
        }
    }
}