package com.app.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.EmptyViewType
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.emptyView
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.brushBottomShadowStyle
import com.app.feature_block.remote.model.BlockUsersListResponse
import com.app.ui.events.BlockListScreenEvents
import com.app.ui.viewmodels.BlockListViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_100
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20

@Composable
fun BlockListScreen(
    userType: Int,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    blockListViewModel: BlockListViewModel = hiltViewModel()
) {


    val uiState by blockListViewModel.uiState.collectAsStateWithLifecycle()
    var showUnBlockHostDialog by rememberSaveable { mutableStateOf<String?>(null) }

    ObserveUIEvents(blockListViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is CommonUIEvents.ShowMessage -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.message)
            }

            is BlockListScreenEvents.UnBlockSuccessfully -> {
                showUnBlockHostDialog = null
                blockListViewModel.getBlockList(isRefreshing = true)
            }
        }
    }

    BoxCommon(
        uiState?.isLoading ?: false
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            HeaderView2(
                title = "Blocked ${if (userType == UserType.USER.value) "Hosts" else "Users"}",
                modifier = Modifier.padding(horizontal = 20.dp), onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }
            )

            PullToRefreshBox(
                isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    blockListViewModel.getBlockList(isRefreshing = true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                    if (uiState?.isLoading ?: false) {
                        ShimmerLayout()
                    } else {
                            if (!uiState?.data.isNullOrEmpty()) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    contentPadding = PaddingValues(20.dp),
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {

                                    if (!uiState?.data.isNullOrEmpty()) {
                                        items(uiState?.data ?: emptyList()) {
                                            BlockedUserCard(modifier = Modifier, it) {
                                                showUnBlockHostDialog = it?.id
                                            }
                                        }
                                    }

                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                            } else {
                                emptyView(
                                    isShowImage = true,
                                    emptyViewType = EmptyViewType.BLOCKED_USERS,
                                    modifier = Modifier.fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                )
                            }
//                        }
//                    }
                }
            }

        }

    }



    ButtonsDialog(
        showDialog = !showUnBlockHostDialog.isNullOrEmpty(),
        showLoader = uiState?.isBlockLoading ?: false,
        title = "Unblock User?",
        description = "Are you sure you want to unblock this user?",
        icon = R.drawable.block_userblockedusers,
        onDismiss = {
            showUnBlockHostDialog = null
        },
        showBothButtons = true,
        positiveBtnText = "Unblock",
        negativeBtnText = "Cancel",
        onPositiveClick = {
            blockListViewModel.unblock(showUnBlockHostDialog)
        },
        onNegativeClick = {
            showUnBlockHostDialog = null
        }
    )

}
@Composable
private fun ShimmerLayout(){


    Column(verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
        repeat(3){
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                repeat(2){
                    ShimmerBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(240.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun BlockedUserCard(
    modifier: Modifier = Modifier,
    data: BlockUsersListResponse.BlockUserData? = null,
    onClick: (BlockUsersListResponse.BlockUserData?) -> Unit,
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    var rect by remember { mutableStateOf(Rect.Zero) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(Shape_20)

    ) {

        AsyncImageWithLoader(
            image = data?.profilePic,
            modifier = Modifier
                .matchParentSize(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = brushBottomShadowStyle())
        )

        GradientButton(
            onClick = {
                onClick(data)
            },
            text = "Unblock",
            shape = Shape_15,
            verticalPadding = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(10.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)

        ) {
            Text(
                data?.fullName ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
