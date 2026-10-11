package com.app.feature_home_host.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LOG_TAG
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.DateFormats.DATE_TIME_FORMAT_3
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GiftReceivedCard
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.RatingBar
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush10
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_29
import com.app.core_ui.ui.theme.theme_color_6
import com.app.core_ui.ui.theme.theme_color_7
import com.app.feature_home_host.remote.model.response.VisitorDetailsResponse
import com.app.feature_home_host.ui.viewmodels.VisitorDetailsViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


@Composable
fun VisitorDetailsScreen(
    visitorId : String?,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    visitorDetailsViewModel: VisitorDetailsViewModel = hiltViewModel()
) {

    val uiState by visitorDetailsViewModel.uiState.collectAsStateWithLifecycle()



    LaunchedEffect(visitorId) {
        visitorDetailsViewModel.getVisitorDetails(visitorId = visitorId)
    }
    val showShimmers by remember {
        derivedStateOf {
            (uiState?.isLoading == true && uiState?.data == null)
        }
    }



    ObserveUIEvents(visitorDetailsViewModel.uiEvent) { uiEvent ->
        when (uiEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uiEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uiEvent.navKey)
            }
        }
    }

    BoxCommon(modifier = Modifier.fillMaxSize()
        .imePadding()) {

        Column(modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()
        ) {
            HeaderView2(
                title = "User Details",
                modifier = Modifier.padding(horizontal = 20.dp)
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }  )

            PullToRefreshBox(
                isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    visitorDetailsViewModel.getVisitorDetails(visitorId = visitorId, isRefreshing = true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    DetailsCard(
                        modifier = Modifier.padding(horizontal = 20.dp, top = 15.dp),
                        uiState?.data,
                        showShimmers
                    )
                    StatsDataCard(
                        modifier = Modifier.padding(horizontal = 20.dp, top = 15.dp),
                        uiState?.data,
                        showShimmers
                    )
                    GiftsSection(
                        modifier = Modifier.padding(horizontal = 20.dp, top = 15.dp),
                        uiState?.data,
                        showShimmers
                    )
                    RatingsReviewsSection(
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            top = 15.dp
                        ),
                        uiState?.data,
                        showShimmers
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailsCard(
    modifier: Modifier,
    data: VisitorDetailsResponse.Data?,
    showShimmers: Boolean
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {

        if(showShimmers){

            ShimmerBox(modifier = Modifier
                .clip(Shape_15)
                .width(100.dp)
                .aspectRatio(1f)
            )
        }else {
            AsyncImageWithLoader(
                image = data?.visitor?.profilePic,
                modifier = Modifier
                    .clip(Shape_15)
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(60.dp)
                    .height(15.dp)
                )
            }else {
                Text(
                    data?.visitor?.fullName ?: "",
                    fontSize = 18.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(50.dp)
                    .height(15.dp)
                )
            }else {
                data?.visitor?.age?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.profile_gold),
                            contentDescription = null
                        )
                        Text(
                            "${data.visitor.age ?: 0} Years old",
                            fontSize = 11.sp,
                            lineHeight = 10.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(50.dp)
                    .height(15.dp)
                )
            }else {
                data?.visitor?.city?.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.location_gold),
                            contentDescription = null
                        )
                        Text((data.visitor.city.replaceFirstChar { it.uppercase() }) ?: "", fontSize = 11.sp, lineHeight = 10.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(40.dp)
                    .height(15.dp)
                )
            }else {
                data?.visitor?.isOnline?.takeIf { it }?.let {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.active_now),
                            contentDescription = null
                        )
                        Text("Active now", fontSize = 11.sp, lineHeight = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsDataCard(
    modifier: Modifier,
    data: VisitorDetailsResponse.Data?,
    showShimmers: Boolean
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        val columnModifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .dropShadowCustom(Shape_20)
            .background(color = Color.White, shape = Shape_20)
            .padding(horizontal = 5.dp, vertical = 8.dp)
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = columnModifier
        ) {

            Image(
                painter = painterResource(R.drawable.phone_iconbackground),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(3.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(30.dp)
                    .height(15.dp)
                )
            }else {
                Text(
                    (data?.stats?.calls ?: 0).toString(),
                    fontSize = 15.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text("Calls", fontSize = 11.sp, textAlign = TextAlign.Center)
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = columnModifier
        ) {

            Image(
                painter = painterResource(R.drawable.video_call_iconvideo_call_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(3.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(30.dp)
                    .height(15.dp)
                )
            }else {
                Text(
                    (data?.stats?.videoCalls ?: 0).toString(),
                    fontSize = 15.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                "Video Calls", fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = columnModifier
        ) {

            Image(painter = painterResource(R.drawable.fav_icon), contentDescription = null)
            Spacer(modifier = Modifier.height(3.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(30.dp)
                    .height(15.dp)
                )
            }else {
                Text(
                    (data?.stats?.timesFavorited ?: 0).toString(),
                    fontSize = 15.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text("Times Favourite", fontSize = 11.sp, textAlign = TextAlign.Center)
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = columnModifier
        ) {

            Image(painter = painterResource(R.drawable.views), contentDescription = null)
            Spacer(modifier = Modifier.height(3.dp))
            if(showShimmers){

                ShimmerBox(modifier = Modifier
                    .clip(Shape_15)
                    .width(30.dp)
                    .height(15.dp)
                )
            }else {
                Text(
                    (data?.stats?.profileViews ?: 0).toString(),
                    fontSize = 15.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text("Profile Views", fontSize = 11.sp, textAlign = TextAlign.Center)
        }

    }
}

@Composable
private fun GiftsSection(
    modifier: Modifier,
    data: VisitorDetailsResponse.Data?,
    showShimmers: Boolean
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .dropShadowCustom(Shape_20)
            .background(color = Color.White, Shape_20)
            .padding(vertical = 12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
        {

            Image(
                painterResource(R.drawable.gift_smallimage63), contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text("Gifts", fontSize = 17.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
                Text(
                    "These are the gifts you have received from this user",
                    fontSize = 11.sp,
                    color = text_grey_1
                )
            }

            data?.gifts?.total?.let { total ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .dropShadowCustom(Shape_10)
                        .background(shape = Shape_10, brush = brush10())
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {

                    Text(
                        "TOTAL GIFTS", color = text_grey_3, fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                    if(showShimmers){

                        ShimmerBox(modifier = Modifier
                            .clip(Shape_15)
                            .width(20.dp)
                            .height(15.dp)
                        )
                    }else {
                        Text(total.toString(), fontSize = 15.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
                    }
                }
            }
        }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(top = 10.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                if(showShimmers){
                    repeat(7) {
                        ShimmerBox(
                            modifier = Modifier
                                .clip(Shape_15)
                                .width(80.dp)
                                .height(90.dp)
                        )
                    }
                }else {
                    data?.gifts?.items?.forEach {
                        GiftReceivedCard(
                            image = it?.image,
                            name = it?.name,
                            count = it?.quantity,
                            onClick = {},
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

    }
}


@Composable
private fun RatingsReviewsSection(
    modifier: Modifier,
    data: VisitorDetailsResponse.Data?,
    showShimmers: Boolean
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .dropShadowCustom(Shape_20)
            .background(color = Color.White, Shape_20)
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
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

                Text(
                    (data?.reviews?.averageRating ?: 0).toString(),
                    fontSize = 15.sp,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
                RatingBar(
                    rating = data?.reviews?.averageRating?.toInt() ?: 0,
                    onRatingChanged = {},
                    starSize = 12.dp,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                )
                Text(
                    "${data?.reviews?.total ?: 0} Reviews", fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        )
        {
            if(showShimmers){
                repeat(4){
                    ShimmerBox(modifier = Modifier.fillMaxWidth()
                        .height(80.dp))
                }
            }else{
                data?.reviews?.items?.forEach {
                    RatingItem(it)
                }
            }
        }
    }
}

@Composable
private fun RatingItem(item: VisitorDetailsResponse.Data.Reviews.Item?) {

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