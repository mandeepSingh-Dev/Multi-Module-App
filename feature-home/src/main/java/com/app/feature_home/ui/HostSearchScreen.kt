package com.app.feature_home.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.MenuOptions
import com.app.core_navigation.NavigationProvider
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.StoryScreenSearchBar
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.emptyView
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP
import com.app.core_ui.ui.theme.grey_12
import com.app.core_ui.ui.theme.red_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_12
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.events.HomeSearchScreenEvents
import com.app.feature_home.ui.viewmodels.HostSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun HostSearchScreen(
    showOnlineProfile : Boolean = false,
    bottomTabBarHeight: Dp,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    hostSearchViewModel: HostSearchViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val hostListPaging = hostSearchViewModel.hostListPagingStateFlow.collectAsLazyPagingItems()
    val uiState by hostSearchViewModel.uiState.collectAsStateWithLifecycle()
    val firstTimeApiHit by hostSearchViewModel.isFirstTimeApiHit.collectAsStateWithLifecycle()


    val search by hostSearchViewModel.search.collectAsStateWithLifecycle()

    var showFilterScreen by rememberSaveable { mutableStateOf(false) }

    var onlineViewMenuOpen by remember {
        mutableStateOf<Pair<HostData?, Rect>?>(
            null
        )
    }

    var showResetFilterIcon by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState?.filterState) {

        hostSearchViewModel.manualTrigger.collect { trigger ->

            Log.d("FILTER", "trigger = $trigger")

            val isPopulated = uiState?.filterState?.checkStateIsPopulated() == true

            showResetFilterIcon = isPopulated && trigger == true
        }
    }

    LaunchedEffect(Unit) {

        if(showOnlineProfile){
            hostSearchViewModel.updateState { state ->
                state.copy(filterState = state.filterState?.copy(isOnline = if(showOnlineProfile) 1 else 0))
            }
            hostSearchViewModel.triggerFilter(true)
        }else{
            hostSearchViewModel.triggerFilter(false)
        }


    }

    LaunchedEffect(hostListPaging.loadState.refresh) {
        if (hostListPaging.loadState.refresh is LoadState.NotLoading) {
            hostSearchViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (hostListPaging.loadState.refresh is LoadState.Error) {
            (hostListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                hostSearchViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (hostListPaging.loadState.hasError) {
            (hostListPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }

    ObserveUIEvents(hostSearchViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

            is HomeSearchScreenEvents.CallPartner -> {
                NavigationProvider.callNavigator.startCallActivity(
                    context,
                    chatID = uIEvent.chatID,
                    partnerID = uIEvent.partnerID,
                    personName = uIEvent.personName,
                    callType = uIEvent.callType,
                    profilePic = uIEvent.profilePic
                )
            }

        }
    }

    PullToRefreshBox(
        isRefreshing = (uiState?.isRefreshing ?: false),
        onRefresh = {
            hostSearchViewModel.updateState {
                it.copy(isRefreshing = true)
            }
            hostListPaging.refresh()
        },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                StoryScreenSearchBar(
                    modifier = Modifier.weight(1f),
                    value = search ?: "",
                    onValueChange = {
                        hostSearchViewModel.updateSearch(search = it)
                    },
                    startIcon = R.drawable.search,
                    startIconTint = Color.Black,
                    bgColor = grey_12,
                    borderColor = Color.Transparent,
                    verticalPadding = 12.dp,
                    hint = "Search by host name...",
                    onShowEmojis = {}
                )
                Box(
                    modifier = Modifier

                ) {
                    Image(
                        painter = painterResource(
                            R.drawable.filter_circlebuttonsendreply
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (showResetFilterIcon) 1.dp else 0.dp,
                                color = if (showResetFilterIcon) theme_color_12 else Color.Unspecified,
                                shape = CircleShape
                            )
                            .clickable {
                                showFilterScreen = true
                            }
                            .padding(1.dp)
                    )

                    Row(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        AnimatedVisibility(showResetFilterIcon) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color = red_4, shape = CircleShape)
                                    .clickable {
                                        hostSearchViewModel.updateState { state ->
                                            state.copy(filterState = state.filterState?.getResetState())
                                        }
                                        hostSearchViewModel.triggerFilter(false)
                                        showFilterScreen = false
                                    }
                                    .padding(2.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.baseline_close_24),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(14.dp)
                                )
                            }
                        }
                    }
                }
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
            {

                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {

                    Text(
                        "Explore Hosts",
                        fontSize = HEADING_ROW_SP,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )

                }

                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .animateContentSize()
                            .animateItem()
                    ) {
                        AnimatedVisibility(
                            hostListPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false
                        ) {
                            CircularProgressIndicator(
                                color = theme_color_1, modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                items(hostListPaging.itemCount) { index ->
                    val hostData = hostListPaging.get(index)
                    HostProfileCard(
                        hostData = hostData,
                        isFullWidth = true,
                        modifier = Modifier,
                        onClickController = { item ->
                            hostSearchViewModel.changeOnlineNowCallControllerVisibility(
                                hostListPaging,
                                item
                            )
                        },
                        onOpenMenu = { item, rect ->
                            hostSearchViewModel.changeOnlineNowMenuVisibility(
                                hostListPaging.itemSnapshotList,
                                item
                            )
                            onlineViewMenuOpen = Pair(item, rect)
                        },
                        onClick = {
                            hostSearchViewModel.navigationEvent(
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
                        }, onCall = { data, callType ->

                            hostSearchViewModel.onEvent(
                                HomeSearchScreenEvents.CallPartner(
                                    chatID = data?.chatId,
                                    partnerID = data?.id,
                                    personName = data?.fullName,
                                    profilePic = data?.profilePic,
                                    callType = callType
                                )
                            )
                        }
                    )
                }
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    bottomPagingLoader(

                        isVisible = hostListPaging.loadState.append is LoadState.Loading || hostListPaging.loadState.append is LoadState.Error,
                        isRetry = hostListPaging.loadState.append is LoadState.Error,
                        onRetry = {
                            hostListPaging.retry()
                        })
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    AnimatedVisibility(hostListPaging.itemCount == 0) {
                        if ((hostListPaging.loadState.refresh is LoadState.NotLoading || hostListPaging.loadState.refresh is LoadState.Error) && firstTimeApiHit) {
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
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Spacer(modifier = Modifier.height(bottomTabBarHeight))

                }
            }


        }
    }


    if (onlineViewMenuOpen != null) {
        Dialog(onDismissRequest = {
            val tempItem = onlineViewMenuOpen
            onlineViewMenuOpen = null
            hostSearchViewModel.changeOnlineNowMenuVisibility(
                hostListPaging.itemSnapshotList,
                tempItem?.first
            )
        })
        {
            FloatingMenu(
                list = MenuOptions.entries,
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
                            hostSearchViewModel.block(onlineViewMenuOpen?.first?.id)
                            onlineViewMenuOpen = null
                        }

                        MenuOptions.REPORT -> {
                            hostSearchViewModel.block(onlineViewMenuOpen?.first?.id)
                            onlineViewMenuOpen = null
                        }
                    }
                },
                onDismiss = {
                    onlineViewMenuOpen = null
                })
        }
    }

    FilterScreen(
        show = showFilterScreen,
        filterState = uiState?.filterState,
        isCitiesLoading = uiState?.isCitiesLoading,

        citiesList = uiState?.citiesList,
        onStateChange = { filterState ->
            hostSearchViewModel.updateState { state ->
                state.copy(filterState = filterState)
            }
        },

        onDismiss = {
            showFilterScreen = false
        },
        onApply = {

            hostSearchViewModel.triggerFilter(isApplied = true)

            Log.d("fkbnkfnbf", uiState?.filterState.toString())
            showFilterScreen = false
        },
        onReset = {
            hostSearchViewModel.updateState { state ->
                state.copy(filterState = state.filterState?.getResetState())
            }
            hostSearchViewModel.triggerFilter(isApplied = false)
            showFilterScreen = false
        }
    )
}


