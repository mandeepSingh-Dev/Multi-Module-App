package com.app.feature_notification.ui

import android.util.Log
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.EmptyViewType
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.emptyView
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.feature_notification.remote.model.NotificationListResponse
import com.zodiaq.ui.theme.Shape_15
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds


@Composable
@Preview
fun NotificationListScreen(notificationListViewModel: NotificationListViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val uiState by notificationListViewModel.uiState.collectAsStateWithLifecycle()
    val notificationListPaging =
        notificationListViewModel.notificationListPaging.collectAsLazyPagingItems()


    val showShimmer by remember {
        derivedStateOf {
            notificationListPaging.loadState.refresh == LoadState.Loading && notificationListPaging.itemCount == 0
        }
    }

    LaunchedEffect(notificationListPaging.loadState.refresh) {
        if (notificationListPaging.loadState.refresh is LoadState.NotLoading) {
            notificationListViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (notificationListPaging.loadState.refresh is LoadState.Error) {
            (notificationListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                notificationListViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (notificationListPaging.loadState.hasError) {
            (notificationListPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }

    BoxCommon() {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            HeaderView2(
                title = "Notifications",
                modifier = Modifier.padding(horizontal = 20.dp),
                showNotificationIcon = false
            )


            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    notificationListViewModel.updateState {
                        it.copy(isRefreshing = true)
                    }
                    notificationListPaging.refresh()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                        .animateContentSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                )
                {

                    if (showShimmer) {
                        items(5) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            )
                        }
                    } else {
                        items(notificationListPaging.itemCount) { index ->
                            val data = notificationListPaging.get(index)

                            NotificationCardItem(data)

                        }
                    }
                    item {
                        bottomPagingLoader(
                            isVisible = notificationListPaging.loadState.append is LoadState.Loading || notificationListPaging.loadState.append is LoadState.Error,
                            isRetry = notificationListPaging.loadState.append is LoadState.Error,
                            onRetry = {
                                notificationListPaging.retry()
                            })
                    }

                    item {
                        if (notificationListPaging.itemCount == 0) {
                            if (notificationListPaging.loadState.refresh is LoadState.NotLoading || notificationListPaging.loadState.refresh is LoadState.Error) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    emptyView(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        emptyViewType = EmptyViewType.NOTIFICATION
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

            }

        }

    }
}

@Composable
fun NotificationCardItem(data: NotificationListResponse.NotificationData?) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .dropShadowCustom(shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(12.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.logo_1image1),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(data?.title ?: "", fontSize = 17.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
            Text(
                data?.description ?: "",
                fontSize = 13.sp,
                color = text_grey_3
            )

            data?.createdAt?.convertToDateFormat(DateFormats.DATE_TIME_FORMAT_3)?.let {
                Text(
                    it,
                    fontSize = 11.sp,
                    color = text_grey_2,
                    modifier = Modifier.align(
                        Alignment.End
                    )
                )
            }
        }

    }
}