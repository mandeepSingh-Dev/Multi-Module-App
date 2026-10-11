package com.app.feature_home.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.tooling.ComposeToolingApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.MenuOptions
import com.app.core_navigation.AppScreens
import com.app.core_navigation.NavigationProvider
import com.app.core_navigation.UserScreens
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.emptyView
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP
import com.app.core_ui.ui.theme.theme_color_1
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.events.HomeSearchScreenEvents
import com.app.feature_home.ui.viewmodels.FavouriteHostsListViewModel


@Composable
fun FavouriteHostsScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    favouriteHostsListViewModel : FavouriteHostsListViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val hostListPaging = favouriteHostsListViewModel.favouriteListPagingStateFlow.collectAsLazyPagingItems()
    val uiState by favouriteHostsListViewModel.uiState.collectAsStateWithLifecycle()
    val firstTimeApiHit by favouriteHostsListViewModel.isFirstTimeApiHit.collectAsStateWithLifecycle()

    LaunchedEffect(hostListPaging.loadState.refresh) {
        if (hostListPaging.loadState.refresh is LoadState.NotLoading) {
            favouriteHostsListViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (hostListPaging.loadState.refresh is LoadState.Error) {
            (hostListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                favouriteHostsListViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (hostListPaging.loadState.hasError) {
            (hostListPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }
    var onlineViewMenuOpen by remember {
        mutableStateOf<Pair<HostData?, Rect>?>(
            null
        )
    }
    ObserveUIEvents(favouriteHostsListViewModel.uiEvent) { uIEvent ->
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

    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderView2(title = "Favourite Hosts",
                modifier = Modifier.padding(horizontal = 20.dp) , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                })

            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    favouriteHostsListViewModel.updateState {
                        it.copy(isRefreshing = true)
                    }
                    hostListPaging.refresh()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                )
                {

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
                                favouriteHostsListViewModel.changeOnlineNowCallControllerVisibility(
                                    hostListPaging,
                                    item
                                )
                            },
                            onOpenMenu = { item, rect ->
                                favouriteHostsListViewModel.changeOnlineNowMenuVisibility(
                                    hostListPaging.itemSnapshotList,
                                    item
                                )
                                onlineViewMenuOpen = Pair(item, rect)
                            },
                            onClick = {hostData ->
                                favouriteHostsListViewModel.navigationEvent(
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

                                favouriteHostsListViewModel.onEvent(
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
                        Row{
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
                    }
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))

                    }
                }
            }

        }

    }
    if (onlineViewMenuOpen != null) {
        Dialog(onDismissRequest = {
            val tempItem = onlineViewMenuOpen
            onlineViewMenuOpen = null
            favouriteHostsListViewModel.changeOnlineNowMenuVisibility(
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
                            favouriteHostsListViewModel.block(onlineViewMenuOpen?.first?.id)
                            onlineViewMenuOpen = null
                        }

                        MenuOptions.REPORT -> {
                            favouriteHostsListViewModel.block(onlineViewMenuOpen?.first?.id)
                            onlineViewMenuOpen = null
                        }
                    }
                },
                onDismiss = {
                    onlineViewMenuOpen = null
                })
        }
    }
}