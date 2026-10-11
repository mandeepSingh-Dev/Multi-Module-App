package com.app.feature_referral.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SliderDefaults.drawStopIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.DateFormats
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.padding
import com.app.core_ui.components.strokeGapModifier
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_LIGHT
import com.app.core_ui.ui.theme.HEADING_ROW_SP
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.green_10
import com.app.core_ui.ui.theme.green_11
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_4
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_20
import com.app.feature_referral.remote.model.response.ReferralDetailsResponse
import com.app.feature_referral.ui.viewmodels.ReferralScreenViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import com.zodiaq.ui.theme.Shape_8
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Composable
@Preview
fun ReferEarnScreen(
    snackbarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    referralScreenViewModel: ReferralScreenViewModel = hiltViewModel()
) {

    val uiState by referralScreenViewModel.uiState.collectAsStateWithLifecycle()

    var showMainImage by rememberSaveable{mutableStateOf(false)}

    val coroutineScope = rememberCoroutineScope()


    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .animateContentSize()

        ) {

            HeaderView2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                title = "Refer & Earn",
                secTitle = "Invite friends and earn bonus coins together"
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                } )

            PullToRefreshBox(isRefreshing = uiState?.isRefreshing ?: false, onRefresh = {
                referralScreenViewModel.getReferralDetails(isRefreshing = true)
            }) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                )
                {

                    item {
                        Row {
                            AnimatedVisibility(showMainImage,
                                enter = slideInVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessVeryLow)){
                                    it
                                },
                                exit = slideOutVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy , stiffness = Spring.StiffnessVeryLow)){
                                    it
                                }
                            ) {
                                Image(
                                    painterResource(R.drawable.refer_earn_imgimage63),
                                    contentDescription = null,
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 15.dp
                                    )
                                )
                            }
                        }
                    }
                    item {
                        ReferrerFriendCard(
                            uiState?.data?.bonus,
                            modifier = Modifier
                                .onVisibilityChanged{
                                    if(it){
                                        showMainImage = true
                                    }
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 10.dp)
                        )
                    }
                    item {
                        ReferralCodeCard(
                            uiState?.data?.referralCode,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 20.dp)
                        )
                    }
                    item {
                        GradientButton(
                            onClick = {},
                            text = "Share",
                            showEndIcon = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 10.dp)
                        )
                    }
                    item {
                        Text(
                            "Share your code or link with friends", color = text_grey_4,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp, top = 10.dp)
                        )
                    }

                    item {
                        SuccessfullReferralsCard(
                            uiState?.data,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, top = 20.dp)
                        )
                    }
                    if (!uiState?.data?.referrals.isNullOrEmpty()) {
                        item {
                            Text(
                                "Referral History",
                                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                                fontSize = HEADING_ROW_SP,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp, horizontal = 20.dp)
                            )
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp, horizontal = 20.dp)
                                    .dropShadow(
                                        Shape_15,
                                        shadow = Shadow(
                                            color = Color.Black.copy(0.1f),
                                            radius = 10.dp
                                        )
                                    )
                                    .background(color = Color.White, Shape_15)
                                    .padding(vertical = 15.dp)
                            ) {

                                uiState?.data?.referrals?.forEachIndexed { index, referral ->
                                    ReferralHistoryItem(
                                        referral,
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    )
                                    if (index < (uiState?.data?.referrals?.size?.minus(1) ?: 0)) {
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 15.dp),
                                            thickness = 1.dp, color = divider_color_1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
            }
        }

    }

}

@Composable
private fun ReferrerFriendCard(
    bonus: ReferralDetailsResponse.ReferralDetailsData.Bonus?,
    modifier: Modifier
) {

    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .background(color = Color.Black, shape = Shape_20)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.profile_imagbackground_border),
                contentDescription = null
            )

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(bonus?.referrerLabel ?: "Referral", color = text_grey_2, fontSize = 12.sp)

                AnimatedContent(
                    bonus?.displayAmount,
                    modifier = Modifier.padding(top = 2.dp)
                ) { displayAmount ->
                    if (displayAmount.isNullOrEmpty()) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(50.dp)
                                .height(15.dp)
                        )
                    } else {

                        Text(
                            displayAmount ?: "",
                            color = theme_color_1,
                            fontFamily = HAFFER_TRIAL_BOLD,
                            fontSize = 20.sp,
                        )

                    }
                }
                Text("Coins", color = text_grey_5, fontSize = 13.sp)
            }
        }

        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 4.dp), thickness = 1.dp, color = grey_10
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.profile_imagbackground_border),
                contentDescription = null
            )

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(bonus?.friendLabel ?: "Friend", color = text_grey_2, fontSize = 12.sp)

                AnimatedContent(
                    bonus?.displayAmount,
                    modifier = Modifier.padding(top = 2.dp)
                ) { displayAmount ->
                    if (displayAmount.isNullOrEmpty()) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(50.dp)
                                .height(15.dp)
                        )
                    } else {
                        Text(
                            displayAmount,
                            color = theme_color_1,
                            fontFamily = HAFFER_TRIAL_BOLD,
                            fontSize = 20.sp,
                        )
                    }
                }
                Text("Coins", color = text_grey_5, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun ReferralCodeCard(referralCode: String?, modifier: Modifier) {



    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .strokeGapModifier(cornerRadius = 20.dp)
            .background(color = Color.White, shape = Shape_20)
            .padding(15.dp)
    ) {

        Image(painterResource(R.drawable.gift_icon), contentDescription = null)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text("Your Referral Code", fontSize = 13.sp)
            AnimatedContent(referralCode) { code ->
                if (code.isNullOrEmpty()) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(15.dp)
                    )
                } else {
                    GradientText(code, letterSpacing = 1.2.sp)
                }
            }
        }

        AnimatedContent(referralCode) { code ->
            if (code.isNullOrEmpty()) {
                ShimmerBox(
                    modifier = Modifier
                        .width(80.dp)
                        .height(15.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .border(width = 1.dp, color = grey_10, shape = Shape_8)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Image(painterResource(R.drawable.copycontainer), contentDescription = null)
                    Text("Copy", fontSize = 12.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
                }
            }
        }

    }

}

@Composable
private fun SuccessfullReferralsCard(
    data: ReferralDetailsResponse.ReferralDetailsData?,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .dropShadow(Shape_20, shadow = Shadow(color = Color.Black.copy(0.2f), radius = 5.dp))
            .background(color = Color.White, shape = Shape_20)
            .padding(20.dp)
    ) {
        Row() {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painterResource(R.drawable.people_group_iconbackground),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        "Successfull Referrals", fontSize = 13.sp,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD
                    )

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedContent(data?.verifiedCount) { verifiedCount ->
                            if (verifiedCount == null) {
                                ShimmerBox(
                                    modifier = Modifier
                                        .width(12.dp)
                                        .height(10.dp)
                                )
                            } else {
                                Text(
                                    (verifiedCount ?: 0).toString(),
                                    color = theme_color_1,
                                    fontSize = 19.sp,
                                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                                )
                            }
                        }
                        AnimatedContent(data?.remainingLimit) { remainingLimit ->
                            if (remainingLimit == null) {
                                ShimmerBox(
                                    modifier = Modifier
                                        .padding(start = 3.dp)
                                        .width(12.dp)
                                        .height(10.dp)
                                )
                            } else {
                                Text(
                                    "/ $remainingLimit",
                                    color = text_grey_4,
                                    fontSize = 13.sp,
                                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                                )
                            }
                        }
                    }
                }

            }

            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text("Maximum")
                AnimatedContent(data?.remainingLimit) { remainingLimit ->
                    if (remainingLimit == null) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(12.dp)
                                .height(10.dp)
                        )
                    } else {
                        Text(
                            "$remainingLimit verified referrals",
                            color = theme_color_20,
                            fontSize = 13.sp,
                            fontFamily = HAFFER_TRIAL_SEMI_BOLD
                        )
                    }
                }
            }
        }

        LinearProgressIndicator(
            gapSize = 0.dp,
            drawStopIndicator = {
                ProgressIndicatorDefaults.drawStopIndicator(
                    drawScope = this,
                    stopSize = 2.dp,
                    color = theme_color_1,
                    strokeCap = StrokeCap.Round,
                )
            },
            trackColor = grey_9,
            color = theme_color_20,
            progress = {
                data?.getSuccessfullReferralProgress() ?: 0f

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 5.dp)
                .height(7.dp)
        )
    }

}

@Composable
private fun ReferralHistoryItem(
    referralItem: ReferralDetailsResponse.ReferralDetailsData.Referral?,
    modifier: Modifier
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        ProfileImage(image = referralItem?.user?.profilePic)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(referralItem?.user?.fullName ?: "", fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 15.sp)
            referralItem?.joinedAt?.convertToDateFormat(toFormat = DateFormats.DATE_FORMAT_17)?.let {date ->
                Text(
                    date,
                    fontFamily = HAFFER_TRIAL_SEMI_LIGHT,
                    fontSize = 13.sp,
                    color = text_grey_1
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = green_11, Shape_8)
                    .background(color = green_10, Shape_8)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text("Verified", color = green_4, fontSize = 12.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(referralItem?.bonusDisplay ?: "", fontFamily = HAFFER_TRIAL_SEMI_BOLD, fontSize = 16.sp)
                Image(
                    painterResource(R.drawable.coins_stack_iconimage52),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

    }
}

