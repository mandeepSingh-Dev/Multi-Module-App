package com.app.feature_call.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.app.core_common.extensions.showToast
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.CallType
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncProfileImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.EmptyViewType
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.emptyView
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_LIGHT
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.grey_12
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_14
import com.app.feature_call.remote.model.response.CallHistoryResponse
import com.app.feature_call.ui.states.CallHistoryUIState
import com.app.feature_call.ui.viewmodels.CallHistoryViewModel
import com.zodiaq.ui.theme.Shape_15


@Composable
fun CallHistoryScreen(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    callHistoryViewModel: CallHistoryViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val uiState by callHistoryViewModel.uiState.collectAsStateWithLifecycle()

    val allCallsPaging = callHistoryViewModel.allCallHistory.collectAsLazyPagingItems()
    val videoCallsPaging = callHistoryViewModel.videoCallHistory.collectAsLazyPagingItems()
    val audioCallPaging = callHistoryViewModel.audioCallHistory.collectAsLazyPagingItems()

    val firstTimeApiHit by callHistoryViewModel.isFirstTimeApiHit.collectAsStateWithLifecycle()

    var selectedIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(allCallsPaging.loadState.refresh) {
        if (allCallsPaging.loadState.refresh is LoadState.NotLoading) {
            callHistoryViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (allCallsPaging.loadState.refresh is LoadState.Error) {
            (allCallsPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                callHistoryViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (allCallsPaging.loadState.hasError) {
            (allCallsPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }

    LaunchedEffect(videoCallsPaging.loadState.refresh) {
        if (videoCallsPaging.loadState.refresh is LoadState.NotLoading) {
            callHistoryViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (videoCallsPaging.loadState.refresh is LoadState.Error) {
            (videoCallsPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                callHistoryViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (videoCallsPaging.loadState.hasError) {
            (videoCallsPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }

    LaunchedEffect(audioCallPaging.loadState.refresh) {
        if (audioCallPaging.loadState.refresh is LoadState.NotLoading) {
            callHistoryViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (audioCallPaging.loadState.refresh is LoadState.Error) {
            (audioCallPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                callHistoryViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (audioCallPaging.loadState.hasError) {
            (audioCallPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }
    ObserveUIEvents(callHistoryViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackbarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

        }
    }


    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderView2(
                title = "Call History",
                secTitle = "View your complete audio and video call history.",
                modifier = Modifier.padding(horizontal = 20.dp)
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }   )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                SegmentedTabs(
                    tabs = listOf("All", "Audio", "Video"),
                    getTabText = {
                        it
                    },
                    selectedIndex = selectedIndex,
                    onSelect = {
                        selectedIndex = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )


                AnimatedContent(selectedIndex,
                    modifier = Modifier.fillMaxWidth().weight(1f)) { selectedIndex ->
                    when(selectedIndex){
                        0 -> {
                            lazyCallLists(
                                allCallsPaging, firstTimeApiHit,
                                uiState = uiState,
                                callHistoryViewModel = callHistoryViewModel
                            )
                        }
                        1 -> {
                            lazyCallLists(audioCallPaging, firstTimeApiHit,
                                uiState = uiState,
                                callHistoryViewModel = callHistoryViewModel)
                        }

                        else -> {
                            lazyCallLists(videoCallsPaging, firstTimeApiHit,
                                uiState = uiState,
                                callHistoryViewModel = callHistoryViewModel)
                        }

                    }
                }
            }

        }
    }


}

@Composable
fun lazyCallLists(
    callsPaging: LazyPagingItems<CallHistoryResponse.CallHistoryData>,
    firstTimeApiHit: Boolean,
    uiState: CallHistoryUIState?,
    callHistoryViewModel: CallHistoryViewModel
) {

    PullToRefreshBox(
        isRefreshing = (uiState?.isRefreshing ?: false),
        onRefresh = {
            callHistoryViewModel.updateState {
                it.copy(isRefreshing = true)
            }
            callsPaging.refresh()
        },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier.fillMaxSize()
        )
        {
            if(callsPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false){
               items(5){
                   ShimmerBox(modifier = Modifier.fillMaxWidth().height(100.dp))
               }
            }else {
                items(callsPaging.itemCount) { index ->
                    val item = callsPaging.get(index)
                    CallHistoryItem(
                        item,
                        modifier = Modifier
                    )
                }
                item {
                    bottomPagingLoader(

                        isVisible = callsPaging.loadState.append is LoadState.Loading || callsPaging.loadState.append is LoadState.Error,
                        isRetry = callsPaging.loadState.append is LoadState.Error,
                        onRetry = {
                            callsPaging.retry()
                        })
                }
                item {

                    AnimatedVisibility(callsPaging.itemCount == 0) {

                        Log.d("fkbnkfnb", ((callsPaging.loadState.refresh is LoadState.NotLoading || callsPaging.loadState.refresh is LoadState.Error) && firstTimeApiHit).toString()+ " == 0")
                        Log.d("fkbnkfnb", "$firstTimeApiHit == 1")
                        Log.d("fkbnkfnb", (callsPaging.loadState.refresh is LoadState.Error).toString()+ " == 2")
                        Log.d("fkbnkfnb", ((callsPaging.loadState.refresh is LoadState.NotLoading ).toString())+ " == 3")


                            if ((callsPaging.loadState.refresh is LoadState.NotLoading || callsPaging.loadState.refresh is LoadState.Error) && firstTimeApiHit) {
                           Log.d("fkbkfnb","1")
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                emptyView(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    emptyViewType = EmptyViewType.CALL_HISTORY
                                )
                            }
                        }else{
                            Log.d("fkbkfnb","2")

                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(60.dp))

            }
        }
    }
}

@Composable
fun tabs() {

    var index by remember { mutableIntStateOf(0) }

    SecondaryScrollableTabRow(
        containerColor = Color.Blue,
        edgePadding = 1.dp,
        divider = {},
        selectedTabIndex = index,
        modifier = Modifier.fillMaxWidth(),
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(
                        selectedTabIndex = index,
                        matchContentSize = false
                    )
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = theme_color_1,
                        shape = CircleShape
                    )
            )
        }
    ) {
        repeat(3) { tabIndex ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .clickable { index = tabIndex }
                    .background(color = Color.Red)
                    .padding(15.dp)
            ) {
                Text(
                    text = "$tabIndex",
                    color = if (index == tabIndex) theme_color_11 else Color.Black,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    modifier = Modifier
                )
            }
        }
    }
}


@Composable
fun <T> SegmentedTabs(
    tabs: List<T>,
    getTabText: (T) -> String,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var tabWidthDp by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    val coordinates = remember {
        mutableStateListOf<Float>()
    }

    LaunchedEffect(coordinates) {
        Log.d("knbkfnbf", coordinates.toString())
    }

    val indicatorOffset by animateDpAsState(
        targetValue = with(density) { (coordinates.getOrNull(selectedIndex) ?: 0f).toDp() },
        label = "indicatorOffset"
    )

    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(color = grey_12)
            .padding(4.dp)
    ) {
        // Sliding indicator
        Log.d("fknbkfnbf", tabWidthDp.toString())
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(with(density) { tabWidthDp.toDp() })
                .fillMaxHeight()
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(radius = 20.dp, color = Color.Black.copy(alpha = 0.1f))
                )
                .clip(CircleShape)
                .border(width = 1.dp, color = theme_color_11, shape = CircleShape)
                .background(Color.White, shape = CircleShape) // your tab "chip" color
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { tabIndex, item ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .clickable {
                            onSelect(tabIndex)
                        }
                        .padding(vertical = 6.dp)
                        .onGloballyPositioned {
                            tabWidthDp = it.size.width
                            coordinates.add(tabIndex, it.boundsInParent().left)
                        }
                ) {
                    Text(
                        text = getTabText(item),
                        color = if (selectedIndex == tabIndex) theme_color_11 else text_grey_3,
                        fontFamily = HAFFER_TRIAL_MEDIUM,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun CallHistoryItem(item: CallHistoryResponse.CallHistoryData?, modifier: Modifier) {

    var isSelected by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(if (isSelected) theme_color_14 else Color.White)
    val radius by animateDpAsState(if (isSelected) 10.dp else 20.dp)
    val alpha by animateFloatAsState(if (isSelected) 0.2f else 0.05f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .dropShadow(Shape_15, shadow = Shadow(color = Color.Black.copy(alpha), radius = radius))
            .clip(Shape_15)

            .background(color = bgColor, shape = Shape_15)
    ) {

        item?.duration?.let { duration ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        brush = brush2(),
                        shape = RoundedCornerShape(bottomEnd = 12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.clock_iconcontainer),
                        contentDescription = null
                    )
                    Text(
                        duration, fontSize = 11.sp, color = Color.White,
                        letterSpacing = 1.2.sp,
                        lineHeight = 10.sp,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 10.dp, bottom = 12.dp)
        ) {
            AsyncProfileImageWithLoader(
                item?.receiverId?.profilePic,
                modifier = Modifier.size(40.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(item?.receiverId?.fullName ?: "", fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 15.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(
                        painterResource(R.drawable.cal_icon),
                        contentDescription = null
                    )

                    val time = item?.startedAt?.convertToDateFormat(toFormat = DateFormats.
                    DATE_TIME_FORMAT)
                    time?.let {time ->
                        Text(
                            time, fontSize = 12.sp, color = text_grey_3,
                            fontFamily = HAFFER_TRIAL_SEMI_LIGHT,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
            VerticalDivider(
                thickness = 1.dp,
                color = divider_color_1,
                modifier = Modifier.fillMaxHeight()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Coin Spent",
                    fontSize = 11.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.coins_stack_iconimage52),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        (item?.coinsSpent ?: 0).toString(), fontSize = 16.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
                }
                Text(
                    "Coins", fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = text_grey_3, fontFamily = HAFFER_TRIAL_MEDIUM
                )

            }
            Spacer(modifier = Modifier.width(6.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    "Call Type",
                    fontSize = 11.sp
                )
                val icon = if(item?.callType == CallType.Audio.type) R.drawable.phone_iconbackground else R.drawable.video_call_iconvideo_call_icon
                Image(
                    painter = painterResource(icon),
                    contentDescription = null
                )
                Text(
                    if(item?.callType == CallType.Audio.type) "Audio" else "Video", fontSize = 12.sp,
                    color = text_grey_3, fontFamily = HAFFER_TRIAL_MEDIUM
                )

            }
        }
    }
}
