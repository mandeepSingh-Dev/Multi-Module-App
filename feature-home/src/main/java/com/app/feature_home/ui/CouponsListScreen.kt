package com.app.feature_home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.padding
import com.app.core_ui.components.strokeGapLineModifier
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brush8
import com.app.core_ui.ui.theme.green_11
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_2
import com.app.core_ui.ui.theme.pink_1
import com.app.core_ui.ui.theme.pink_2
import com.app.core_ui.ui.theme.pink_3
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_22
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import com.zodiaq.ui.theme.Shape_8


@Composable
@Preview
fun CouponsListScreen(backStack: NavBackStack<NavKey> = LocalBackStackNav.current) {

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
                title = "Coupons",
                secTitle = "Use Coupons to get exciting discounts"
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }   )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                CouponTabBars()
                LazyColumn(modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp))
                {
                    items(10){
                        CouponsListItem(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                HowUseCouponCard(modifier = Modifier.padding(horizontal = 20.dp, top = 5.dp))
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}


@Composable
private fun CouponTabBars() {

    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf(
        "Available",
        "Used",
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {

        list.forEachIndexed { index, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .clip(Shape_20)
                    .clickableDebAnim {
                        selectedIndex = index
                    }
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                if (index == selectedIndex) {
                    GradientText(text = item, fontSize = 14.sp)
                } else {
                    Text(
                        item, color =
                            if (selectedIndex == index) theme_color_11 else text_grey_1,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                        fontSize = 14.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .then(
                            if (selectedIndex == index) {
                                Modifier.fillMaxWidth()
                            } else {
                                Modifier
                            }
                        )
                        .height(2.dp)
                        .background(brush = brush2(), Shape_20)
                )

            }
        }
    }
}


@Composable
private fun CouponsListItem(modifier: Modifier) {

    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .dropShadow(shape = Shape_10, shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp))
            .clip(shape = Shape_15)
            .background(color = Color.White, Shape_15)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .background(brush = Brush.horizontalGradient(listOf(pink_1, pink_2)))
                .padding(horizontal = 30.dp, vertical = 30.dp)
        )
        {

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = pink_3, fontSize = 20.sp, letterSpacing = 1.1.sp,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD
                        )
                    ) {
                        append("20")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = pink_3, fontSize = 15.sp, letterSpacing = 1.1.sp,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD
                        )
                    ) {
                        append("%")
                    }
                })
                Text(
                    "OFF", color = pink_3, letterSpacing = 1.1.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
        }

        Column(modifier = Modifier
            .fillMaxHeight()
            .offset(x = (-4).dp)) {

            Image(
                painterResource(R.drawable.top_shapenotchesatdividingline_width32_leftbadge_),
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.down_shapebackground_border),
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .strokeGapLineModifier()
                .fillMaxHeight()
        )
        Column(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 15.dp)) {
            Row(modifier = Modifier) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(color = pink_1, shape = Shape_20)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "PERCENTAGE OFF", fontFamily = HAFFER_TRIAL_SEMI_BOLD, color = pink_3,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .border(width = 1.dp, color = green_4, shape = Shape_20)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "NEW", fontFamily = HAFFER_TRIAL_SEMI_BOLD, color = green_4,
                        fontSize = 11.sp
                    )
                }


            }

            Text("FLAT 20% OFF", fontSize = 16.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD,modifier = Modifier.padding(top = 5.dp))
            Text("Get 20% off on coin packs",modifier = Modifier.padding(top = 3.dp), color = text_grey_2)


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp)) {
                Row(modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Image(painterResource(R.drawable.clock_iconcontainer), contentDescription = null,
                        colorFilter = ColorFilter.tint(color = text_grey_5))
                    Text("Valid till 15 Sep 2026", color = text_grey_5,
                        fontSize = 12.sp)
                }

                GradientButton(onClick = {},
                    text = "Copy",
                    fontSize = 12.sp,
                    verticalPadding = 8.dp,
                    horizontalPadding = 12.dp,
                    shape = Shape_10
                )
            }
        }

    }
}

@Composable
private fun HowUseCouponCard(modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp), shape = Shape_15)
            .clip(Shape_15)
            .background(color = theme_color_22, shape = Shape_15)
            .padding( 15.dp)
    ) {

        Image(painter = painterResource(R.drawable.coupon_icongifticonroundedbadge), contentDescription = null,
            modifier = Modifier.size(30.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)) {
            Text("How to use coupons?", color = theme_color_11, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
            Text("Copy any coupon code and paste it on the checkout screen to avail the offer.", color = text_grey_1, fontSize = 12.sp)
        }
        Image(painter = painterResource(R.drawable.right_arrow), contentDescription = null,
            modifier = Modifier.size(15.dp), colorFilter = ColorFilter.tint(color = Color.Red))

    }

}