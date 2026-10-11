package com.app.feature_bottom_nav.ui

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_navigation.AppScreens
import com.app.core_navigation.UserScreens
import com.app.core_navigation.WalletScreens
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.UIRequirePermissions
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.ui.theme.brush2
import com.app.core_ui.ui.theme.brushTransparent
import com.app.feature_bottom_nav.components.HomeHeader
import com.app.feature_bottom_nav.navigation.homeNavUserScreensGraph
 import com.app.feature_bottom_nav.navigation.HomeHostNavScreensTypes
import com.app.feature_bottom_nav.navigation.HomeUserNavScreens
import com.app.feature_bottom_nav.navigation.HomeUserNavScreensTypes

val permissions = arrayOf(
    Manifest.permission.POST_NOTIFICATIONS,
)
@Composable
@Preview
fun HomeBottomContainerScreens(
    backstack : NavBackStack<NavKey> = LocalBackStackNav.current,
    homeBottomViewModel: HomeBottomViewModel = hiltViewModel(),
) {

    val density = LocalDensity.current
    val bottomNavBackstack = rememberNavBackStack(HomeUserNavScreens.HomeScreen)

    val uiState by homeBottomViewModel.uiState.collectAsStateWithLifecycle()

    var selectedIndex by remember { mutableStateOf(HomeHostNavScreensTypes.HOME_SCREEN.value) }

    val tabItems = remember {
        listOf(
            TabBarItem(
                icon = R.drawable.home_iconhome,
                unSelectedIcon = R.drawable.home_iconhome,
                title = "Home",
                isSelected = selectedIndex == HomeUserNavScreensTypes.HOME_SCREEN.value,
                indexValue = HomeUserNavScreensTypes.HOME_SCREEN.value
            ), TabBarItem(
                icon = R.drawable.search_selected,
                unSelectedIcon = R.drawable.search,
                title = "Search",
                isSelected = selectedIndex == HomeUserNavScreensTypes.SEARCH_SCREEN.value,
                indexValue = HomeUserNavScreensTypes.SEARCH_SCREEN.value
            ), TabBarItem(
                icon = R.drawable.message_selected,
                unSelectedIcon = R.drawable.message,
                title = "Message",
                isSelected = selectedIndex == HomeUserNavScreensTypes.CHAT_LIST_SCREEN.value,
                indexValue = HomeUserNavScreensTypes.CHAT_LIST_SCREEN.value
            ), TabBarItem(
                icon = R.drawable.profile_selected,
                unSelectedIcon = R.drawable.profile,
                title = "Profile",
                isSelected = selectedIndex == HomeUserNavScreensTypes.PROFILE_SETTINGS_SCREEN.value,
                indexValue = HomeUserNavScreensTypes.PROFILE_SETTINGS_SCREEN.value
            )
        )
    }


    LaunchedEffect(bottomNavBackstack.size) {
        selectedIndex = when (bottomNavBackstack.lastOrNull()) {
            HomeUserNavScreens.HomeScreen -> HomeUserNavScreensTypes.HOME_SCREEN
            is HomeUserNavScreens.HomeSearchScreen -> HomeUserNavScreensTypes.SEARCH_SCREEN
            HomeUserNavScreens.ChatScreen -> HomeUserNavScreensTypes.CHAT_LIST_SCREEN
            HomeUserNavScreens.ProfileScreen -> HomeUserNavScreensTypes.PROFILE_SETTINGS_SCREEN
            else -> HomeUserNavScreensTypes.HOME_SCREEN
        }.value
    }

    AttachLifecycleObserver { event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                homeBottomViewModel.getProfileData()
            }

            Lifecycle.Event.ON_RESUME -> {
                homeBottomViewModel.getUnreadNotificationCounts()
                homeBottomViewModel.getWallet()
            }
            else -> Unit
        }
    }

    var bottomTabBarHeight by remember { mutableStateOf(100.dp) }


    var manualPermissionsLauncher by remember { mutableStateOf<(() -> Unit)?>(null) }

    UIRequirePermissions(
        permissions = permissions,
        onPermissionGranted = {
        },
        onPermissionDenied = {
        }, manualLaunchPermissionsOnClick = { trigger ->
            manualPermissionsLauncher = trigger
        })


    BoxCommon(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            HomeHeader(
                profilePic = uiState?.userData?.profilePic,
                notificationCount = uiState?.notificationCount,
                balance = uiState?.walletData?.balance,
                isWalletLoading = uiState?.isWalletLoading,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .onSizeChanged {
                        Log.d("fknbjfnbf", it.height.toString())
                    },
                onClickProfile = {
                    backstack.add(UserScreens.AuthScreens.ProfileUpdateScreen)
//                    backstack.add()
                }, onClickCoins = {
                    backstack.add(WalletScreens.MyCoinsScreen)
                },
                onNotificationClick = {
                    backstack.add(AppScreens.NotificationListScreen)
                })

            NavDisplay(
                modifier = Modifier.weight(1f),
                backStack = bottomNavBackstack,
                onBack = {
                    if (bottomNavBackstack.size > 1) bottomNavBackstack.removeLastOrNull()
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator() // This enables per-screen ViewModels
                ),

                entryProvider = entryProvider {
                    homeNavUserScreensGraph(bottomTabBarHeight,bottomNavBackstack)
                })
        }


        TabBar(
            list = tabItems,
            selectedIndex = selectedIndex,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onSizeChanged {
                    bottomTabBarHeight = with(density) {
                        it.height.toDp()
                    }
                },
            onNavigate = {indexValue ->
                val navKey = when (indexValue) {
                     HomeUserNavScreensTypes.HOME_SCREEN.value ->  HomeUserNavScreens.HomeScreen
                    HomeUserNavScreensTypes.SEARCH_SCREEN.value -> HomeUserNavScreens.HomeSearchScreen()
                    HomeUserNavScreensTypes.CHAT_LIST_SCREEN.value -> HomeUserNavScreens.ChatScreen
                    HomeUserNavScreensTypes.PROFILE_SETTINGS_SCREEN.value -> HomeUserNavScreens.ProfileScreen
                    else -> HomeUserNavScreens.HomeScreen
                }
                bottomNavBackstack.add(navKey)
            })
    }

}


data class TabBarItem(
    val icon: Int,
    val unSelectedIcon: Int,
    val title: String,
    val isSelected: Boolean,
    val indexValue: Int
)


@Composable
fun TabBar(
    list: List<TabBarItem>,
    selectedIndex: Int,
    modifier: Modifier,
    onNavigate: (Int) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .animateContentSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.horizontalGradient(
                    0.2f to Color.Black.copy(alpha = 0.4f), 1.0f to Color.Black.copy(0.4f)
                )
            )
            .padding(15.dp)
    ) {

        val modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 300))


        list.forEach {
            TabItem(
                modifier = modifier,
                icon = it.icon,
                unSelectedIcon = it.unSelectedIcon,
                title = it.title,
                isSelected = selectedIndex == it.indexValue,
                onClick = {
                    onNavigate(it.indexValue)
                })
        }
    }

}

@Composable
fun TabItem(
    modifier: Modifier,
    icon: Int,
    unSelectedIcon: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            })
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .background(
                    brush = if (isSelected) brush2() else brushTransparent(),
                    shape = CircleShape
                )
                .padding(8.dp)
        ) {
            if (isSelected) {
                Image(painterResource(icon), contentDescription = null)
            } else {
                Image(painterResource(unSelectedIcon), contentDescription = null)
            }

            AnimatedVisibility(
                isSelected,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                Text(title, fontSize = 11.sp)
            }
        }
    }
}