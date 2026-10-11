package com.app.kahoji

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.app.core_common.utils.DeepLinkHandler
import com.app.core_common.utils.SocketHostEventsHandler
import com.app.core_common.utils.deepLinkHandler
import com.app.core_navigation.AppScreens
import com.app.core_ui.components.padding
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.KahoJiTheme
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_2
import com.app.feature_auth_host.navigation.authHostGraph
import com.app.feature_auth_user.navigation.authGraph.authGraph
import com.app.feature_block.navigation.withdrawGraph.blockScreensGraph
import com.app.feature_bottom_nav.navigation.homeBottomNavGraph
import com.app.feature_call.navigation.callGraph
import com.app.feature_change_password.navigation.authGraph.changePwdGraph
import com.app.feature_chat.navigation.authGraph.chatGraph
import com.app.feature_cms.navigation.cmsGraph
import com.app.feature_gift.ui.GiftNotificationCard
import com.app.feature_help_support.navigation.helpSupportGraph
import com.app.feature_home.navigation.authGraph.homeUserGraph
import com.app.feature_home_host.navigation.authGraph.hostScreensGraph
import com.app.feature_leaderboard.navigation.leaderboardGraph
import com.app.feature_notification.navigation.notificationGraph
import com.app.feature_onboarding.navigation.onboardingGraph
import com.app.feature_referral.navigation.referralGraph
import com.app.feature_review.navigation.reviewGraph
import com.app.feature_splash.navigation.splashGraph
import com.app.feature_story.navigation.storiesGraph
import com.app.feature_wallet.navigation.authGraph.walletUserGraph
import com.app.feature_withdraw.navigation.withdrawGraph.withdrawScreensGraph
import com.uae.core_network.data.service.CommonService
import com.zodiaq.ui.theme.Shape_20
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntSize
import androidx.navigation3.scene.Scene
import com.app.kahoji.premiumPopTransitionSpec

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    @Inject
    lateinit var commonService : CommonService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        CryptoManager.generateKeysIfNeeded()


//        NavigationProvider.callNavigator.startActivity(
//            this,
//            chatID = "6a9eaf7bcf2620939fd7648b",
//            partnerID = "6a9a832a6e02e433e9ff23a4"
//        )

//        lifecycleScope.launch {
//            delay(2000.milliseconds)
//            LoginRequestBody(phone = "9876543210", countryCode = "+91", role = 1).let {
//                authRepository.login(it)?.collect {
//                    Log.d("knbkbf", it.toString())
//                }
//            }
//        }

//        lifecycleScope.launch {
//
//            val body = hashMapOf(
//                "phone" to "9876543210",
//            "countryCode" to "+91",
//            "role" to "1"
//            )
//            delay(5000.milliseconds)
//            try {
//                val result = commonService.login(body)
//                Log.d("fkbnfkbnf", result.body()?.toString().toString())
//            }catch (e: Exception){
//            }
//        }

//        CryptoManager.getClientEncryptionPublicKey()
//        ClientKeyManager.getEncryptionKeyPair()

        setContent {

            val metrics = LocalWindowInfo.current

            val widthPx = metrics.containerSize.width
            val heightPx = metrics.containerSize.height
            Log.d("knbkfnbf", "${widthPx} - $heightPx")

            KahoJiTheme {
                val backstack = rememberNavBackStack(AppScreens.SplashScreen)
                val snackBarHostState = remember { SnackbarHostState() }
                CompositionLocalProvider(
                    LocalSnackBarHostState provides snackBarHostState,
                    LocalBackStackNav provides backstack,
                ) {

                    deepLinkHandler = retain { object : DeepLinkHandler{
                        override fun navigateToLoginScreen() {
                        }
                    } }

                    val receivedGift by SocketHostEventsHandler.receiveGift.collectAsStateWithLifecycle(null)
                    var showGiftNotification by remember(receivedGift) { mutableStateOf(receivedGift ) }

                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackBarHostState) {
                                Snackbar(
                                    shape = Shape_20,
                                    snackbarData = it,
                                    dismissActionContentColor = theme_color_1,
                                    containerColor = theme_color_1,
                                    contentColor = Color.White,
                                    actionColor = theme_color_2,
                                    actionOnNewLine = true,
                                )
                            }

                        },modifier = Modifier.fillMaxSize()) { innerPadding ->
                        innerPadding


                        NavDisplay(
                            backStack = backstack,
                            onBack = {
                                if (backstack.size > 1) backstack.removeLastOrNull()
                            },
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator() // This enables per-screen ViewModels
                            ),
                            transitionSpec = {
                                fadeIn(
                                    animationSpec = tween(300)
                                ) togetherWith fadeOut(
                                    animationSpec = tween(300)
                                )
                            },
                            predictivePopTransitionSpec = iosPredictivePopTransitionSpec(),
                            popTransitionSpec = {
                                fadeIn(
                                    animationSpec = tween(300)
                                ) togetherWith fadeOut(
                                    animationSpec = tween(300)
                                )
                            },
                            entryProvider = entryProvider {
                                splashGraph()
                                onboardingGraph()

                                //USER GRAPHS
                                authGraph()
                                homeUserGraph()
                                homeBottomNavGraph()

                                //HOST GRAPHS
                                authHostGraph()

                                chatGraph()

                                walletUserGraph()
                                callGraph()
                                referralGraph()

                                hostScreensGraph()
                                withdrawScreensGraph()
                                blockScreensGraph()
                                changePwdGraph()
                                notificationGraph()
                                reviewGraph()
                                leaderboardGraph()
                                storiesGraph()

                                helpSupportGraph()
                                cmsGraph()
                            })
                    }

                        GiftNotificationCard(
                            showGiftNotification,
                            onDismiss = { showGiftNotification = null },
                            modifier = Modifier.systemBarsPadding()
                                .padding(horizontal = 20.dp, top = 10.dp))
                }
            }
        }
    }
}


public fun <T : Any> spatialElevationPredictivePopSpec(): AnimatedContentTransitionScope<Scene<T>>.(
    swipeEdge: Int
) -> ContentTransform = {
    val exitAnimation = slideOutHorizontally(
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth -> (fullWidth * 1.1f).toInt() }
    ) + scaleOut(
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        targetScale = 0.88f
    ) + fadeOut(
        animationSpec = tween(durationMillis = 250)
    )

    val enterAnimation = scaleIn(
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        initialScale = 0.94f
    ) + fadeIn(
        animationSpec = tween(durationMillis = 280)
    )

    enterAnimation togetherWith exitAnimation
}

public fun <T : Any> iosPredictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(
    swipeEdge: Int
) -> ContentTransform = {
    // Top screen slides right smoothly while scaling down slightly on release
    val exitAnimation = slideOutHorizontally(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth -> fullWidth }
    ) + scaleOut(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        targetScale = 0.96f
    ) + fadeOut(animationSpec = tween(durationMillis = 200))

    // Background destination subtly scales up to 100% and fades in fully
    val enterAnimation = slideInHorizontally(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        initialOffsetX = { fullWidth -> -fullWidth / 3 }
    ) + scaleIn(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        initialScale = 0.92f
    ) + fadeIn(animationSpec = tween(durationMillis = 300))

    enterAnimation togetherWith exitAnimation
}

public fun <T : Any> premiumForwardTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    val enterAnimation = slideInHorizontally(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        initialOffsetX = { fullWidth -> fullWidth }
    ) + scaleIn(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        initialScale = 0.96f
    ) + fadeIn(animationSpec = tween(durationMillis = 200))

    val exitAnimation = slideOutHorizontally(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth -> -fullWidth / 3 }
    ) + scaleOut(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        targetScale = 0.92f
    ) + fadeOut(animationSpec = tween(durationMillis = 300))

    enterAnimation togetherWith exitAnimation
}


public fun <T : Any> premiumPopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    val enterAnimation = slideInHorizontally(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        initialOffsetX = { fullWidth -> -fullWidth / 3 }
    ) + scaleIn(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        initialScale = 0.92f
    ) + fadeIn(animationSpec = tween(durationMillis = 300))

    val exitAnimation = slideOutHorizontally(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        targetOffsetX = { fullWidth -> fullWidth }
    ) + scaleOut(
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        targetScale = 0.96f
    ) + fadeOut(animationSpec = tween(durationMillis = 200))

    enterAnimation togetherWith exitAnimation
}

public fun premiumSizeTransform(): SizeTransform = SizeTransform(
    clip = true,
    sizeAnimationSpec = { initialSize: IntSize, targetSize: IntSize ->
        spring(
            dampingRatio = Spring.DampingRatioLowBouncy, // Subtle natural spring elasticity
            stiffness = Spring.StiffnessMediumLow         // Smooth fluid feel
        )
    }
)