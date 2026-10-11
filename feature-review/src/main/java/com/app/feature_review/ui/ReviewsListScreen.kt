package com.app.feature_review.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_common.utils.DateFormats.DATE_TIME_FORMAT_3
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.RatingBar
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.emptyView
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.theme_color_29
import com.app.core_ui.ui.theme.theme_color_6
import com.app.core_ui.ui.theme.theme_color_7
import com.app.feature_review.remote.model.response.ReviewsListResponse
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15


@Composable
@Preview
fun ReviewsListScreen(
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    reviewsListViewModel: ReviewsListViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val uiState by reviewsListViewModel.uiState.collectAsStateWithLifecycle()
    val reviewsListPaging =
        reviewsListViewModel.reviewsListPaging.collectAsLazyPagingItems()


    val showShimmer by remember {
        derivedStateOf {
            reviewsListPaging.loadState.refresh == LoadState.Loading && reviewsListPaging.itemCount == 0
        }
    }

    LaunchedEffect(reviewsListPaging.loadState.refresh) {
        if (reviewsListPaging.loadState.refresh is LoadState.NotLoading) {
            reviewsListViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (reviewsListPaging.loadState.refresh is LoadState.Error) {
            (reviewsListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                reviewsListViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (reviewsListPaging.loadState.hasError) {
            (reviewsListPaging.loadState.refresh as LoadState.Error).error.let {
                snackbarHostState.showSnackBarWithDismiss(it.message.toString())
            }

        }
    }

    BoxCommon() {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            HeaderView2(
                title = "Your Reviews",
                modifier = Modifier.padding(horizontal = 20.dp),
                showNotificationIcon = false
            )


            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    reviewsListViewModel.updateState {
                        it.copy(isRefreshing = true)
                    }
                    reviewsListPaging.refresh()
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
                        AveragaRatingCard(
                            averageRating = uiState?.summary?.averageRating,
                            totalRating = uiState?.summary?.totalReviews,
                            showShimmer
                        )
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
                        items(reviewsListPaging.itemCount) { index ->
                            val data = reviewsListPaging.get(index)

                            RatingItem(data)

                        }
                    }
                    item {
                        bottomPagingLoader(
                            isVisible = reviewsListPaging.loadState.append is LoadState.Loading || reviewsListPaging.loadState.append is LoadState.Error,
                            isRetry = reviewsListPaging.loadState.append is LoadState.Error,
                            onRetry = {
                                reviewsListPaging.retry()
                            })
                    }

                    item {
                        if (reviewsListPaging.itemCount == 0) {
                            if (reviewsListPaging.loadState.refresh is LoadState.NotLoading || reviewsListPaging.loadState.refresh is LoadState.Error) {
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
    averageRating: Number?,
    totalRating: Int?,
    showShimmer: Boolean
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
            .dropShadowCustom(Shape_15)
            .clip(Shape_15)
            .background(color = Color.White,Shape_15)
            .padding(horizontal = 12.dp, vertical = 15.dp)
    )
    {

        Image(
            painterResource(R.drawable.gift_smallimage63), contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "Your Reviews & Ratings",
                fontSize = 17.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                lineHeight = 18.sp
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
                if(showShimmer){
                    ShimmerBox(modifier = Modifier.width(20.dp).height(15.dp))
                }else {
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
                    ShimmerBox(modifier = Modifier.width(40.dp).height(15.dp))
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
private fun RatingItem(item: ReviewsListResponse.ReviewData.ReviewDataItem?) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_15)
            .border(width = 1.dp, color = theme_color_6, Shape_15)
            .background(color = theme_color_29, Shape_15)
            .padding(12.dp)
    ) {
        Image(
            painterResource(R.drawable.call_iconlike), contentDescription = null,
            modifier = Modifier
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            )
            {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Call", fontSize = 16.sp,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD
                    )
                    item?.createdAt?.convertToDateFormat(toFormat = DATE_TIME_FORMAT_3)?.let {
                        Text(
                            it, fontSize = 11.sp,
                            color = text_grey_2
                        )
                    }
                }

                RatingBar(
                    rating = 2, onRatingChanged = {}, starSize = 12.dp,
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier
                )
//                Image(painterResource(R.drawable.outline_more_vert_24), contentDescription = null)
            }

            Text(item?.comment ?: "")
        }

    }
}