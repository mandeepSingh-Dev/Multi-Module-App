package com.app.feature_home_host.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.localeUtils.toViewedAgo
import com.app.core_navigation.AppScreens
import com.app.core_navigation.HostScreens
import com.app.core_ui.components.OutlineButton2
import com.app.core_ui.components.PremiumAnimatedTabBar
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.TabItem
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.emptyView
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_10
import com.app.core_ui.ui.theme.theme_color_12
import com.app.feature_home_host.remote.model.response.ProfileVisitorsListResponse
import com.app.feature_home_host.ui.enums.VisitorRangeType
import com.app.feature_home_host.ui.states.ProfileVisitorsUIState
import com.app.feature_home_host.ui.viewmodels.ProfileVisitorsViewModel
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.launch

@Composable
@Preview
fun ProfileVisitorVisitorsScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    profileVisitorsViewModel: ProfileVisitorsViewModel = hiltViewModel()
) {


    val uiState by profileVisitorsViewModel.uiState.collectAsStateWithLifecycle()

    val tabs = remember {
        VisitorRangeType.entries.map {
            if (it == VisitorRangeType.ALL) {
                TabItem("All Visitors", 0)

            } else {
                TabItem("Recent 7 Days", 0)
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val pagingState = rememberPagerState { tabs.size }

    LaunchedEffect(pagingState) {
        Log.d("fkbnkfnb", pagingState.currentPage.toString())
    }


        val allVisitorslListPaging  = profileVisitorsViewModel.allVisitorsListPaging.collectAsLazyPagingItems()
        val sevenDaysVisitorslListPaging  = profileVisitorsViewModel.sevenDaysVisitorsListPaging.collectAsLazyPagingItems()



    ObserveUIEvents(profileVisitorsViewModel.uiEvent) {
        when(it){
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(it.error)
            }
            is CommonUIEvents.NavigateTo -> {
                Log.d("fbnkfbnfnkb", it.navKey.toString())
                backStack.add(it.navKey)
            }
        }
    }


    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {

            HeaderView2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                title = "Profile Visitors",
                secTitle = "The users viewed your profile",
                showNotificationIcon = false,
                onBack = { backStack.removeLastOrNull() },
                 onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                } )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                PremiumAnimatedTabBar(
                    modifier = Modifier.padding(top = 20.dp),
                    tabs = tabs,
                    selectedTabIndex = pagingState.currentPage
                ) {
                    coroutineScope.launch {
                        Log.d("fklbmkfnbf", it.toString())
                        pagingState.animateScrollToPage(it)
                    }
                }


                HorizontalPager(
                    pagingState,
                    modifier = Modifier
                ) { currentPage ->

                    if (currentPage == VisitorRangeType.ALL.ordinal) {
                        VisitorsList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            range = VisitorRangeType.ALL.value,
                            uiState = uiState,
                            profileVisitorsViewModel = profileVisitorsViewModel,
                            allVisitorslListPaging,
                            onViewProfileClick = {visitorId ->
                                profileVisitorsViewModel.navigationEvent(HostScreens.VisitorDetailsScreen(visitorId = visitorId))
                            }
                        )
                    } else {
                        VisitorsList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            range = VisitorRangeType.SEVEN_DAYS.value,
                            uiState = uiState,
                            profileVisitorsViewModel = profileVisitorsViewModel,
                            sevenDaysVisitorslListPaging,
                            onViewProfileClick = {

                            }
                        )
                    }
                }

            }
        }

    }
}

@Composable
fun VisitorsList(
    modifier: Modifier,
    range: String,
    uiState: ProfileVisitorsUIState?,
    profileVisitorsViewModel: ProfileVisitorsViewModel,
    listPaging: LazyPagingItems<ProfileVisitorsListResponse.Data.ProfileVisitorData>,
    onViewProfileClick: (visitorId : String?) -> Unit
) {

    val isFirstTimeApiHit by profileVisitorsViewModel.isFirstTimeApiHit.collectAsStateWithLifecycle()



    LaunchedEffect(Unit) {
        Log.d("kbnfjbnf",listPaging.itemCount.toString() + " $range")
    }

    PullToRefreshBox(
        isRefreshing = listPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == true,
        onRefresh = {
            profileVisitorsViewModel.updateState { state ->
                state.copy(isRefreshing = true)
            }
            listPaging.refresh()
        }, modifier = modifier
    ) {

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .animateItem()
                ) {
                    AnimatedVisibility(
                        listPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false && listPaging.itemCount == 0
                    ) {
                        CircularProgressIndicator(
                            color = theme_color_1, modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            items(listPaging.itemCount) { index ->
                val visitorData = listPaging.get(index)
                ProfileVisitorListItem(visitorData, onViewProfileClick = {onViewProfileClick(visitorData?.viewer?.id)})
            }
            item {
                bottomPagingLoader(
                    isVisible = listPaging.loadState.append is LoadState.Loading || listPaging.loadState.append is LoadState.Error,
                    isRetry = listPaging.loadState.append is LoadState.Error,
                    onRetry = {
                        listPaging.retry()
                    })
            }
            item {
                AnimatedVisibility(listPaging.itemCount == 0) {
                    if ((listPaging.loadState.refresh is LoadState.NotLoading || listPaging.loadState.refresh is LoadState.Error) && isFirstTimeApiHit) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            emptyView(
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }

}


// Color Palette for Gold/Premium Aesthetic
val GoldGradientStart = Color(0xFFE5B04A)
val GoldGradientCenter = Color(0xFFF9E088)
val GoldGradientEnd = Color(0xFFC48B28)
val UnselectedTabTextColor = Color(0xFF6B7280)
val SelectedTabTextColor = Color(0xFFB47B1C)

val PremiumGoldBrush = Brush.horizontalGradient(
    colors = listOf(
        GoldGradientStart,
        GoldGradientCenter,
        GoldGradientEnd
    )
)


@Composable
fun ProfileVisitorListItem(
    data: ProfileVisitorsListResponse.Data.ProfileVisitorData?,
    onViewProfileClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20)
            .background(Color.White, Shape_20)
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image with Online Indicator Badge
            Box(
                modifier = Modifier.size(50.dp)
            ) {
                ProfileImage(
                    image = data?.viewer?.photo,
                    modifier = Modifier
                        .size(50.dp)
                )

                if (false) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd)
                            .padding(0.dp)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color(0xFF10B981), // Green indicator dot
                            shape = CircleShape
                        ) {}
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Info Column
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data?.viewer?.name ?: "",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                val age = if(data?.viewer?.age!=null) data.viewer.age.toString() else ""
                Text(
                    text = "$age • ${(data?.viewer?.city ?: "").replaceFirstChar { it.uppercase() }} ${data?.viewer?.country ?: ""}",
                    fontSize = 13.sp,
                    color = text_grey_1
                )

                Spacer(modifier = Modifier.height(2.dp))


                Text(
                    text = data?.lastViewedAt.toViewedAgo(),
                    fontSize = 12.sp,
                    color = text_grey_2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // View Profile Button
            OutlineButton2(
                onClick = onViewProfileClick,
                text = "View Profile",
                fontColor = theme_color_12,
                isBorder = true,
                borderBrush = brush2(),
                bgColor = theme_color_10,
                fontSize = 13.sp,
                verticalPadding = 7.dp,
                horizontalPadding = 10.dp,
                shape = CircleShape
            )
        }
    }
}