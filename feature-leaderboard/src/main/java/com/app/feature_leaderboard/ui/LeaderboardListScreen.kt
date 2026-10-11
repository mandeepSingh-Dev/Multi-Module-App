package com.app.feature_leaderboard.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.enums.UserType
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.RatingBar
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.emptyView
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.theme_color_29
import com.app.core_ui.ui.theme.theme_color_6
import com.app.core_ui.ui.theme.theme_color_7
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import com.app.core_ui.ui.theme.theme_color_11
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.app.core_ui.components.AsyncImageWithLoader
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.random.Random

@Composable
fun LeaderboardListScreen(
    userType: Int,
    leaderboardListViewModel: LeaderboardListViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val uiState by leaderboardListViewModel.uiState.collectAsStateWithLifecycle()
    val leaderboardListPaging =
        leaderboardListViewModel.leaderboardListPaging.collectAsLazyPagingItems()


    val showShimmer by remember {
        derivedStateOf {
            leaderboardListPaging.loadState.refresh == LoadState.Loading && leaderboardListPaging.itemCount == 0
        }
    }

    LaunchedEffect(leaderboardListPaging.loadState.refresh) {
        if (leaderboardListPaging.loadState.refresh is LoadState.NotLoading) {
            leaderboardListViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (leaderboardListPaging.loadState.refresh is LoadState.Error) {
            (leaderboardListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                leaderboardListViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (leaderboardListPaging.loadState.hasError) {
            (leaderboardListPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }

    BoxCommon() {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            val desc = if (userType == UserType.USER.value) {
                "Top users ranked by coins and activity."
            } else {
                "Top Earning hosts"
            }
            HeaderView2(
                title = "Leaderboard",
                secTitle = desc,
                modifier = Modifier.padding(horizontal = 20.dp),
                showNotificationIcon = false
            )


            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    leaderboardListViewModel.updateState {
                        it.copy(isRefreshing = true)
                    }
                    leaderboardListPaging.refresh()
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

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (showShimmer) {
                                ShimmerBox(modifier = Modifier
                                    .width(100.dp)
                                    .height(20.dp))
                            } else {
                                Text(
                                    uiState?.resetAt ?: "",
                                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            AnimatedVisibility(!uiState?.topThree.isNullOrEmpty()) {
                                PodiumSection(
                                    topThreeUsers = uiState?.topThree,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp))
                    }

                    if (showShimmer) {
                        items(5) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            )
                        }
                    } else {
                        items(leaderboardListPaging.itemCount) { index ->
                            val data = leaderboardListPaging.get(index)

                            LeaderBoardListCard(data)

                        }
                    }



                    item {
                        bottomPagingLoader(
                            isVisible = leaderboardListPaging.loadState.append is LoadState.Loading || leaderboardListPaging.loadState.append is LoadState.Error,
                            isRetry = leaderboardListPaging.loadState.append is LoadState.Error,
                            onRetry = {
                                leaderboardListPaging.retry()
                            })
                    }

                    item {
                        if (leaderboardListPaging.itemCount == 0) {
                            if (leaderboardListPaging.loadState.refresh is LoadState.NotLoading || leaderboardListPaging.loadState.refresh is LoadState.Error) {
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
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

            }

        }

    }
}

@Composable
private fun AveragaRatingCard(
    averageRating: Int?,
    totalRating: Int?,
    showShimmer: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_15)
            .clip(Shape_15)
            .background(color = Color.White, Shape_15)
            .padding(12.dp)
    )
    {

        Image(
            painterResource(R.drawable.gift_smallimage63), contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Your Reviews & Ratings",
                fontSize = 17.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
            Text(
                "These are the reviews you have received from this user after call",
                fontSize = 11.sp,
                color = text_grey_1
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .dropShadowCustom(Shape_10)
                .border(brush = brush2(), shape = Shape_10, width = 1.dp)
                .background(shape = Shape_10, color = theme_color_7)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {

            AnimatedContent(showShimmer) { showShimmer ->
                if (showShimmer) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(20.dp)
                            .height(15.dp)
                    )
                } else {
                    Text(
                        (averageRating ?: 0).toString(),
                        fontSize = 15.sp,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD
                    )
                }
            }
            RatingBar(
                rating = 5,
                onRatingChanged = {},
                starSize = 12.dp,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
            )
            AnimatedContent(showShimmer) { showShimmer ->
                if (showShimmer) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(40.dp)
                            .height(15.dp)
                    )
                } else {
                    Text(
                        "${totalRating ?: 0} Reviews", fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
private fun LeaderBoardListCard(data: LeaderboardListResponse.LeaderboardData.LeaderBoardItem?) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_15)
            .border(width = 1.dp, color = theme_color_6, Shape_15)
            .background(color = theme_color_29, Shape_15)
            .padding(12.dp)
    ) {


        Text(
            (data?.rank ?: 1).toString(), fontSize = 15.sp,
            color = theme_color_11,
            fontFamily = HAFFER_TRIAL_BOLD
        )


        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            ProfileImage(
                image = data?.profilePic,
                modifier = Modifier.size(40.dp)
            )
            Text(data?.fullName ?: "", fontSize = 15.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Image(
                painterResource(R.drawable.coins_stack_iconimage52), contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text(data?.beansFormatted ?: "", fontSize = 16.sp, fontFamily = HAFFER_TRIAL_BOLD)
        }

    }
}

