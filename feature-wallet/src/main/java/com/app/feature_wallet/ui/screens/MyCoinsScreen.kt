package com.app.feature_wallet.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_navigation.AppScreens
import com.app.core_navigation.WalletScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.GradientButton
import com.app.core_ui.components.GradientText
import com.app.core_ui.components.HeaderView2
import com.app.core_ui.components.OutlineButton
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.padding
import com.app.core_ui.components.shimmerEffect
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brush4
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.grey_4
import com.app.feature_wallet.ui.viewmodels.WalletViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20


@Composable
fun MyCoinsScreen(
    snackBarHostState : SnackbarHostState = LocalSnackBarHostState.current,
    backStack : NavBackStack<NavKey> = LocalBackStackNav.current,
    walletViewModel: WalletViewModel= hiltViewModel()
) {

    val uiState by walletViewModel.uiState.collectAsStateWithLifecycle()

    AttachLifecycleObserver { event ->
        when(event) {
            Lifecycle.Event.ON_START -> {
                walletViewModel.getWallet()
                Log.d("fkbjkfbnf", "onStart")
            }
            else -> {
                Log.d("fkbjkfbnf", event.toString())
            }
        }
    }


    ObserveUIEvents(walletViewModel.uiEvent) { uIEvent ->
        when(uIEvent){
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }
        }
    }

    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            HeaderView2(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = "My Coins",
                secTitle = "Manage your coins and enjoy every moment.",
                 onNotification = {
                    backStack.add(AppScreens.NotificationListScreen)
                }
            )

            PullToRefreshBox(isRefreshing = uiState?.isRefreshing ?: false,
                onRefresh = {
                    walletViewModel.getWallet(isRefreshing = true)
                },
                modifier = Modifier.fillMaxWidth().weight(1f)) {


            Column(modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                val balance = uiState?.walletData?.balance
                val bonusCoins = uiState?.walletData?.bonusCoins
                val purchasedCoins = uiState?.walletData?.purchasedCoins
                BalanceCard(
                    modifier = Modifier.padding(horizontal = 20.dp, top = 20.dp),
                    balance = if(balance != null) (balance ?: 0).toString() else "" ,
                    onRecharge = {
                        walletViewModel.navigationEvent(WalletScreens.CoinsRechargeScreen)
                    },
                    onTransactionHistory = {
                        walletViewModel.navigationEvent(WalletScreens.TransactionsHistoryScreen)
                    },
                )

                MyCoinsCard(
                    modifier = Modifier.padding(top = 20.dp).padding(horizontal = 20.dp),
                    icon = R.drawable.bonusbackground,
                    title = "Bonus Coin",
                    desc = "Coins earned from offers, rewards and promotions",
                    coins = if(bonusCoins != null) (bonusCoins ?: 0).toString() else "" ,
                )
                MyCoinsCard(
                    modifier = Modifier.padding(top = 20.dp).padding(horizontal = 20.dp),
                    icon = R.drawable.purchasedbackground,
                    title = "Purchased Coins",
                    desc = "Coins you have purchased through recharges",
                    coins = if(purchasedCoins != null) (purchasedCoins ?: 0).toString() else "" ,
                )

                Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
            }

            }
        }

    }
}


@Composable
@Preview
fun BalanceCard(
    modifier: Modifier = Modifier,
    showButtons : Boolean = true,
    balance: String = "",
    onRecharge : () -> Unit = {},
    onTransactionHistory : () -> Unit = {},
    ){

    Column(modifier = modifier.fillMaxWidth()
        .border(width = 1.dp, brush = brush2(), shape = Shape_10)
        .background(brush = brush4(), shape = Shape_10)
        .padding(horizontal = 15.dp, vertical = 5.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)) {

            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GradientText(text = "Balance", fontSize = 17.sp)

                Row(
                    modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Image(
                        painterResource(R.drawable.balance_coinbackground_shadow),
                        contentDescription = null
                    )

                        AnimatedContent(balance){balance ->
                            if(balance.isNotEmpty()){
                                Text(balance, fontFamily = HAFFER_TRIAL_BOLD, fontSize = 22.sp)
                            }else{
                                ShimmerBox(shape = Shape_20, modifier = Modifier.width(40.dp).height(10.dp))
                            }
                        }
                }
            }
            Image(
                painterResource( R.drawable.wallet_imgimage51),
                contentDescription = null,
            )
        }
        if(showButtons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                GradientButton(
                    onClick = onRecharge,
                    text = "Recharge",
                    modifier = Modifier.weight(1f)
                )

                OutlineButton(
                    onClick = onTransactionHistory,
                    text = "Transaction history",
                    isBorder = true,
                    fontColor = Color.Black,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }

}

@Composable
private fun MyCoinsCard(modifier: Modifier,
                        icon : Int,
                        title : String,
                        desc : String,
                        coins : String){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier.fillMaxWidth()
            .height(IntrinsicSize.Max)
        .dropShadow(Shape_15, shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp))
        .clip(Shape_15)
        .background(color = Color.White, shape = Shape_15)
            .padding(15.dp)) {

        Image(painterResource(icon), contentDescription = null)
        Column(modifier = Modifier.weight(1f).padding(start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, color = Color.Black,
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = 15.sp)
                Image(painter = painterResource(R.drawable.baseline_info_24), contentDescription = null,
                    modifier = Modifier.size(15.dp))
            }
            Text(desc, fontSize = 11.sp,
                color = grey_4,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        VerticalDivider(thickness = 1.dp, color = divider_color_1, modifier = Modifier.fillMaxHeight().padding(vertical = 5.dp, horizontal = 8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)) {
            AnimatedContent(coins){coins ->
                if(coins.isNotEmpty()){
                    Text(coins, color = Color.Black, fontSize = 18.sp,
                        fontFamily = HAFFER_TRIAL_BOLD
                    )                }else {
                    ShimmerBox(shape = Shape_20, modifier = Modifier.width(30.dp).height(10.dp))
                }
            }
            Text("COINS", color = Color.Black, fontSize = 12.sp, letterSpacing = 1.2.sp
            )
        }
        Image(painterResource(R.drawable.baseline_arrow_forward_ios_24,), contentDescription = null)
    }

}