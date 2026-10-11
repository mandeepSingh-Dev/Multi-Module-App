package com.app.feature_home.ui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.enums.ChatListType
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.ChatScreens
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.StoryScreenSearchBar
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.emptyView
import com.app.core_ui.ui.theme.HAFFER_TRIAL_REGULAR
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.grey_12
import com.app.core_ui.ui.theme.text_grey_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.feature_home.remote.model.response.ChatListResponse
import com.app.feature_home.ui.viewmodels.ChatUsersListViewModel
import com.zodiaq.ui.theme.Shape_20

@Composable
fun ChatUsersListScreen(
    bottomTabBarHeight: Dp,
    userType : Int,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    chatUsersListViewModel: ChatUsersListViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val chatsListPaging = chatUsersListViewModel.chatUsersList.collectAsLazyPagingItems()
    val uiState by chatUsersListViewModel.uiState.collectAsStateWithLifecycle()

    val search by chatUsersListViewModel.search.collectAsStateWithLifecycle()


    AttachLifecycleObserver {
        when(it){
            Lifecycle.Event.ON_RESUME -> {
                chatsListPaging.refresh()
            }
            else -> Unit
        }
    }

    LaunchedEffect(chatsListPaging.loadState.refresh) {
        if (chatsListPaging.loadState.refresh is LoadState.NotLoading) {
            chatUsersListViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (chatsListPaging.loadState.refresh is LoadState.Error) {
            (chatsListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                chatUsersListViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (chatsListPaging.loadState.hasError) {
            (chatsListPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }
    BoxCommon(isLoading = chatsListPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false) {
        PullToRefreshBox(
            isRefreshing = (uiState?.isRefreshing ?: false),
            onRefresh = {
                chatUsersListViewModel.updateState {
                    it.copy(isRefreshing = true)
                }
                chatsListPaging.refresh()
            },
            modifier = Modifier
                .fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {

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
                            chatUsersListViewModel.updateSearch(search = it)
                        },
                        startIcon = R.drawable.search,
                        startIconTint = Color.Black,
                        bgColor = grey_12,
                        borderColor = Color.Transparent,
                        verticalPadding = 12.dp,

                        hint = "Search by host name...",
                        onShowEmojis = {}
                    )
                    /* Image(
                        painter = painterResource(
                            R.drawable.filter_circlebuttonsendreply
                        ),
                        contentDescription = null
                    )*/
                }
                ChatOptionsRow(counts = uiState?.chatCounts) { type ->
                    chatUsersListViewModel.updateFilter(type.value)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .animateContentSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )
                {

                    items(chatsListPaging.itemCount) { index ->
                        val chatData = chatsListPaging.get(index)
                        chatListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableDebAnim {
                                   backStack.add(
                                        ChatScreens.ChatScreen(
                                            userType = userType,
                                            partnerID = chatData?.partner?.id,
                                            partnerName = chatData?.partner?.fullName,
                                            partnerProfilePic = chatData?.partner?.profilePic,
                                            isOnline = chatData?.partner?.isOnline,
                                            isPartnerFav = chatData?.partner?.isOnline,
                                            chatID = chatData?.id,
                                            isVerified = chatData?.partner?.isVerified,
                                            gender = chatData?.partner?.gender,
                                        )
                                    )
                                },
                            chatData,
                            onProfileIconClick = {
                                if(userType == UserType.HOST.value) {
                                    backStack.add(
                                        UserScreens.HostDetailsScreen(
                                            id = chatData?.partner?.id,
                                            fullName = chatData?.partner?.fullName,
                                            profilePic = chatData?.partner?.profilePic,
                                            chatId = chatData?.id,
                                            rating = chatData?.partner?.rating,
                                            isVerified = chatData?.partner?.isVerified,
                                            isWishlist = null,
                                            gender = chatData?.partner?.gender
                                        ),
                                    )
                                }
                            }
                        )

                        if (index < chatsListPaging.itemCount) {
                            HorizontalDivider(
                                color = divider_color_1, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp)
                            )
                        }
                    }
                    item {
                        bottomPagingLoader(
                            isVisible = chatsListPaging.loadState.append is LoadState.Loading || chatsListPaging.loadState.append is LoadState.Error,
                            isRetry = chatsListPaging.loadState.append is LoadState.Error,
                            onRetry = {
                                chatsListPaging.retry()
                            })
                    }

                    item {
                        if (chatsListPaging.itemCount == 0) {
                            if (chatsListPaging.loadState.refresh is LoadState.NotLoading || chatsListPaging.loadState.refresh is LoadState.Error) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    emptyView(
                                        modifier = Modifier
                                            .fillMaxSize(),

                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(bottomTabBarHeight))
                    }
                }

            }
        }
    }
}


@Composable
private fun ChatOptionsRow(
    counts: ChatListResponse.ChatListData.Counts?,
    onClick: (ChatListType) -> Unit) {

    var selectedOption by remember { mutableIntStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(Shape_20)
                .clickableDebAnim {
                    onClick(ChatListType.ALL)
                    selectedOption = 0
                }
                .border(
                    width = if (selectedOption == 0) 1.dp else 0.dp,
                    color = if (selectedOption == 0) theme_color_1 else Color.Transparent,
                    shape = Shape_20
                )
                .padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(
                "All",
                fontFamily = if (selectedOption == 0) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_REGULAR,
                fontSize = 12.sp
            )
            counts?.all?.takeIf { it > 0 }?.let {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(color = theme_color_1),
                    contentAlignment = Alignment.Center
                ) {

                    Text(it.toString(), color = Color.White, fontSize = 9.sp)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(Shape_20)
                .clickableDebAnim {
                    onClick(ChatListType.UNREAD)
                    selectedOption = 1
                }
                .border(
                    width = if (selectedOption == 1) 1.dp else 0.dp,
                    color = if (selectedOption == 1) theme_color_1 else Color.White,
                    shape = Shape_20
                )
                .padding(horizontal = 15.dp, vertical = 10.dp)) {
            Text(
                "Unread",
                fontFamily = if (selectedOption == 1) HAFFER_TRIAL_SEMI_BOLD else HAFFER_TRIAL_REGULAR,
                fontSize = 12.sp
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp)
                    .background(color = theme_color_1),
                contentAlignment = Alignment.Center
            ) {
                counts?.unread?.takeIf { it > 0 }?.let {
                    Text(it.toString(), color = Color.White, fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
private fun chatListItem(modifier: Modifier, chatData: ChatListResponse.ChatListData.Chat?,
                         onProfileIconClick : () -> Unit) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ProfileImage(
            image = chatData?.partner?.profilePic,
            modifier = Modifier.size(40.dp)
                .clickableDebAnim{
                    onProfileIconClick()
                }
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                chatData?.partner?.fullName ?: "",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                fontSize = 15.sp
            )
            Text(
                chatData?.lastMessage ?: "", fontSize = 12.sp,
                lineHeight = 13.sp
            )
//            Text("Booking ID: #${chatData?.id}", color = text_grey_1, fontSize = 11.sp,
//                lineHeight = 13.sp)

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text("10:34 AM", color = text_grey_4, fontSize = 11.sp)

            chatData?.unreadCount?.let {
                if (it > 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(color = theme_color_11, CircleShape)
                            .padding(3.dp)
                    ) {
                        Text(
                            (chatData.unreadCount ?: 0).toString(),
                            color = Color.White,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

    }
}