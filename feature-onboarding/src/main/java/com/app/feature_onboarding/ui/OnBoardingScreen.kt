package com.app.feature_onboarding.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_navigation.AppScreens
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.OutlineButton
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brushGrey
import com.app.core_ui.ui.theme.brushWhite
import kotlinx.coroutines.launch


@Composable
@Preview
fun OnBoardingScreen(backStack: NavBackStack<NavKey> = LocalBackStackNav.current) {

    val coroutineScope = rememberCoroutineScope()

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(pageCount = { 3 })

    val text1 by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> "Discover Amazing"
                1 -> "Chat Freely."
                else -> "Send Gifts"
            }
        }
    }
    val description by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> "Explore verified female creators, view profiles, stories & live status, and find your perfect  match instantly."
                1 -> "Start with unlimited free text chat.\u2028 When you’re ready, enjoy crystal-clear\u2028 audio or HD video calls."
                else -> "Express your feelings with virtual gifts,\u2028 collect rewards, and unlock exclusive\u2028 VIP benefits."
            }
        }
    }

    val textImage by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> R.drawable.host_imghosts
                1 -> R.drawable.callinstantly_imgcallinstantly
                else -> R.drawable.makemomently_imgmakemomentsspecial
            }
        }
    }

    BoxCommon(isAppearanceLightNavigationBars = false) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text1,
                fontFamily = HAFFER_TRIAL_BOLD,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp).padding(top = 20.dp)
            )
            AnimatedContent(pagerState.currentPage, modifier = Modifier  .padding(top = 10.dp)
                .padding(horizontal = 20.dp)) { index ->
            when(index){
                0 -> Image(
                    painter = painterResource(R.drawable.host_imghosts),
                    contentDescription = null,
                    modifier = Modifier

                        .wrapContentHeight()
                )
                1 ->Image(
                    painter = painterResource(R.drawable.callinstantly_imgcallinstantly),
                    contentDescription = null,
                    modifier = Modifier

                )
                else -> Image(
                    painter = painterResource(R.drawable.makemomently_imgmakemomentsspecial),
                    contentDescription = null,
                    modifier = Modifier

                        .wrapContentHeight()
                )
            }
            }

            Text(
                description,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-30).dp),
                textAlign = TextAlign.Center
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { index ->

                val image = when (index) {
                    0 -> R.drawable.host_main_imgimage12
                    1 -> R.drawable.callinstant_main_imgimage13
                    else -> R.drawable.makemoment_main_imgimage14
                }
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

            }

            LoadingIndicators(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                currentIndex = pagerState.currentPage,
                counts = 3,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 10.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AnimatedVisibility(pagerState.currentPage < pagerState.pageCount-1) {
                    OutlineButton(
                        onClick = {
                            backStack.removeAll(backStack)
                            backStack.add(AppScreens.UserTypeSelectionScreen)
                        },
                        text = "Skip",
                        isBorder = true,
                        horizontalPadding = 20.dp,
                        shape = CircleShape,
                    )
                }
                GradientButton(
                    onClick = {
                        if(pagerState.currentPage < pagerState.pageCount-1){
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage.plus(1))
                            }
                        }else{
                            backStack.removeAll(backStack)
                            backStack.add(AppScreens.UserTypeSelectionScreen)
                        }
                    },
                    text = if(pagerState.currentPage == pagerState.pageCount-1) "Get Started" else "Continue",
                    endIcon = R.drawable.group_2,
                    modifier = Modifier.weight(1f),
                )
            }

        }
    }

}

@Composable
fun LoadingIndicators(
    modifier: Modifier = Modifier,
    currentIndex: Int = 0,
    counts: Int = 3,
    onClick: (Int) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        repeat(counts) { index ->
            val width by animateDpAsState(if (index == currentIndex) 37.dp else 10.dp)
            Box(
                modifier = Modifier
                    .width(width)
                    .height(7.dp)
                    .clip(CircleShape)
                    .background(brush = if (index == currentIndex) brush2() else brushGrey())
                    .clickable {
                        onClick(index)
                    }
            )
        }
    }
}