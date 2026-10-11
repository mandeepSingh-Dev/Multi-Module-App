package com.app.feature_home.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.SwipeMode.Companion.spring
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.CallType
import com.app.core_common.utils.enums.MenuOptions
import com.app.core_navigation.AppScreens
import com.app.core_navigation.NavigationProvider
import com.app.core_navigation.StoriesScreens
import com.app.core_navigation.UserScreens
import com.app.core_story.remote.model.response.StoriesListResponse
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.FilledCustomButton
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.clickableDeb
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.components.strokeGapModifier
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.brushBottomShadowStyle
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.green_2
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.feature_home.remote.model.response.HomeDataUserResponse
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.events.HomeUserScreenEvents
import com.app.feature_home.ui.viewmodels.HomeUserViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_20
import com.zodiaq.ui.theme.Shape_8


@Composable
fun HomeUserScreen(
    bottomTabBarHeight: Dp,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    homeUserViewModel: HomeUserViewModel = hiltViewModel(),
    onOnlineProfilesSeeAll: () -> Unit,
    onRecommendedProfilesSeeAll: () -> Unit
) {

    val uiState by homeUserViewModel.uiState.collectAsStateWithLifecycle()


    var showBlockHostDialog by rememberSaveable { mutableStateOf<String?>(null) }

    var onlineViewMenuOpen by remember {
        mutableStateOf<Pair<HostData?, Rect>?>(
            null
        )
    }

    Log.d("kfnbknfbf", "KVNVJDNV")

    AttachLifecycleObserver { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                homeUserViewModel.getHomeData()
                homeUserViewModel.getWallet()
                homeUserViewModel.getStoriesList()

                homeUserViewModel.connectToSocket()
            }

            else -> Unit
        }
    }



    val context = LocalContext.current

    ObserveUIEvents(homeUserViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

            is HomeUserScreenEvents.CallPartner -> {
                NavigationProvider.callNavigator.startCallActivity(
                    context,
                    chatID = uIEvent.chatID,
                    partnerID = uIEvent.partnerID,
                    personName = uIEvent.personName,
                    callType = uIEvent.callType,
                    profilePic = uIEvent.profilePic,

                    )
            }

            is HomeUserScreenEvents.BlockSuccessfully -> {
                showBlockHostDialog = null
            }
        }
    }

    BoxCommon(
        isLoading = false,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = Color.Transparent
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            PullToRefreshBox(
                isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    homeUserViewModel.getHomeData(isRefresh = true)
                    homeUserViewModel.getWallet(isRefreshing = true)
                    homeUserViewModel.getStoriesList(isRefresh = true)
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

                    Log.d("fbvjfbnjf", (uiState?.storiesList.isNullOrEmpty().toString()))
                    if (!uiState?.storiesList.isNullOrEmpty()) {
                        StoriesRow(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(top = 30.dp),
                            uiState?.isStoriesLoading ?: false,
                            uiState?.storiesList,
                            onClick = {
                                homeUserViewModel.onEvent(CommonUIEvents.NavigateTo(StoriesScreens.StoryViewScreen))
                            },
                            onStorySeeAll = {
                                homeUserViewModel.navigationEvent(StoriesScreens.StoryViewScreen)
                            }
                        )
                    }

                    OnlineNowView(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 30.dp),
                        isLoading = uiState?.isLoading ?: false,
                        onlineNowUsersList = uiState?.onlineNow,
                        onClickController = { item ->

                            homeUserViewModel.changeOnlineNowCallControllerVisibility(item)

                        }, onOpenMenu = { item, rect ->
                            homeUserViewModel.changeOnlineNowMenuVisibility(item)
                            onlineViewMenuOpen = Pair(item, rect)
                        },
                        onClick = { hostData ->
                            homeUserViewModel.navigationEvent(
                                UserScreens.HostDetailsScreen(
                                    id = hostData?.id,
                                    chatId = hostData?.chatId,
                                    fullName = hostData?.fullName,
                                    profilePic = hostData?.profilePic,
                                    rating = hostData?.rating,
                                    isVerified = hostData?.isVerified,
                                    isWishlist = hostData?.isWishlisted,
                                    gender = hostData?.gender
                                )
                            )
                        },
                        onCall = { data, callType ->

                            homeUserViewModel.onEvent(
                                HomeUserScreenEvents.CallPartner(
                                    chatID = data?.chatId,
                                    partnerID = data?.id,
                                    personName = data?.fullName,
                                    profilePic = data?.profilePic,
                                    callType = callType
                                )
                            )
                        }, onSeeAll = onOnlineProfilesSeeAll
                    )
                    RecommendedForYouView(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 30.dp),
                        uiState?.isLoading ?: false,
                        uiState?.recomended,
                        onRecommendedProfilesSeeAll = onRecommendedProfilesSeeAll,
                        onClick = { hostData ->
                            homeUserViewModel.navigationEvent(
                                UserScreens.HostDetailsScreen(
                                    id = hostData?.id,
                                    chatId = hostData?.chatId,
                                    fullName = hostData?.fullName,
                                    profilePic = hostData?.profilePic,
                                    rating = hostData?.rating,
                                    isVerified = hostData?.isVerified,
                                    isWishlist = hostData?.isWishlisted,
                                    gender = hostData?.gender
                                )
                            )
                        }

                    )
                    Spacer(modifier = Modifier.height(bottomTabBarHeight))
                }
            }
        }
    }


    if (onlineViewMenuOpen != null) {
        Dialog(onDismissRequest = {
            val tempItem = onlineViewMenuOpen
            onlineViewMenuOpen = null
            homeUserViewModel.changeOnlineNowMenuVisibility(tempItem?.first)
        }) {
            FloatingMenu(
                list = MenuOptions.entries.filter { it != MenuOptions.REPORT },
                getLabel = {
                    when (it) {
                        MenuOptions.BLOCK -> "Block"
                        MenuOptions.REPORT -> "Report"
                    }
                },
                onlineViewMenuOpen?.second,
                onClick = {
                    when (it) {
                        MenuOptions.BLOCK -> {
                            showBlockHostDialog = onlineViewMenuOpen?.first?.id
                            onlineViewMenuOpen = null
                        }

                        MenuOptions.REPORT -> {
                            homeUserViewModel.block(onlineViewMenuOpen?.first?.id)
                            onlineViewMenuOpen = null
                        }
                    }
                },
                onDismiss = {
                    val tempItem = onlineViewMenuOpen
                    onlineViewMenuOpen = null
                    homeUserViewModel.changeOnlineNowMenuVisibility(tempItem?.first)
                })
        }
    }

    ButtonsDialog(
        showDialog = !showBlockHostDialog.isNullOrEmpty(),
        showLoader = uiState?.isBlockLoading ?: false,
        title = "Block User?",
        description = "Are you sure you want to block this user? You will no longer be able to interact with each other.",
        icon = R.drawable.block_userblockedusers,
        onDismiss = {
            showBlockHostDialog = null
        },
        showBothButtons = true,
        positiveBtnText = "Block",
        negativeBtnText = "Cancel",
        onPositiveClick = {
            homeUserViewModel.block(showBlockHostDialog)

        },
        onNegativeClick = {
            showBlockHostDialog = null
        }
    )

}


@Composable
fun StoriesRow(
    modifier: Modifier,
    isLoading: Boolean,
    storiesList: List<StoriesListResponse.UserStoryGroup?>?,
    onClick: (StoriesListResponse.UserStoryGroup?) -> Unit,
    onStorySeeAll: () -> Unit
) {

    HeadingRow(
        modifier = modifier.fillMaxWidth(),
        heading = "Live / Recent Stories",
        buttonText = "See All",
    ) {
        onStorySeeAll()
    }


    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        if (isLoading) {
            items(10) {
                StoryCircleCardShimmer()
            }
        } else {
            items(storiesList ?: emptyList()) { storyData ->
                Log.d("fkbfknbf", storyData?.user?.profilePic.toString())
                storyCircleCard(storyData, onClick = {
                    onClick(storyData)
                })
            }
        }
    }
}


@Composable
fun StoryCircleCardShimmer() {

    val infinite = rememberInfiniteTransition()

    val alpha by infinite.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        ),
        label = "",
    )

    ShimmerBox(
        shape = CircleShape,
        modifier = Modifier
            .size(60.dp)
            .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
            .background(color = Color.LightGray, CircleShape)
            .padding(4.dp)

    )
}

@Composable
fun storyCircleCard(
    storyData: StoriesListResponse.UserStoryGroup?,
    onClick: () -> Unit
) {

    Box() {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .clickableDebAnim {
                    onClick()
                }
                .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
                .padding(4.dp)
        ) {

            Log.d("dkbnfkbnf", storyData?.user?.profilePic.toString())
            ProfileImage(
                image = storyData?.user?.profilePic,
                modifier = Modifier.fillMaxSize()
            )
        }
        /* if(storyData.)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(
                    x = (-4).dp,
                    y = (-4).dp
                )
                .size(12.dp)
                .clip(CircleShape)
                .background(color = Color.White, CircleShape)
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(color = green_4, CircleShape)
            )
        }*/
    }
}

@Composable
fun HeadingRow(
    modifier: Modifier,
    heading: String,
    buttonText: String,
    onClickBtn: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            heading, modifier = Modifier.weight(1f),
            fontFamily = HAFFER_TRIAL_MEDIUM
        )
        FilledCustomButton(
            onClick = onClickBtn,
            color = Color.Transparent,
            vertical = 0.dp,
            horizontal = 0.dp,
            text = buttonText,
            endIcon = R.drawable.baseline_arrow_forward_ios_24,
            endIconColor = theme_color_11,
            textColor = theme_color_11
        )
    }

}

@Composable
fun OnlineNowView(
    modifier: Modifier,
    isLoading: Boolean = false,
    onlineNowUsersList: List<HostData?>?,
    onClickController: (HostData?) -> Unit,
    onOpenMenu: (HostData?, Rect) -> Unit,
    onClick: (HostData?) -> Unit,
    onCall: (HostData?, callType: String) -> Unit,
    onSeeAll: () -> Unit
) {
    HeadingRow(
        modifier = modifier.fillMaxWidth(),
        heading = "Online Now",
        buttonText = "See All",
    ) {
        onSeeAll()
    }

    if (isLoading) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = 15.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .height(1.dp)
            )
            repeat(4) {
                OnlineViewShimmer()
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                        .height(1.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .width(10.dp)
                    .height(1.dp)
            )
        }
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            itemsIndexed(onlineNowUsersList ?: emptyList()) { index, item ->
                HostProfileCard(
                    hostData = item,
                    index = index,
                    onClickController = onClickController,
                    onOpenMenu = onOpenMenu,
                    onClick = onClick,
                    onCall = onCall
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedForYouView(
    modifier: Modifier,
    isloading: Boolean,
    recomended: List<HostData?>?,
    onClick: (HostData?) -> Unit,
    onRecommendedProfilesSeeAll: () -> Unit
) {
    HeadingRow(
        modifier = modifier.fillMaxWidth(),
        heading = "Recommended For You",
        buttonText = "See All",
    ) {
        onRecommendedProfilesSeeAll()
    }

    Spacer(modifier = Modifier.height(15.dp))

    AnimatedContent(isloading) { isLoading ->
        if (isLoading) {
            RecommendedCardShimmer(modifier = Modifier.padding(horizontal = 20.dp))
        } else {

            BoxWithConstraints() {
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { recomended?.size ?: 0 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    preferredItemWidth = maxWidth,
                    itemSpacing = 5.dp,
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) { i ->
                    val hostData = recomended?.get(i)
                    RecommendedCard(
                        recommendedProfile = hostData,
                        modifier = Modifier.padding(horizontal = 10.dp),
                        onClick = { onClick(hostData) }
                    )
                }
            }
        }
    }
}


@Composable
fun OnlineViewShimmer(
    modifier: Modifier = Modifier,
    isFullWidth: Boolean = false
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp
    ShimmerBox(
        shape = Shape_20,
        modifier = Modifier
            .then(
                if (isFullWidth) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.width((screenWidth * 0.5).dp)
                }
            )
            .height(280.dp)
            .clip(Shape_20)
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HostProfileCard(
    modifier: Modifier = Modifier,
    hostData: HostData? = null,
    index: Int = 0,
    isFullWidth: Boolean = false,
    onClick: (HostData?) -> Unit,
    onClickController: (HostData?) -> Unit,
    onOpenMenu: (HostData?, Rect) -> Unit,
    onCall: (HostData?, callType: String) -> Unit,
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp


    val sideBarHandlerHeight by animateDpAsState(
        if (hostData?.isCallControllerViewOpen ?: false) 50.dp else 35.dp
    )


    var rect by remember { mutableStateOf(Rect.Zero) }
    Box(
        modifier = modifier
            .then(
                if (isFullWidth) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.width((screenWidth * 0.5).dp)
                }
            )
            .height(280.dp)
            .clip(Shape_20)

    ) {

        AsyncImageWithLoader(
            image = hostData?.profilePic,
            modifier = Modifier
                .matchParentSize()
                .clickableDeb {
                    onClick(hostData)
                },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = brushBottomShadowStyle())
        )



        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    rect = it.boundsInRoot()
                }
                .align(Alignment.CenterEnd)) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .background(
                        color = Color.Black.copy(0.4f),
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    )
                    .clickable {
                        onClickController(hostData)
                    }
                    .padding(horizontal = 9.dp, vertical = 10.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp))
            {


                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(sideBarHandlerHeight)
                        .background(color = Color.White.copy(0.5f), CircleShape)
                )
                AnimatedVisibility(hostData?.isCallControllerViewOpen ?: false) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painterResource(R.drawable.call_iconlike), contentDescription = null,
                            modifier = Modifier.clickableDebAnim {
                                onCall(hostData, CallType.Audio.type)
                            })
                        Image(
                            painterResource(R.drawable.video_call_iconvideo_call_icon),
                            contentDescription = null, modifier = Modifier.clickableDebAnim {
                                onCall(hostData, CallType.Video.type)
                            }
                        )
                        Image(
                            painterResource(R.drawable.menu_circle_iconmore),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    onOpenMenu(hostData, rect)
                                }
                        )
                    }
                }
            }

            AnimatedVisibility(hostData?.isCallControllerViewOpen ?: false) {
                Box(
                    modifier = Modifier
                        .offset(x = (-6).dp, y = (-6).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .clickable{
                            onClickController(hostData)
                        }
                        .background(color = Color.White, CircleShape)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painterResource(R.drawable.baseline_close_24),
                        colorFilter = ColorFilter.tint(color = Color.Black),
                        contentDescription = null,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(10.dp)
        )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (hostData?.isOnline == 1) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .border(width = 1.dp, color = grey_4, Shape_8)
                            .background(color = Color.Black.copy(0.4f), Shape_8)
                            .padding(5.dp)
                    ) {
                        Text("Online", color = green_2, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (hostData?.rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .border(width = 1.dp, color = grey_4, Shape_8)
                            .background(color = Color.Black.copy(0.4f), Shape_8)
                            .padding(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.starcontainer),
                            contentDescription = null
                        )
                        Text(
                            hostData.rating.toString(), color = Color.White, fontSize = 12.sp,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    hostData?.fullName ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                hostData?.isVerified?.let {
                    Image(
                        painter = painterResource(R.drawable.verifies_iconsymbol),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.padding(top = 5.dp)
            ) {
                hostData?.address?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(1f, false)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.locationcontainer),
                            contentDescription = null
                        )
                        Text(
                            it, fontSize = 12.sp, color = grey_9,
                            lineHeight = 13.sp, maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                hostData?.age?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(1f, false)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.calendarcircum_calendar_date),
                            contentDescription = null
                        )
                        Text(
                            "Age: $it", fontSize = 12.sp, color = grey_9,
                            lineHeight = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
                }
            }

        }

    }

}

@Composable
fun RecommendedCardShimmer(modifier: Modifier = Modifier) {
    ShimmerBox(
        shape = Shape_20,
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(Shape_20)
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun RecommendedCard(
    modifier: Modifier = Modifier,
    recommendedProfile: HostData?,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)

            .clip(Shape_20)
            .clickableDeb {
                onClick()
            }

    ) {

        AsyncImageWithLoader(
            image = recommendedProfile?.profilePic,
            modifier = Modifier.matchParentSize(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = brushBottomShadowStyle())
        )

        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                if (recommendedProfile?.isOnline == 1) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .border(width = 1.dp, color = grey_4, Shape_8)
                            .background(color = Color.Black.copy(0.4f), Shape_8)
                            .padding(5.dp)
                    ) {
                        Text("Online", color = green_2, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (recommendedProfile?.rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .border(width = 1.dp, color = grey_4, Shape_8)
                            .background(color = Color.Black.copy(0.4f), Shape_8)
                            .padding(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.starcontainer),
                            contentDescription = null
                        )
                        Text(
                            recommendedProfile.rating.toString(),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    recommendedProfile?.fullName ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                recommendedProfile?.isVerified?.let {
                    Image(
                        painter = painterResource(R.drawable.verifies_iconsymbol),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.padding(top = 5.dp)
            ) {
                recommendedProfile?.address?.let {
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Image(
                            painter = painterResource(R.drawable.locationcontainer),
                            contentDescription = null
                        )
                        Text(
                            it, fontSize = 12.sp, color = grey_9, lineHeight = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                        )
                    }
                }

                recommendedProfile?.age?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.weight(1f, false)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.calendarcircum_calendar_date),
                            contentDescription = null
                        )
                        Text(
                            "Age: $it", fontSize = 12.sp, color = grey_9,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 13.sp, maxLines = 2
                        )
                    }
                }
            }

        }
    }

}

@Composable
fun <T> FloatingMenu(
    list: List<T>,
    getLabel: (T) -> String,
    rect: Rect?,
    onClick: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    var containerWidth by remember { mutableIntStateOf(0) }
    var containerHeight by remember { mutableIntStateOf(0) }

    var menuWidth by remember { mutableIntStateOf(0) }
    var menuHeight by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onDismiss()
            }
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                containerWidth = coordinates.size.width
                containerHeight = coordinates.size.height
            }
    ) {

        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .onGloballyPositioned { coordinates ->
                    menuWidth = coordinates.size.width
                    menuHeight = coordinates.size.height
                }
                .offset {
                    val desiredX = rect?.left?.toInt() ?: 0
                    val desiredY = (rect?.bottom?.toInt() ?: 0) - 141

                    val maxX = containerWidth - menuWidth
                    val maxY = containerHeight - menuHeight

                    IntOffset(
                        x = desiredX.coerceIn(
                            0,
                            maxX.coerceAtLeast(0)
                        ),
                        y = desiredY.coerceIn(
                            0,
                            maxY.coerceAtLeast(0)
                        )
                    )
                }
                .dropShadowCustom(
                    shape = Shape_10,
                    color = Color.Black.copy(alpha = 0.1f)
                )
                .clip(Shape_10)
                .background(
                    color = Color.White,
                    shape = Shape_10
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            list.forEachIndexed { index, item ->

                Text(
                    text = getLabel(item),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableDebAnim {
                            onClick(item)
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )

                if (index != list.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(
                                color = divider_color_1,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
