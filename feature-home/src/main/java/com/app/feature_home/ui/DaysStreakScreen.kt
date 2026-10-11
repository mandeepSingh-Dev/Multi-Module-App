package com.app.feature_home.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.light_blue_1
import com.app.core_ui.ui.theme.light_blue_2
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_21
import com.app.core_ui.ui.theme.theme_color_22
import com.app.core_ui.ui.theme.theme_color_23
import com.app.feature_home.remote.model.response.DailyStreakResponse
import com.app.feature_home.ui.viewmodels.DailyStreakViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_20

@Composable
@Preview
fun DaysStreakScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    dailyStreakViewModel: DailyStreakViewModel = hiltViewModel()
) {

    val uiState by dailyStreakViewModel.uiState.collectAsStateWithLifecycle()

    ObserveUIEvents(dailyStreakViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.error)
            }
        }
    }

    var showKeepAliveCard by rememberSaveable { mutableStateOf(false) }

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
                title = "7 Days Streak",
                secTitle = "Keep your streak alive and earn more bonus coins every day!"
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                AnimatedVisibility(showKeepAliveCard,
                    enter = slideInVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessVeryLow)){
                        it
                    },
                    exit = slideOutVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessVeryLow)){
                        it
                    }
                ) {
                    KeepStreakAliveCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, top = 20.dp)
                    )
                }
                DaysStreakCard(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, top = 20.dp)
                        .onVisibilityChanged {
                            if (it) {
                                showKeepAliveCard = true
                            }
                        }, isLoading = uiState?.isLoading ?: true,
                    uiState?.dailyStreakData?.days
                )
                UpgradeToVIP(modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp))
                HowItWorks(modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp))

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }
}


@Composable
private fun KeepStreakAliveCard(modifier: Modifier) {

    Box(
        modifier = modifier
            .clip(Shape_20)
            .background(color = Color.Black),
    ) {
        Image(
            painter = painterResource(
                R.drawable.bg_2shapebg
            ),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .padding(10.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.cal_streak3dflipcalendarwithstarcoinbadge_csstransform),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("Keep the Streak Alive!", fontSize = 20.sp, fontFamily = HAFFER_TRIAL_BOLD)
                Text(
                    "Complete your daily actions and earn exciting coins every day",
                    fontSize = 13.sp
                )

            }

        }
    }

}

@Composable
private fun DaysStreakCard(
    modifier: Modifier,
    isLoading: Boolean?,
    days: List<DailyStreakResponse.DailyStreakData.Day?>?
) {

    val count = days?.size ?: 7

    val density = LocalDensity.current

    val selectedIndex by remember { mutableIntStateOf(3) }
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0) }


    val infiniteAnimation = rememberInfiniteTransition()
    val shadowAlpha by infiniteAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(animation = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Log.d("ijndjfbjdfd", y.toString())


    Row(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_20,
                shadow = Shadow(color = Color.Black.copy(shadowAlpha), radius = 10.dp)
            )
            .background(Color.White, Shape_20)
            .padding(horizontal = 10.dp, vertical = 20.dp)
            .animateContentSize()
    ) {

        val currentDayIndex = days?.indexOfFirst { it?.isCurrent == true }

        repeat(count) { index ->

            val bgColor by animateColorAsState(if (currentDayIndex == index) theme_color_21 else light_blue_1)

            val infiniteAnimation = rememberInfiniteTransition()
            val currentBorderColor by infiniteAnimation.animateColor(
                initialValue = theme_color_23,
                targetValue = theme_color_1,
                animationSpec = infiniteRepeatable(animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                    repeatMode = RepeatMode.Reverse
                )
            )


            val borderColor by animateColorAsState(if (currentDayIndex == index) currentBorderColor else Color.Transparent)

            val day = days?.getOrNull(index)



            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)
            )
            {

                Text(
                    maxLines = 2,
                    text = "Day ${day?.day ?: index.plus(1)}",
                    style = TextStyle(fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 12.sp),
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 5.sp,
                        maxFontSize = 11.sp
                    )
                )


                val image1 = when {
                    day?.isCurrent == true -> {
                        R.drawable.day_currentactivebadgewithstar
                    }

                    day?.isLocked == true -> {
                        R.drawable.day_locklockbadge
                    }

                    day?.isCompleted == true -> {
                        R.drawable.day_checkbadge
                    }

                    else -> {
                        // Default/upcoming day
                        R.drawable.day_checkbadge
                    }
                }
                AnimatedContent(
                    isLoading == true && days.isNullOrEmpty(),
                    modifier = Modifier
                        .onSizeChanged {
                            height = it.height
                        }
                        .onGloballyPositioned {
                            Log.d(
                                "kfnbkfnb",
                                it.parentLayoutCoordinates?.boundsInParent()?.right.toString() + " $index"
                            )
                            if (y == 0f) {
                                y = it.parentLayoutCoordinates?.boundsInParent()?.center?.y
                                    ?: 0f
                            }
//                            Log.d("kfnbkfnb",it.parentCoordinates?.boundsInParent()?.center?.y.toString() + " $index")
                        }) { isLoading ->
                    if (isLoading) {
                        ShimmerBox(modifier = Modifier.size(30.dp), shape = CircleShape)
                    } else {
                        Image(
                            painter = painterResource(image1),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)

                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .border(width = 1.dp, color = borderColor, Shape_10)
                        .background(color = bgColor, Shape_10)
                        .padding(horizontal = 10.dp, vertical = 12.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.starbackground_border_shadow),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    if (isLoading == true && days.isNullOrEmpty()) {
                        ShimmerBox(modifier = Modifier
                            .width(20.dp)
                            .height(10.dp))
                    } else {
                        BasicText(
                            maxLines = 1,
                            text = "${day?.reward ?: 0}",
                            style = TextStyle(fontFamily = HAFFER_TRIAL_BOLD),
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 5.sp,
                                maxFontSize = 15.sp
                            )
                        )
                    }
                }
            }
//            if (index < (currentDayIndex ?: 0)) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .offset({
                        IntOffset(
                            x = 0,
                            y = (with(density) {
                                (y.toInt().toDp()) + height.toDp()
                            }.value.toInt())
                        )
                    }),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = if (index < (currentDayIndex ?: 0)) theme_color_11 else grey_9
                        )
                )
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = if (index < (currentDayIndex ?: 0)) theme_color_11 else grey_9
                        )
                )
            }
//            }
        }

    }
}

@Composable
private fun UpgradeToVIP(modifier: Modifier) {

    Column(
        modifier = modifier
            .clip(Shape_20)
            .border(width = 1.dp, color = theme_color_23, Shape_20)
            .background(color = theme_color_22, shape = Shape_20)
            .padding(horizontal = 5.dp, vertical = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(painterResource(R.drawable.vip_iconimage37), contentDescription = null)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                            fontSize = 16.sp
                        )
                    ) {
                        append("VIP users get ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = theme_color_11,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                            fontSize = 16.sp
                        )
                    ) {
                        append("2X ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                            fontSize = 16.sp
                        )
                    ) {
                        append("rewards!")
                    }
                })

                Text(
                    "Upgrade to VIP and earn double coins for your daily streak.",
                    fontSize = 13.sp, color = text_grey_1
                )
            }
        }
        GradientButton(
            onClick = {},
            text = "Upgrade to VIP",
            verticalPadding = 12.dp,
            horizontalPadding = 20.dp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 2.dp)
        )
    }
}


@Composable

private fun HowItWorks(modifier: Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_20,
                shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp)
            )
            .background(Color.White, Shape_20)
            .padding(15.dp)
    ) {
        Text("How it works?", fontSize = HEADING_ROW_SP, fontFamily = HAFFER_TRIAL_SEMI_BOLD)

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                Image(painterResource(R.drawable.cal_how), contentDescription = null)

                Text(
                    "1. Complete Daily", fontFamily = HAFFER_TRIAL_MEDIUM,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Finish your daily tasks to maintain your streak.", color = text_grey_1,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp

                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)
            ) {
                Image(painterResource(R.drawable.coin_how), contentDescription = null)

                Text(
                    "2. Earn Coins Daily", fontFamily = HAFFER_TRIAL_MEDIUM,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Get exciting coins based on the day of your streak.", color = text_grey_1,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp

                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                Image(painterResource(R.drawable.cal_how), contentDescription = null)

                Text(
                    "1. Complete Daily", fontFamily = HAFFER_TRIAL_MEDIUM,
                    textAlign = TextAlign.Center
                )
                Text(
                    "The longer your streak, the bigger your rewards!", color = text_grey_1,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 5.dp)
                .clip(Shape_20)
                .border(width = 1.dp, color = theme_color_1, Shape_20)
                .background(color = theme_color_22, shape = Shape_20)
                .padding(15.dp)
        ) {

            Image(painter = painterResource(R.drawable.firetext), contentDescription = null)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Don't break your streak!",
                    color = theme_color_11,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
                Text(
                    "Miss a day and your streak will reset from Day 1.",
                    color = text_grey_3,
                    fontSize = 12.sp
                )
            }
        }

    }
}

