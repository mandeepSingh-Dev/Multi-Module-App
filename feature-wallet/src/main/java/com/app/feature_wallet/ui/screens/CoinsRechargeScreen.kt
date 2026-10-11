package com.app.feature_wallet.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.app.core_navigation.AppScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.padding
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brushFirstRecharge
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.purple
import com.app.core_ui.ui.theme.purple_2
import com.app.core_ui.ui.theme.purple_light
import com.app.core_ui.ui.theme.text_grey_2
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_14
import com.app.feature_wallet.remote.model.response.CoinPacksListResponse
import com.app.feature_wallet.ui.WalletRechargeScreenEvents
import com.app.feature_wallet.ui.viewmodels.CoinsRechargeViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_5


@Composable
@Preview
fun CoinsRechargeScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    coinsRechargeViewModel: CoinsRechargeViewModel = hiltViewModel()
) {

    val uiState by coinsRechargeViewModel.uiState.collectAsStateWithLifecycle()

    var showRechargeSuccessDialog by remember { mutableStateOf(false) }

    val selectedCoinPack by remember {
        derivedStateOf {
            uiState?.coinPacksListData?.find { it?.isSelected ?: false }
        }
    }

    ObserveUIEvents(coinsRechargeViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }
            is WalletRechargeScreenEvents.WalletRechargeSuccess -> {
                showRechargeSuccessDialog = true
            }
        }
    }

    BoxCommon(modifier = Modifier.fillMaxSize().blur(if(showRechargeSuccessDialog) 10.dp else 0.dp)) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            HeaderView2(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = "Transaction History",
                secTitle = "View your coin earnings, spending and transaction details",
                 onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }     )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            )
            {
                item {
                    val balance = uiState?.walletData?.balance

                    BalanceCard(
                        showButtons = false, modifier = Modifier.padding(top = 10.dp),
                        balance = if (balance != null) (balance ?: 0).toString() else "",
                    )
                }
                item {
                    FirstRechargeOfferCard(modifier = Modifier.padding(top = 10.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (uiState?.isLoading == true) {
                    item {
                        coinRechargesShimmer(modifier = Modifier)
                    }
                } else {
                    items(uiState?.coinPacksListData ?: emptyList()) { pack ->
                        CoinRechargeItem(pack, modifier = Modifier, onClick = {
                            coinsRechargeViewModel.updateList(pack)
                        })
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 10.dp, horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(Shape_15)
                    .background(color = purple_light, Shape_15)
                    .padding(10.dp)
            ) {
                Image(painterResource(R.drawable.securebackground), contentDescription = null)
                Text(
                    "Secure payments. 100% safe & trusted.",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }


            AnimatedContent(uiState?.isRecharging) { isRecharging ->
                if (isRecharging == true) {
                    CircularProgressIndicator()
                } else {
                    GradientButton(
                        onClick = {
                            coinsRechargeViewModel.rechargeWallet(selectedCoinPack?.id)
                        },
                        text = "Proceed to Pay",
                        showEndIcon = true,
                        enabled = selectedCoinPack != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, top = 10.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(top = 10.dp, horizontal = 10.dp)
            ) {
                Image(painterResource(R.drawable.lock_smallsymbol), contentDescription = null)
                Text("Payments are secure & encrypted", fontSize = 10.sp, color = grey_10)
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

    }

    ButtonsDialog(
        showDialog = showRechargeSuccessDialog,
        onDismiss = {
            showRechargeSuccessDialog = false
        },
        icon = R.drawable.coins_stack_iconimage52,
        showBothButtons = false,
        title = "Coins Added Successfully!",
        description = "Your coins have been added to your wallet successfully.",
        positiveBtnText = "Done",
        onPositiveClick = {
            showRechargeSuccessDialog = false
            backStack.removeLastOrNull()
        })
}


@Composable
private fun FirstRechargeOfferCard(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = purple_2.copy(alpha = 0.2f), shape = Shape_10)
            .background(brush = brushFirstRecharge(), shape = Shape_10)
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {

        Box(contentAlignment = Alignment.Center) {
            Image(
                painterResource(R.drawable.gift_rechargeimage53), contentDescription = null,
            )
        }

        Column(modifier = Modifier) {
            Text("First Recharge Offer", color = purple, fontSize = 16.sp)
            Text("Upto", fontFamily = HAFFER_TRIAL_MEDIUM)
            Image(
                painterResource(R.drawable._50_offgroup1261162548), contentDescription = null,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text("On your first coin recharge", modifier = Modifier.padding(top = 5.dp))
        }

        Image(painterResource(R.drawable.percentage_iconvector), contentDescription = null)

    }

}

@Composable
private fun CoinRechargeItem(
    pack: CoinPacksListResponse.CoinPacksListData?, modifier: Modifier,
    onClick: () -> Unit
) {

    val isSelected = pack?.isSelected ?: false

    val bgColor by animateColorAsState(if (isSelected) theme_color_14 else Color.White)
    val radius by animateDpAsState(if (isSelected) 10.dp else 20.dp)
    val alpha by animateFloatAsState(if (isSelected) 0.2f else 0.05f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(Shape_15, shadow = Shadow(color = Color.Black.copy(alpha), radius = radius))
            .clip(Shape_15)
            .clickable {
                onClick()
            }
            .background(color = bgColor, shape = Shape_15)
    ) {

        pack?.isPopular.takeIf { it == true }?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        brush = brush2(),
                        shape = RoundedCornerShape(bottomEnd = 12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    "Most Popular", fontSize = 11.sp, color = Color.White,
                    letterSpacing = 1.2.sp, fontFamily = HAFFER_TRIAL_SEMI_BOLD
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 5.dp, bottom = 12.dp)
        ) {
            Text(
                (pack?.price ?: 0).toString(),
                fontSize = 18.sp,
                fontFamily = HAFFER_TRIAL_SEMI_BOLD
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painterResource(R.drawable.coins_stack_iconimage52),
                    contentDescription = null
                )
                Column {
                    Text(
                        (pack?.coins ?: 0).toString(),
                        fontFamily = HAFFER_TRIAL_BOLD,
                        fontSize = 17.sp
                    )
                    Text("Coins", fontSize = 12.sp, color = text_grey_3)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {


                val discount = if (pack?.discountType == "percentage") {
                    val discountPercentage = pack.firstRechargeOffer?.discountPercent ?: 0
                    discountPercentage.takeIf { it != 0 }?.let {
                        "${it}% OFF"
                    }
                } else {
                    val discountAmount = pack?.firstRechargeOffer?.discountAmount ?: 0
                    discountAmount.takeIf { it != 0 }?.let {
                        "₹${it}"
                    }
                }
                discount?.let {
                    Text(
                        it,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = if(pack?.isPopular == false) 5.dp else 0.dp)
                            .background(color = theme_color_11, shape = Shape_5)
                            .padding(horizontal = 5.dp, vertical = 3.dp),
                        fontSize = 10.sp
                    )
                }
                Text(
                    "₹${(pack?.firstRechargeOffer?.payablePrice ?: 0)}",
                    color = text_grey_2,
                    fontSize = 10.sp
                )
                (pack?.firstRechargeOffer?.discountAmount ?: 0).takeIf { it !=0 }?.let {
                    Text(
                        "You save ₹${it}", fontSize = 11.sp,
                        color = green_4, fontFamily = HAFFER_TRIAL_MEDIUM
                    )
                }
            }

            AnimatedContent(isSelected) { isSelected ->
                if (isSelected) {
                    Image(
                        painterResource(R.drawable.circle_selected__input),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Image(
                        painterResource(R.drawable.circle_selectorellipse5),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun coinRechargesShimmer(modifier: Modifier) {

    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        repeat(4) {
            ShimmerBox(
                shape = Shape_15, modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }

}
