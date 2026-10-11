package com.app.feature_withdraw.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_auth.utils.SignUpHostSteps
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_navigation.HostScreens
import com.app.core_navigation.WithDrawScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.TextFieldOuterLabel
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP_MEDIUM
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brush9
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.grey_7
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.text_grey_5
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_4
import com.app.core_ui.ui.theme.theme_color_7
import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse
import com.app.feature_withdraw.ui.events.WithdrawBeansScreenEvents
import com.app.feature_withdraw.ui.states.WithdrawBeanUIState
import com.app.feature_withdraw.ui.viewmodels.WithdrawBeansViewModel
import com.zodiaq.ui.theme.Shape_12
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun WithdrawBeansScreens(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    withdrawBeansViewModel: WithdrawBeansViewModel = hiltViewModel()
) {

    val uiState by withdrawBeansViewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    var showComponents by rememberSaveable { mutableStateOf(false) }

    var showWithdrawSuccessDialog by remember { mutableStateOf(false) }

    ObserveUIEvents(withdrawBeansViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

            is WithdrawBeansScreenEvents.GoToVerificationScreen -> {
                backStack.add(WithDrawScreens.IdentityVerificationScreen)
            }

            is WithdrawBeansScreenEvents.WithdrawRequestSuccess -> {
                showWithdrawSuccessDialog = true
            }
        }

    }

    BoxCommon(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .blur(radius = if (showWithdrawSuccessDialog) 10.dp else 0.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        )
        {
            HeaderView2(
                modifier = Modifier.padding(top = 10.dp, horizontal = 20.dp),
                title = "Withdrawal Beans",
                secTitle = "Convert your beans into real earnings"
                , onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }       )

            PullToRefreshBox(
                isRefreshing = /*uiState?.isRefreshing ?:*/ false,
                onRefresh = {
//                    homeHostViewModel.getDashboardStats(isRefreshing = true)
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

                    AvailableBeansCard(
                        uiState = uiState,
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
                    )

                    WithdrawMethodCard(
                        modifier = Modifier.padding(top = 20.dp, horizontal = 20.dp)
                            .clickableDebAnim{
                                backStack.add(HostScreens.AuthHostScreens.SignUpHostScreen(
                                    phone = null,
                                    countryCode = null,
                                    step = SignUpHostSteps.BANK_DETAILS.value.minus(1),
                                    isForUpdate = true
                                ))
                            },
                        uiState
                    )
                    BeansPackagesSection(
                        modifier = Modifier.padding(top = 20.dp, horizontal = 20.dp),
                        uiState,
                        onSelect = {
                            withdrawBeansViewModel.selectBean(it)
                        }
                    )
                    EnterCustomBeanAmount(
                        modifier = Modifier.padding(top = 20.dp, horizontal = 20.dp),
                        amount = if (uiState?.selectedAmount != null) uiState?.selectedAmount.toString() else "",
                        onAmountChange = {
                            withdrawBeansViewModel.updateAmount(it)

                        }
                    )

                    AnimatedVisibility(uiState?.selectedAmount != null) {
                        TotalPayableAmountCard(
                            amount = uiState?.selectedAmount.toString(),
                            modifier = Modifier.padding(top = 20.dp, horizontal = 20.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }


            AnimatedVisibility(
                uiState?.isLoading == true || uiState?.isWithdrawLoading == true,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }

            AnimatedVisibility(
                uiState?.isLoading == false && uiState?.isWithdrawLoading == false,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                GradientButton(
                    onClick = {
                        withdrawBeansViewModel.checkWithdrawVerificationStatus()
                    },
                    text = "Withdraw Now",
                    enabled = uiState?.selectedAmount != null,
                    endIcon = R.drawable.right_arrow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 15.dp, horizontal = 20.dp)
                )
            }
        }
    }

    ButtonsDialog(
        showDialog = showWithdrawSuccessDialog,
        onDismiss = {
            showWithdrawSuccessDialog = false
        },
        showBothButtons = false,
        icon = R.drawable.bean_iconimage86,
        title = "Withdrawal Successful 🎉",
        description = "Your beans have been successfully converted and the amount has been transferred to your bank account.",
        positiveBtnText = "Done",
        onPositiveClick = {
            showWithdrawSuccessDialog = false
            backStack.removeLastOrNull()
        },
    )
}


@Composable
fun AvailableBeansCard(
    modifier: Modifier,
    uiState: WithdrawBeanUIState?,
) {


    val data = uiState?.withdrawSummaryData

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
                    "Available Beans", fontFamily = HAFFER_TRIAL_MEDIUM, fontSize = 13.sp,
                    color = grey_1
                )

                AnimatedContent(uiState?.isLoading ?: false) { isLoading ->
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
                            data?.beans?.let {
                                Text(
                                    "${data.beans ?: 0}",
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
        }

        AnimatedContent(
            uiState?.isLoading ?: false,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp)
        ) { isLoading ->
            if (isLoading ?: false) {
                ShimmerBox(
                    modifier = Modifier
                        .width(60.dp)
                        .height(20.dp)
                )
            } else {
                data?.rupees?.let {
                    Text(
                        "≈ ₹${data.rupees ?: 0}",
                        color = text_grey_3,
                        fontFamily = HAFFER_TRIAL_SEMI_BOLD
                    )
                }
            }
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
private fun WithdrawMethodCard(
    modifier: Modifier,
    uiState: WithdrawBeanUIState?
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(shape = Shape_15)
            .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Withdrawal Method", fontSize = 13.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.banj_with_bgbankiconbox),
                contentDescription = null
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Bank Account", color = Color.Black, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
                AnimatedContent(uiState?.isLoading ?: false) { isLoading ->
                    if (isLoading) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(30.dp)
                                .height(20.dp)
                        )
                    } else {
                        val bankDesc =
                            "${uiState?.withdrawSummaryData?.bankDetail?.bankName ?: ""} ${uiState?.withdrawSummaryData?.bankDetail?.accountNumber ?: ""}"
                        Text(bankDesc, color = text_grey_5, fontSize = 12.sp)
                    }
                }
            }
            Image(
                painterResource(R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun BeansPackagesSection(
    modifier: Modifier, uiState: WithdrawBeanUIState?,
    onSelect: (BeanPacksListResponse.BeanPackData?) -> Unit
) {

    val isLoading = uiState?.isBeansPackListLoading ?: false

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            "Enter Beans Amount",
            fontSize = HEADING_ROW_SP_MEDIUM,
            fontFamily = HAFFER_TRIAL_SEMI_BOLD,
            modifier = Modifier.weight(1f)
        )
        Text("Convert to INR", fontSize = 13.sp, color = theme_color_11)
    }

    AnimatedContent(isLoading, modifier = modifier) { isLoading ->

        if (isLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                repeat(3) {
                    ShimmerBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp),
                        shape = Shape_15
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                uiState?.beanPackData?.let {
                    it.chunked(3).forEach {
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            it.forEach {
                                BeanCard(
                                    modifier = Modifier.weight(1f),
                                    data = it,
                                    isSelected = it?.isSelected ?: false,
                                ) {
                                    onSelect(it)
                                }

                            }
                            if (it.size < 2) {
                                Box(modifier = Modifier.weight(1f))
                                Box(modifier = Modifier.weight(1f))
                            } else if (it.size < 3) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }

                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                    Text("OR", color = grey_4)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(color = grey_7, shape = CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
private fun BeanCard(
    modifier: Modifier,
    data: BeanPacksListResponse.BeanPackData? = null,
    isSelected: Boolean = false,
    onSelect: () -> Unit
) {


    val bgColor by animateColorAsState(if (isSelected) theme_color_7 else Color.White)

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .fillMaxWidth()
            .dropShadowCustom(shape = Shape_12)
            .then(
                if (isSelected) {
                    Modifier.border(width = 2.dp, brush = brush2(), shape = Shape_12)
                } else {
                    Modifier
                }
            )
            .clip(Shape_12)
            .background(color = bgColor, Shape_12)
            .clickable {
                onSelect()
            }
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {

        Image(painterResource(R.drawable.bean_iconimage86), contentDescription = null)

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
        ) {
            data?.beans?.let {
                Text((it ?: 0).toString(), fontFamily = HAFFER_TRIAL_SEMI_BOLD)
            }
            Text(data?.price ?: "", fontSize = 12.sp)
        }
    }
}

@Composable
private fun EnterCustomBeanAmount(
    modifier: Modifier,
    amount: String?,
    onAmountChange: (String) -> Unit
) {
    Text(
        "Custom Amount",
        fontSize = HEADING_ROW_SP_MEDIUM,
        fontFamily = HAFFER_TRIAL_SEMI_BOLD,
        modifier = modifier
    )

    TextFieldOuterLabel(
        modifier = Modifier.padding(top = 10.dp, horizontal = 20.dp),
        value = amount ?: "",
        onValueChange = {
            if (it.isDigitsOnly() || it.isEmpty()) {
                onAmountChange(it)
            }
        },
        label = "Beans Amount",
        leadingIcon = {
            Image(painterResource(R.drawable.bean_kidneyframe5), contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        hint = "Enter beans amount",
        isBorder = true
    )
}

@Composable
fun TotalPayableAmountCard(
    modifier: Modifier,
    amount: String?
) {

    Column(
        modifier = modifier
            .dropShadowCustom(shape = Shape_15)
            .background(Color.White, shape = Shape_15)
            .padding(15.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("You will receive", color = text_grey_2, modifier = Modifier.weight(1f))
            Text(amount ?: "", color = Color.Black, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }
        HorizontalDivider(
            thickness = 1.dp, color = divider_color_1, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Processing Fee", color = text_grey_2, modifier = Modifier.weight(1f))
            Text("₹0.0", color = Color.Black, fontFamily = HAFFER_TRIAL_SEMI_BOLD)
        }
        HorizontalDivider(
            thickness = 1.dp, color = divider_color_1, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Text(
                "Total Payable",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                color = theme_color_11,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                amount ?: "",
                color = theme_color_11,
                fontSize = 15.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
        }
    }
}