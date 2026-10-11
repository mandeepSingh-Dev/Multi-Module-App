package com.app.core_ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.CoinsCountsCard
import com.app.core_ui.components.DragLine
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP_MEDIUM
import com.app.core_ui.ui.theme.green_10
import com.app.core_ui.ui.theme.green_11
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_12
import com.app.core_ui.ui.theme.theme_color_6
import com.app.core_ui.ui.theme.theme_color_7
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.launch
import java.util.SortedMap


@Composable
fun giftItem(
    image: String?,
    name: String?,
    coinCost: Int?,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(130.dp)
            .height(170.dp)
            .clickableDebAnim {
                onClick()
            }
            .border(color = borderColor, width = 1.dp, shape = Shape_15)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White, bgColor
                    )
                ), shape = Shape_15
            )
            .padding(horizontal = 7.dp, vertical = 7.dp)
    ) {
        AsyncImageWithLoader(image, modifier = Modifier.size(50.dp))
        BasicText(
            name ?: "", style = TextStyle(lineHeight = 14.sp, textAlign = TextAlign.Center),
            autoSize = TextAutoSize.StepBased(minFontSize = 6.sp, maxFontSize = 13.sp),
            modifier = Modifier.padding(top = 5.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Image(painterResource(R.drawable.single_coin), contentDescription = null)
            Text((coinCost ?: 0).toString(), fontSize = 14.sp)
        }
    }
}

@Composable
fun GiftCard(
    image: String?,
    name: String?,
    coinCost: Int?,
    width: Dp = 120.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .width(width)
            .clickableDebAnim {
                onClick()
            }
            .dropShadow(
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 5.dp),
                shape = Shape_20
            )
            .clip(Shape_20)
            .background(color = Color.White, Shape_20)
            .padding(horizontal = 5.dp, vertical = 20.dp)
    ) {
        AsyncImageWithLoader(
            image, modifier = Modifier.size(60.dp),
            placeholder = painterResource(R.drawable.kaho_ji_logoimage1)
        )
        Text(name ?: "", textAlign = TextAlign.Center, fontSize = 12.sp)
        Row(
            modifier = Modifier.padding(bottom = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Image(painterResource(R.drawable.single_coin), contentDescription = null)
            Text((coinCost ?: 0).toString(), color = grey_4)
        }
    }
}

@Composable
fun GiftReceivedCard(
    image: String?,
    name: String?,
    count: Int?,
    width: Dp = 80.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .width(width)
            .clickableDebAnim {
                onClick()
            }
            .dropShadow(
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 5.dp),
                shape = Shape_20
            )
            .clip(Shape_20)
            .background(color = Color.White, Shape_20)
            .padding(horizontal = 5.dp, vertical = 8.dp)
    ) {
        AsyncImageWithLoader(
            image, modifier = Modifier.size(40.dp),
            placeholder = painterResource(R.drawable.kaho_ji_logoimage1)
        )
        Text(name ?: "", textAlign = TextAlign.Center, fontSize = 12.sp)


        Text(
            "x${(count ?: 0)}",
            color = theme_color_11,
            fontSize = 12.sp,
            fontFamily = HAFFER_TRIAL_SEMI_BOLD
        )
    }
}