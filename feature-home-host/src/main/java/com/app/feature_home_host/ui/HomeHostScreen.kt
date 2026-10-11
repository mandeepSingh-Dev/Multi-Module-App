package com.app.feature_home_host.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_navigation.HostScreens
import com.app.core_navigation.StoriesScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.RatingBar
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.clickableDeb
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.components.shimmerEffect
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush5
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.green_14
import com.app.core_ui.ui.theme.grey_12
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_4
import com.app.feature_home_host.remote.DashboardStatsApiResponse
import com.app.feature_home_host.ui.viewmodels.HomeHostViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


@Composable
fun HomeHostScreen(
    bottomTabBarHeight: Dp,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    homeHostViewModel: HomeHostViewModel = hiltViewModel(),
    onEarningScreen: () -> Unit
)

{
    val uiState by homeHostViewModel.uiState.collectAsStateWithLifecycle()


    AttachLifecycleObserver { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                homeHostViewModel.getDashboardStats()
                homeHostViewModel.connectToSocket()
            }

            else -> Unit
        }
    }


    ObserveUIEvents(homeHostViewModel.uiEvent) { uiEvent ->
        when (uiEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uiEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uiEvent.navKey)
            }

            is CommonUIEvents.ShowMessage -> {
                snackBarHostState.showSnackBarWithDismiss(message = uiEvent.message)
            }
        }
    }

    BoxCommon(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize())
        {


            PullToRefreshBox(
                isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    homeHostViewModel.getDashboardStats(isRefreshing = true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    HostStatusCard(
                        modifier = Modifier.padding(top = 20.dp, horizontal = 20.dp),
                        isOnline = uiState?.isOnline ?: false, onChange = {
                            homeHostViewModel.updateOnline(it)
                        })
                  /*  if (uiState?.isLoading == true && uiState?.dashboardData == null) {
                        TodayEarningCardShimmer(
                            modifier = Modifier.padding(
                                top = 20.dp,
                                horizontal = 20.dp
                            )
                        )
                        HostDetailsCardsSectionShimmer(
                            modifier = Modifier.padding(
                                top = 20.dp,
                                horizontal = 20.dp
                            )
                        )


                    } else {*/
                        TodayEarningCard(
                            modifier = Modifier
                                .padding(top = 20.dp, horizontal = 20.dp)
                                .clickableDeb {
                                    onEarningScreen()
                                },
                            uiState?.isLoading,
                            uiState?.dashboardData
                        )

                        HostDetailsCardsSection(
                            modifier = Modifier.padding(
                                top = 20.dp,
                                horizontal = 20.dp
                            ),
                            isLoading = uiState?.isLoading,
                            dashboardData = uiState?.dashboardData,
                            onNavigate = {
                                homeHostViewModel.navigationEvent(it)
                            }
                        )
//                    }
                    KeepComingCard(
                        modifier = Modifier.padding(
                            top = 20.dp,
                            horizontal = 20.dp
                        )
                    )
                    Spacer(modifier = Modifier.height(bottomTabBarHeight.plus(50.dp)))

                }
            }
        }
    }
}

@Composable
fun HostScreenHeader(
    modifier: Modifier,
    name: String?,
    profilePic: String?,
    onNavigate: (NavKey) -> Unit,
    notificationCount: Int?
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
                .padding(1.dp)
        ) {
            ProfileImage(
                image = profilePic,
                modifier = Modifier
                    .fillMaxSize()
                    .clickableDebAnim {
                        onNavigate(
                            HostScreens.AuthHostScreens.SignUpHostScreen(
                                phone = null,
                                countryCode = null,
                                step = null,
                                isForUpdate = true
                            )
                        )
                    }
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        )
        {
            Text("Good Morning", color = grey_4, fontSize = 11.sp)

            Row(

                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(name ?: "", fontFamily = HAFFER_TRIAL_BOLD, fontSize = 16.sp)

                Image(
                    painter = painterResource(R.drawable.hi_emojihi),
                    contentDescription = null
                )

            }
        }
        Box(
            modifier = Modifier
                .size(35.dp)
                .clickableDebAnim {
                    onNavigate(AppScreens.NotificationListScreen)
                }
                .dropShadowCustom(shape = Shape_20, color = Color.Black.copy(0.1f))
                .clip(CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(7.dp)
        ) {

            BadgedBox(badge = {
                notificationCount?.let {count ->
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ) {
                        Text("$count", color = Color.White, fontSize = 9.sp)
                     }
                }
            }) {
                Image(
                    painter = painterResource(R.drawable.notificationcontainer),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }

        }
        Box(
            modifier = Modifier
                .size(35.dp)
                .dropShadowCustom(shape = Shape_20, color = Color.Black.copy(0.1f))
                .clip(CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(3.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.camera_icon),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickableDebAnim {
                        onNavigate(StoriesScreens.StoryPostScreen)
                    }
                    .align(Alignment.Center)
            )
        }

    }

}

@Composable
private fun HostStatusCard(
    modifier: Modifier,
    isOnline: Boolean = false,
    onChange: (Boolean) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(shape = Shape_20, color = Color.Black.copy(0.1f), radius = 10.dp)
            .clip(Shape_20)
            .background(color = Color.White, shape = Shape_20)
            .padding(15.dp)
            .animateContentSize()
    ) {

        Text("Status", fontSize = 10.sp, color = text_grey_1)


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedContent(isOnline) { isOnline ->
                if (isOnline) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color = green_14, shape = CircleShape)
                        )
                        Text(
                            "Online",
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                            color = green_14,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    Text(
                        "Offline",
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                        color = text_grey_1,
                        fontSize = 15.sp,
                        modifier = Modifier
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isOnline, onCheckedChange = null,
                colors = SwitchDefaults.colors().copy(
                    checkedTrackColor = theme_color_1,
                    uncheckedTrackColor = grey_12,
                    uncheckedThumbColor = grey_4
                ),
                modifier = Modifier
                    .clip(Shape_20)
                    .toggleable(value = isOnline, onValueChange = {
                        onChange(!isOnline)
                    })
            )
        }

        HorizontalDivider(
            thickness = 1.dp, color = divider_color_1,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                if (isOnline) "You are visible to all users." else "You are not visible to other users.",
                color = text_grey_1,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )


            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Online",
                    fontFamily = if (isOnline) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_REGULAR,
                    fontSize = 13.sp,
                    color = if (isOnline) theme_color_11 else text_grey_1
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                ) {
                    AnimatedVisibility(isOnline) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = if (isOnline) theme_color_11 else text_grey_1,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Offline",
                    fontFamily = if (!isOnline) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_REGULAR,
                    fontSize = 13.sp,
                    color = if (!isOnline) theme_color_11 else text_grey_1
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                ) {
                    AnimatedVisibility(!isOnline) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = theme_color_11, shape = CircleShape)
                        )
                    }
                }
            }

        }
    }
}


@Composable
private fun TodayEarningCardShimmer(modifier: Modifier) {
    ShimmerBox(
        shape = Shape_20,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(brush = brush5(), shape = Shape_20)
            .shimmerEffect(Shape_20)
    )
}

@Composable
private fun TodayEarningCard(
    modifier: Modifier,
    isLoading: Boolean?,
    dashboardData: DashboardStatsApiResponse.DashboardStatsData?
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20, color = Color.Black.copy(0.05f))
            .border(width = 1.dp, color = theme_color_4.copy(alpha = 0.5f), shape = Shape_20)
            .background(brush = brush5(), shape = Shape_20)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            Image(
                painterResource(R.drawable.half_circletoday_earn),
                contentDescription = null,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text("TODAY'S EARNINGS", letterSpacing = 1.05.sp, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if(isLoading == true && dashboardData == null){
                        ShimmerBox(modifier = Modifier.padding(horizontal = 3.dp).width(20.dp).height(10.dp))
                    }else {
                        Text(
                            "${dashboardData?.todayEarning ?: 0}",
                            fontSize = 20.sp,
                            fontFamily = HAFFER_TRIAL_BOLD
                        )
                    }
                    Text("Beans", fontSize = 13.sp, fontFamily = HAFFER_TRIAL_BOLD)
                }

            }

            Image(painterResource(R.drawable.half_circlecircle_arrow), contentDescription = null)
        }

        Image(
            painterResource(R.drawable.half_circleoverlay), contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 20.dp)
        )

    }
}

@Composable
private fun HostDetailsCardsSectionShimmer(modifier: Modifier) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerBox(
                shape = Shape_15,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
            ShimmerBox(
                shape = Shape_15,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
        }
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerBox(
                shape = Shape_15,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
            ShimmerBox(
                shape = Shape_15,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
            )
        }
    }


}


@Composable
private fun HostDetailsCardsSection(
    modifier: Modifier,
    isLoading: Boolean?,
    dashboardData: DashboardStatsApiResponse.DashboardStatsData?,
    onNavigate: (NavKey) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TalkTimeCard(
                isLoading,
                dashboardData?.talkTime,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            RatingCard(
                isLoading,
                dashboardData?.rating,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickableDebAnim {
                        onNavigate(AppScreens.ReviewsListScreen)
                    }
            )
        }
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CallsCard(
                isLoading,
                dashboardData?.calls,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            ProfileViewsCard(
                isLoading,
                dashboardData?.profileViews,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickableDebAnim {
                        onNavigate(HostScreens.HostProfileVisitorsScreen)
                    }
            )
        }
    }
}

@Composable
private fun TalkTimeCard(
    isLoading: Boolean?,
    talkTime: DashboardStatsApiResponse.DashboardStatsData.TalkTime?,
    modifier: Modifier
) {


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .dropShadowCustom(Shape_15, Color.Black.copy(0.05f))
            .clip(Shape_15)
            .border(width = 1.dp, color = grey_9, shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(10.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.talk_timeoverlay), contentDescription = null)
            Text("Talk Time", fontSize = 12.sp, fontFamily = HAFFER_TRIAL_MEDIUM)
        }
        if(isLoading == true && talkTime == null){
            ShimmerBox(modifier = Modifier.width(60.dp).height(10.dp))
        }else {
            Text(talkTime?.formatted ?: "", fontSize = 18.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }
        Image(
            painterResource(R.drawable.talktime_graphsubtleaudiowaveformgraphic),
            contentDescription = null,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

}

@Composable
private fun RatingCard(
    isLoading: Boolean?,
    rating: DashboardStatsApiResponse.DashboardStatsData.Rating?,
    modifier: Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .dropShadowCustom(Shape_15, Color.Black.copy(0.05f))
            .clip(Shape_15)
            .border(width = 1.dp, color = grey_9, shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(10.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.ratingoverlay), contentDescription = null)
            Text("Rating", fontSize = 12.sp, fontFamily = HAFFER_TRIAL_MEDIUM)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isLoading == true && rating == null){
                ShimmerBox(modifier = Modifier.width(20.dp).height(10.dp))
            }else {
                Text(
                    "${(rating?.totalRatings ?: 0)}",
                    fontSize = 18.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
            Image(
                painterResource(R.drawable.star_2vector), contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }

        RatingBar(
            rating = (rating?.average ?: 0).toInt(),
            onRatingChanged = {
//                rating = it
            },
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            starSize = 15.dp,
            modifier = Modifier.padding(top = 20.dp)
        )

    }

}


@Composable
private fun CallsCard(
    isLoading: Boolean?,
    callData: DashboardStatsApiResponse.DashboardStatsData.Calls?,
    modifier: Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .dropShadowCustom(Shape_15, Color.Black.copy(0.05f))
            .clip(Shape_15)
            .border(width = 1.dp, color = grey_9, shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(top = 10.dp, bottom = 5.dp, horizontal = 10.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.callsoverlay), contentDescription = null)
            Text("Calls", fontSize = 12.sp, fontFamily = HAFFER_TRIAL_MEDIUM)
        }

        if(isLoading == true && callData == null){
            ShimmerBox(modifier = Modifier.width(50.dp).height(10.dp))
        }else {
            Text(
                (callData?.total ?: 0).toString(),
                fontSize = 18.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Total Calls", fontSize = 10.sp, color = text_grey_5)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.call_graphjminiascendingbarchartgraphic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(30.dp)
            )
        }
    }

}


@Composable
private fun ProfileViewsCard(
    isLoading: Boolean?,
    profileStats: DashboardStatsApiResponse.DashboardStatsData.ProfileViews?,
    modifier: Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .dropShadowCustom(Shape_15, Color.Black.copy(0.05f))
            .clip(Shape_15)
            .border(width = 1.dp, color = grey_9, shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(top = 10.dp, bottom = 5.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Image(painterResource(R.drawable.profile_viewsoverlay), contentDescription = null)
            Text("Profile Views", fontSize = 12.sp, fontFamily = HAFFER_TRIAL_MEDIUM)
        }

        if(isLoading == true && profileStats == null){
            ShimmerBox(modifier = Modifier.padding(horizontal = 10.dp).width(30.dp).height(10.dp))
        }else {
            Text(
                (profileStats?.total ?: 0).toString(),
                fontSize = 18.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Today", fontSize = 10.sp, color = text_grey_5,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.eye_yellosvg),
                contentDescription = null,
                alpha = 0.2f,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(30.dp)
            )
        }
    }

}

@Composable
private fun KeepComingCard(modifier: Modifier) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20, color = Color.Black.copy(0.05f))
            .border(width = 1.dp, color = theme_color_4.copy(alpha = 0.5f), shape = Shape_20)
            .background(brush = brush5(), shape = Shape_20)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            Image(
                painterResource(R.drawable._3dgiftboxwithgoldencoinsconnecting_gift_iconbeansrepresentation),
                contentDescription = null,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Keep connecting\n& earn more!",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 16.sp
                )
                Text("Your conversations matter.", color = text_grey_1, fontSize = 12.sp)
            }
        }
    }

}