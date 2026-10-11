package com.app.feature_home_host.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.PeriodTypes
import com.app.core_earning.model.response.EarningDetailsResponse
import com.app.core_earning.model.response.EarningTrendResponse
import com.app.core_navigation.WithDrawScreens
import com.app.core_ui.R
import com.app.core_ui.components.AnimatedContentShimmer
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.EarningChart
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP_MEDIUM
import com.app.core_ui.ui.theme.brush9
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.green_8
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.grey_12
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_4
import com.app.feature_home_host.ui.viewmodels.EarningDetailsViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun EarningDetailsScreen(
    bottomTabBarHeight: Dp,

    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    earningDetailsViewModel: EarningDetailsViewModel = hiltViewModel()
) {

    val uiState by earningDetailsViewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    var showComponents by rememberSaveable { mutableStateOf(false) }


    ObserveUIEvents(earningDetailsViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }
        }

    }

    BoxCommon(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize())
        {


            PullToRefreshBox(
                isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    earningDetailsViewModel.getEarningDetails(period = uiState?.selectedEarningDetailsOption, isRefreshing = true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .animateContentSize()
                ) {

                    TotalEarningsCard(
                        isLoading = uiState?.isEarningDetailsLoading,
                        totalEarnings = uiState?.earningDetails?.totalEarnings,
                        modifier = Modifier
                            .padding(top = 20.dp, horizontal = 20.dp)
                            .onVisibilityChanged {
                                coroutineScope.launch {
                                    if (it) {
                                        delay(100.milliseconds)
                                        showComponents = true
                                    }
                                }
                            }
                            .animateContentSize(),
                        selectedOption = uiState?.selectedEarningDetailsOption,
                        onSelect = {
                            earningDetailsViewModel.updateState { state ->
                                state.copy(selectedEarningDetailsOption = it)
                            }
                            earningDetailsViewModel.getEarningDetails(it)
                        },
                        earningDetails = uiState?.earningDetails,
                    )

                    val totalCalls = uiState?.earningDetails?.totalCalls
                    val totalTalkTime = uiState?.earningDetails?.totalTalkTime
                    val callEarnings = uiState?.earningDetails?.callEarnings
                    val giftEarnings = uiState?.earningDetails?.giftEarnings
                    AnimatedVisibility(showComponents) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                                .padding(horizontal = 20.dp, top = 15.dp)
                                .animateContentSize()
                        )
                        {
                            EarningDetailCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                icon = R.drawable.total_call,
                                period = uiState?.earningDetails?.period,
                                isLoading = uiState?.isEarningDetailsLoading ?: false,
                                title = "Total Call",
                                value = if (totalCalls == null) "" else (totalCalls
                                    ?: 0).toString(),
                                growth = uiState?.earningDetails?.growth?.totalCalls,
                            )
                            EarningDetailCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                icon = R.drawable.total_talk_time,
                                period = uiState?.earningDetails?.period,
                                title = "Total Talk Time",
                                value = totalTalkTime ?: "",
                                growth = uiState?.earningDetails?.growth?.totalTalkTime,
                                isLoading = uiState?.isEarningDetailsLoading ?: false,
                            )
                        }
                    }
                    AnimatedVisibility(showComponents) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                                .padding(top = 10.dp, horizontal = 20.dp)
                                .animateContentSize()
                        )
                        {
                            EarningDetailCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                icon = R.drawable.earning_from_calls,
                                period = uiState?.earningDetails?.period,
                                title = "Earnings from Calls",
                                value = if (callEarnings == null) "" else (callEarnings
                                    ?: 0).toString(),
                                growth = uiState?.earningDetails?.growth?.callEarnings,
                                isLoading = uiState?.isEarningDetailsLoading ?: false,
                            )
                            EarningDetailCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                icon = R.drawable.gift_received,
                                period = uiState?.earningDetails?.period,
                                title = "Gift Received",
                                value = if (giftEarnings == null) "" else (giftEarnings
                                    ?: 0).toString(),
                                growth = uiState?.earningDetails?.growth?.giftEarnings,
                                isLoading = uiState?.isEarningDetailsLoading ?: false,
                            )
                        }
                    }
                    withdrawalCard(modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp), onClick = {
                        earningDetailsViewModel.navigationEvent(WithDrawScreens.WithdrawBeansScreens)
                    })

                    AnimatedVisibility(showComponents) {
                        EarningTrendGraphCard(
                            modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp),
                            isLoading = uiState?.isEarningTrendDetailsLoading,
                            selectedOption = uiState?.selectedEarningTrendDetailsOption,
                            onSelect = {

                                earningDetailsViewModel.updateState { state ->
                                    state.copy(selectedEarningTrendDetailsOption = it)
                                }
                                earningDetailsViewModel.getEarningTrendDetails(it)
                            },
                            earningTrendDetails = uiState?.earningTrendDetails
                        )
                    }
                    AnimatedVisibility(showComponents) {
                        recentTransactionSection(
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                top = 20.dp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(bottomTabBarHeight.plus(50.dp)))
                }
            }
        }

    }
}

@Composable
private fun TotalEarningsCard(
    isLoading: Boolean?,
    totalEarnings: Int?,
    modifier: Modifier,
    selectedOption: String?,
    onSelect: (String?) -> Unit,
    earningDetails: EarningDetailsResponse.EarningDetialsData?
) {


    val options = remember {
        PeriodTypes.entries.filterNot { it == PeriodTypes.Day }.map { it.value }
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20, color = Color.Black.copy(0.05f))
            .border(width = 1.dp, color = theme_color_4.copy(alpha = 0.5f), shape = Shape_20)
            .background(brush = brush9(), shape = Shape_20)
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
        )
        {

            Image(
                painterResource(R.drawable.half_circletoday_earn),
                contentDescription = null,
                modifier = Modifier.size(65.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {

                Text(
                    "Total Earnings", fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp,
                    color = grey_1
                )

                AnimatedContent(isLoading) { isLoading ->
                    if (isLoading ?: false) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(60.dp)
                                .height(20.dp)
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .animateContentSize(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        )
                        {
                            earningDetails?.totalEarnings?.let {
                                Text(
                                    "${earningDetails.totalEarnings ?: 0}",
                                    fontSize = 21.sp,
                                    fontFamily = HAFFER_TRIAL_BOLD,
                                    lineHeight = 10.sp
                                )
                            }
                            Text(
                                " Beans", fontSize = 15.sp, fontFamily = HAFFER_TRIAL_BOLD,
                                lineHeight = 10.sp
                            )
                        }

                    }
                }
            }

            ComposableFilterDropdown(
                options = options,

                getLabel = {
                    when (it) {
                        PeriodTypes.Week.value -> "This Week"
                        PeriodTypes.Month.value -> "This Month"
                        PeriodTypes.Year.value -> "This Year"
                        else -> ""
                    }
                },
                getSelectedOptionLabel = {
                    when (it) {
                        PeriodTypes.Week.value -> "This Week"
                        PeriodTypes.Month.value -> "This Month"
                        PeriodTypes.Year.value -> "This Year"
                        else -> ""
                    }
                },
                selectedOption = selectedOption,
                onSelect = onSelect,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 5.dp)
        )
        {
            Text(
                "Great Conversation. Greater Earnings!",
                color = grey_10,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.call_graphjminiascendingbarchartgraphic),
                contentDescription = null
            )
        }
    }

}

@Composable
fun EarningDetailCard(
    modifier: Modifier,
    icon: Int,
    period: String?,
    title: String,
    value: String,
    isLoading: Boolean,
    growth: EarningDetailsResponse.EarningDetialsData.Growth.GrowthData?
) {

    val perDesc = when (period) {
        PeriodTypes.Month.value -> "vs last month"
        PeriodTypes.Week.value -> "vs last week"
        PeriodTypes.Year.value -> "vs last year"
        PeriodTypes.Day.value -> "vs last day"
        else -> ""
    }

    val direction = if (growth?.direction == "positive") "↑" else "↓"

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .dropShadowCustom(Shape_15, theme_color_1.copy(0.1f))
            .clip(Shape_15)
            .border(width = 1.dp, color = grey_9.copy(alpha = 0.5f), shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(10.dp)
    ) {

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(painterResource(icon), contentDescription = null)
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(title, color = text_grey_5, fontSize = 11.sp, lineHeight = 13.sp)
                AnimatedContentShimmer(isLoading = isLoading) {
                    Text(
                        value, color = Color.Black, fontFamily = HAFFER_TRIAL_BOLD, fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.Bottom) {

            AnimatedContentShimmer(
                modifierShimmerBox = Modifier
                    .width(30.dp)
                    .height(10.dp),
                isLoading = isLoading
            ) {
                Text(
                    "$direction ${growth?.value ?: 0} ",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 12.sp,
                    color = green_8,
                    lineHeight = 1.sp
                )
            }
            AnimatedContentShimmer(
                modifierShimmerBox = Modifier
                    .width(30.dp)
                    .height(10.dp),
                isLoading = isLoading
            ) {
                Text(perDesc, fontSize = 10.sp, color = text_grey_5, lineHeight = 1.sp)

            }

        }
    }
}

@Composable
private fun withdrawalCard(modifier: Modifier,
                           onClick : () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20, color = Color.Black.copy(0.05f))
            .border(width = 1.dp, color = theme_color_4.copy(alpha = 0.5f), shape = Shape_20)
            .background(brush = brush9(), shape = Shape_20)
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {

        Image(painterResource(R.drawable.withdrawal), contentDescription = null)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {

            Text(
                "Ready to withdraw?",
                fontFamily = HAFFER_TRIAL_BOLD,
                fontSize = 13.sp,
                lineHeight = 14.sp
            )
            Text(
                "Transfer your Beans to your wallet",
                color = text_grey_5,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )

        }

        GradientButton(
            onClick = onClick,
            text = "Withdraw Now",
            showEndIcon = true, fontSize = 12.sp,
            verticalPadding = 8.dp,
            horizontalPadding = 8.dp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }

}

@Composable
fun ComposableFilterDropdown(
    options: List<String>,
    getLabel: (String) -> String,
    getSelectedOptionLabel: (String) -> String,
    selectedOption: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = theme_color_4,
    bgColor: Color = Color.White
) {


    var expanded by remember { mutableStateOf(false) }


    Box(modifier = modifier) {
        // Trigger Button
        Row(
            modifier = Modifier
                .dropShadowCustom(Shape_15)
                .clip(Shape_15)
                .background(bgColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = Shape_15
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded = !expanded }
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .animateContentSize(
                    animationSpec = spring(
                        Spring.DampingRatioMediumBouncy,
                        Spring.StiffnessVeryLow
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getSelectedOptionLabel(selectedOption ?: "") ?: "",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Image(
                painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = "Toggle Dropdown",
                modifier = Modifier
            )
        }

        // Floating Popup Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },

            containerColor = Color.Transparent,
            modifier = Modifier
                .width(130.dp)
                .background(Color.White, shape = Shape_20)
        )
        {
            options.forEach { option ->
                val isSelected = option == selectedOption
                DropdownMenuItem(
                    text = {
                        Text(
                            text = getLabel(option),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFFD97706) else Color(0xFF334155)
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun EarningTrendGraphCard(
    modifier: Modifier,
    selectedOption: String?,

    onSelect: (String?) -> Unit,
    earningTrendDetails: List<EarningTrendResponse.EarningTrendData?>?,
    isLoading: Boolean?,
) {
    val options = remember {
        PeriodTypes.entries.filterNot { it == PeriodTypes.Year }.map { it.value }
    }


    val list = earningTrendDetails?.filterNotNull()

    val values = list?.map { it.value ?: 0 }
    val labels = list?.map { it.lable ?: "_" }

    Log.d("kfnbnknbf", values.toString())
    Log.d("kfnbnknbf", labels.toString())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20, color = Color.Black.copy(0.09f))
            .border(width = 1.dp, color = grey_12.copy(alpha = 0.5f), shape = Shape_20)
            .background(color = grey_12, shape = Shape_20)
            .padding(15.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Earnings Trend",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                modifier = Modifier.weight(1f),
                fontSize = HEADING_ROW_SP_MEDIUM
            )
            ComposableFilterDropdown(
                borderColor = grey_12,
                bgColor = grey_12.copy(alpha = 0.8f),
                options = options,
                getLabel = {
                    when (it) {
                        PeriodTypes.Day.value -> "Last 7 Days"
                        PeriodTypes.Week.value -> "Last 30 Days"
                        PeriodTypes.Month.value -> "Last 12 Months"
                        else -> ""
                    }
                },
                getSelectedOptionLabel = {
                    when (it) {
                        PeriodTypes.Day.value -> "Last 7 Days"
                        PeriodTypes.Week.value -> "Last 30 Days"
                        PeriodTypes.Month.value -> "Last 12 Months"
                        else -> ""
                    }
                },
                selectedOption = selectedOption,
                onSelect = onSelect,
            )
        }

        AnimatedContent(((isLoading ?: false) && values.isNullOrEmpty())) { shouldShowEmpty ->
            if (!shouldShowEmpty) {
                values?.let {
                    EarningChart(
                        values = values,
                        valuesLabels = labels ?: emptyList(),
                    )
                }
            } else {
                Text(
                    "No data available",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    color = grey_10,
                    fontSize = 12.sp
                )
            }
        }

    }
}

@Composable
fun recentTransactionSection(modifier: Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(Shape_20, color = Color.Black.copy(0.09f))
            .border(width = 1.dp, color = grey_12.copy(alpha = 0.5f), shape = Shape_20)
            .background(color = grey_12, shape = Shape_20)
            .padding(15.dp)
    ) {
        Text(
            "Recent Transactions",
            fontSize = HEADING_ROW_SP_MEDIUM,
            fontFamily = HAFFER_TRIAL_SEMI_BOLD
        )
        repeat(30) { index ->
            HostTransactionHistoryItem(Modifier.padding(top = if (index == 0) 25.dp else 1.dp))
            if (index < 30.minus(1)) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = divider_color_1,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun HostTransactionHistoryItem(modifier: Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val icon =
            if (false) R.drawable.gift_iconbackground else R.drawable.added_icon

        ProfileImage(modifier = Modifier.size(30.dp), image = "")

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                "Call with Rohan",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                modifier = Modifier
            )
            Text(
                "26 Aug 2026, 10:24 PM . 32 min",
                color = text_grey_2,
                fontSize = 11.sp
            )
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "+320 Beans",
                color = theme_color_11,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
//            val time = item?.createdAt?.convertToDateFormat(toFormat = TIME_FORMAT_1)
            Text("Call Earning" ?: "", color = text_grey_2, fontSize = 11.sp)
        }
    }
}