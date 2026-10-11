package com.app.feature_wallet.ui.screens


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.DateFormats.TIME_FORMAT_1
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.localeUtils.convertToDateFormat
import com.app.core_common.utils.localeUtils.getParsedDate
import com.app.core_common.utils.localeUtils.getTodayYesterdayElseData
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.EmptyViewType
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.DateRangePicker
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.bottomPagingLoader
import com.app.core_ui.components.emptyView
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.green_8
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.red_4
import com.app.core_ui.ui.theme.text_grey_1
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.theme_color_1
import com.app.feature_wallet.remote.model.response.TransactionsListResponse
import com.app.feature_wallet.ui.states.TransactionHistoryUIState
import com.app.feature_wallet.ui.viewmodels.TransactionsHistoryViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


enum class TransactionTypes(val type : String?){
    ALL_TYPES("All Types"),
    CREDIT("Credit"),
    DEBIT("Debit")
}


@Composable
@Preview
fun TransactionHistoryScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    transactionsHistoryViewModel: TransactionsHistoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val transactionListPaging = transactionsHistoryViewModel.transactionsList.collectAsLazyPagingItems()
    val uiState by transactionsHistoryViewModel.uiState.collectAsStateWithLifecycle()

    val firstTimeApiHit by transactionsHistoryViewModel.isFirstTimeApiHit.collectAsStateWithLifecycle()

    var showDateRangePicker by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(transactionListPaging.loadState.refresh) {
        if (transactionListPaging.loadState.refresh is LoadState.NotLoading) {
            transactionsHistoryViewModel.updateState { it.copy(isRefreshing = false) }
        }
        if (transactionListPaging.loadState.refresh is LoadState.Error) {
            (transactionListPaging.loadState.refresh as LoadState.Error).error.let {
//                snackBarHostState.showSnackBarWithDismiss(message = it.message)
                transactionsHistoryViewModel.updateState { it.copy(isRefreshing = false) }
            }
        }
        if (transactionListPaging.loadState.hasError) {
            (transactionListPaging.loadState.refresh as LoadState.Error).error.let {
                context.showToast(message = it.message.toString())
                Log.d("fkbnfbnf", it.message.toString())
            }

        }
    }
    ObserveUIEvents(transactionsHistoryViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

        }
    }

    BoxCommon(modifier = Modifier.fillMaxSize().blur(if(showDateRangePicker) 10.dp else 0.dp)) {


        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            HeaderView2(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = "Transaction History",
                secTitle = "View your coin earnings, spending and transaction details",
                 onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }   )
            PullToRefreshBox(
                isRefreshing = (uiState?.isRefreshing ?: false),
                onRefresh = {
                    transactionsHistoryViewModel.updateState {
                        it.copy(isRefreshing = true)
                    }
                    transactionListPaging.refresh()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                ) {


                    DateTypesRow(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        uiState = uiState,
                        viewModel = transactionsHistoryViewModel,
                        onShowDatePicker = {
                            showDateRangePicker = true
                        },
                        onSelectType = {
                            transactionListPaging.refresh()
                        }
                               , onClearDate = {
                            transactionsHistoryViewModel.updateState { state ->
                                state.copy(fromDate = null, toDate = null)
                            }
                            transactionListPaging.refresh()
                        }
                    )


                    LazyColumn(
                        contentPadding = PaddingValues(20.dp),
                        modifier = Modifier.animateContentSize()
                    )
                    {

                        item {
                            val balance = uiState?.walletData?.balance
                            BalanceCard(
                                showButtons = false,
                                modifier = Modifier
                                    .padding(top = 15.dp),
                                balance = if (balance != null) (balance ?: 0).toString() else "",
                                onRecharge = {},
                                onTransactionHistory = {}
                            )
                        }

                        if(transactionListPaging.loadState.refresh is LoadState.Loading && uiState?.isRefreshing == false){
                            item{
                                transactionShimmer(modifier = Modifier.animateItem().padding(vertical = 20.dp))
                            }
                        }else{
                            items(transactionListPaging.itemCount) { index ->
                                val item = transactionListPaging.get(index)
                                val previousItem = if (index > 0) {

                                    val pIndex = index.minus(1)
                                    Log.d("kfnbkfnbf", pIndex.toString())
                                    transactionListPaging.get(pIndex)
                                } else {
                                    null
                                }

                                val dateStatus = item?.createdAt?.getTodayYesterdayElseData()
                                val previousDateStatus =
                                    previousItem.takeIf { it != null }?.createdAt?.getTodayYesterdayElseData()


                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp)
                                ) {
                                    if (dateStatus != previousDateStatus) {
                                        Text(
                                            dateStatus ?: "No Date",
                                            fontSize = 15.sp,
                                            modifier = Modifier.padding(vertical = 15.dp)
                                        )
                                    }

                                    TransactionHistoryItem(
                                        item,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                        item {
                            bottomPagingLoader(

                                isVisible = transactionListPaging.loadState.append is LoadState.Loading || transactionListPaging.loadState.append is LoadState.Error,
                                isRetry = transactionListPaging.loadState.append is LoadState.Error,
                                onRetry = {
                                    transactionListPaging.retry()
                                })
                        }
                        item {
                            AnimatedVisibility(transactionListPaging.itemCount == 0) {
                                if ((transactionListPaging.loadState.refresh is LoadState.NotLoading || transactionListPaging.loadState.refresh is LoadState.Error) && firstTimeApiHit) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        emptyView(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            emptyViewType = EmptyViewType.TRANSACTIONS
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(60.dp))

                        }
                    }

                }

            }
        }
    }

    DateRangePicker(
        show = showDateRangePicker,
        onDismiss = {
            showDateRangePicker = false
        },
        onRangeSelected = {
            showDateRangePicker = false
            transactionsHistoryViewModel.updateState { state ->
                state.copy(fromDate = it.startDate.getParsedDate(), toDate = it.endDate.getParsedDate())
            }
            transactionListPaging.refresh()
        }
    )
}


@Composable
private fun DateTypesRow(modifier: Modifier,
                         uiState: TransactionHistoryUIState?,
                         viewModel: TransactionsHistoryViewModel,
                         onShowDatePicker : () -> Unit,
                         onClearDate : () -> Unit,
                         onSelectType : () -> Unit,
                         ) {
var isAllTypesExpanded by rememberSaveable { mutableStateOf(false)}
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        )
        {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .clickable{
                        onShowDatePicker()
                    }
                    .border(width = 1.dp, color = grey_9, shape = CircleShape)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(horizontal = 15.dp, vertical = 5.dp)
            ) {
                Image(painterResource(R.drawable.cal_icon), contentDescription = null)
                Text("All Dates", modifier = Modifier.weight(1f), fontSize = 12.sp)
                Image(
                    painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = null
                )
            }
            Box(modifier = Modifier
                .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable{
                            isAllTypesExpanded = true
                        }
                        .border(width = 1.dp, color = grey_9, shape = CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                ) {
                    Image(painterResource(R.drawable.filet_icon), contentDescription = null)
                    Text(uiState?.transactionType ?: "All Types", modifier = Modifier.weight(1f), fontSize = 12.sp,
                        color = if(!uiState?.transactionType.isNullOrEmpty()) theme_color_1 else Color.Black)
                    Image(
                        painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    shape = Shape_20,
                    expanded = isAllTypesExpanded,
                    onDismissRequest = { isAllTypesExpanded = false },
                    containerColor = Color.White,
                    modifier = Modifier
                        .dropShadow(Shape_15, shadow = Shadow(color =  theme_color_1, radius = 10.dp, offset = DpOffset(x = 5.dp, y = 5.dp)),)
                        .width(130.dp)
                        .background(Color.White, shape = Shape_20)
                )
                {
                    TransactionTypes.entries.forEach { option ->
                        val isSelected = option.type == uiState?.transactionType
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.type ?: "",
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFFD97706) else Color(0xFF334155)
                                )
                            },
                            onClick = {
                                viewModel.updateState { state ->
                                    state.copy(transactionType = option.type)
                                }
//                            onSelect(option)
                                isAllTypesExpanded = false
                                onSelectType()
                            }
                        )
                    }
                }
            }

        }

        AnimatedVisibility(!uiState?.fromDate.isNullOrEmpty() || !uiState?.toDate.isNullOrEmpty()) {
        uiState?.fromDate?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
                .clip(CircleShape)
            .background(color = Color.White, shape = CircleShape)
                .padding(horizontal = 15.dp, vertical = 4.dp)
            ) {

                Text(
                    "$it ${uiState.toDate?.let { "- $it" } ?: ""}",
                    color = theme_color_1,
                    fontFamily = HAFFER_TRIAL_MEDIUM,
                    fontSize = 12.sp,
                )
                Image(painterResource(R.drawable.baseline_close_24), contentDescription = null, colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(15.dp).clickable{
                      onClearDate()

                    })
            }
        }
            }
    }

}

@Composable
fun TransactionHistorySectionCard(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(Shape_15, shadow = Shadow(color = theme_color_1.copy(0.2f), radius = 15.dp))
            .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)
    ) {
        Text(
            "TODAY",
            letterSpacing = 1.2.sp,
            color = text_grey_1,
            fontSize = 12.sp,
            fontFamily = HAFFER_TRIAL_MEDIUM
        )

        repeat(3) {
            TransactionHistoryItem(item = null, modifier = Modifier.padding(top = 10.dp))
        }
    }

}

@Composable
fun TransactionHistoryItem(item: TransactionsListResponse.TransactionData?, modifier: Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val icon =
            if ((item?.display?.type?.lowercase()) == "gift") R.drawable.gift_iconbackground else R.drawable.added_icon
        Image(painter = painterResource(icon), contentDescription = null)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                item?.display?.title ?: "",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                modifier = Modifier
            )
            Text(
                (item?.display?.type ?: "").replaceFirstChar { it.uppercase() },
                color = text_grey_1,
                fontSize = 11.sp
            )
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    item?.display?.amount ?: "",
                    color = if (item?.type == "credit") green_8 else red_4,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
                Image(
                    painterResource(R.drawable.coin_addbackground_border),
                    contentDescription = null
                )
            }
            val time = item?.createdAt?.convertToDateFormat(toFormat = TIME_FORMAT_1)
            Text(time ?: "", color = text_grey_2, fontSize = 11.sp)
        }
    }
}
@Composable
private fun transactionShimmer(modifier: Modifier){

    Column(modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)) {
        repeat(15){
            ShimmerBox(shape = Shape_15, modifier = Modifier.fillMaxWidth()
                .height(80.dp))
        }
    }

}