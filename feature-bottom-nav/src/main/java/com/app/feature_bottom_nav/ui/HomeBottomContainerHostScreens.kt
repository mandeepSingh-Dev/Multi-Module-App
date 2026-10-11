package com.app.feature_bottom_nav.ui


import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.app.core_ui.R
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.UIRequirePermissions
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.feature_bottom_nav.navigation.HomeHostNavScreens
import com.app.feature_bottom_nav.navigation.HomeHostNavScreensTypes
import com.app.feature_bottom_nav.navigation.homeNavHostScreensGraph
import com.app.feature_home_host.ui.HostScreenHeader


@Composable
@Preview
fun HomeBottomContainerHostScreens(
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    homeBottomViewModel: HomeBottomViewModel = hiltViewModel(),
) {

    val uiState by homeBottomViewModel.uiState.collectAsStateWithLifecycle()

    val density = LocalDensity.current
    val bottomNavBackstack = rememberNavBackStack(HomeHostNavScreens.HomeScreen)


    var selectedIndex by remember { mutableStateOf(HomeHostNavScreensTypes.HOME_SCREEN.value) }

    val tabItems = remember {
        listOf(
            TabBarItem(
                icon = R.drawable.home_host_selected,
                unSelectedIcon = R.drawable.home_host_not_selected,
                title = "Dashboard",
                isSelected = selectedIndex == HomeHostNavScreensTypes.HOME_SCREEN.value,
                indexValue = HomeHostNavScreensTypes.HOME_SCREEN.value
            ), TabBarItem(
                icon = R.drawable.wallet_host_selected,
                unSelectedIcon = R.drawable.wallet_host_not_selected,
                title = "Earnings",
                isSelected = selectedIndex == HomeHostNavScreensTypes.EARNING_SCREEN.value,
                indexValue = HomeHostNavScreensTypes.EARNING_SCREEN.value
            ), TabBarItem(
                icon = R.drawable.message_selected,
                unSelectedIcon = R.drawable.message,
                title = "Message",
                isSelected = selectedIndex == HomeHostNavScreensTypes.CHAT_LIST_SCREEN.value,
                indexValue = HomeHostNavScreensTypes.CHAT_LIST_SCREEN.value
            ), TabBarItem(
                icon = R.drawable.profile_selected,
                unSelectedIcon = R.drawable.profile,
                title = "Profile",
                isSelected = selectedIndex == HomeHostNavScreensTypes.PROFILE_SETTINGS_SCREEN.value,
                indexValue = HomeHostNavScreensTypes.PROFILE_SETTINGS_SCREEN.value
            )
        )
    }

    LaunchedEffect(bottomNavBackstack.size) {
        selectedIndex = when (bottomNavBackstack.lastOrNull()) {
            HomeHostNavScreens.HomeScreen -> HomeHostNavScreensTypes.HOME_SCREEN
            HomeHostNavScreens.EarningsScreen -> HomeHostNavScreensTypes.EARNING_SCREEN
            HomeHostNavScreens.ChatScreen -> HomeHostNavScreensTypes.CHAT_LIST_SCREEN
            HomeHostNavScreens.ProfileScreen -> HomeHostNavScreensTypes.PROFILE_SETTINGS_SCREEN
            else -> HomeHostNavScreensTypes.HOME_SCREEN
        }.value
    }

    AttachLifecycleObserver { event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                homeBottomViewModel.getProfileData()
            }
            Lifecycle.Event.ON_RESUME -> {
                homeBottomViewModel.getUnreadNotificationCounts()
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

            HostScreenHeader(
                modifier = Modifier.padding(top = 10.dp),
                notificationCount = uiState?.notificationCount,
                name = uiState?.userData?.fullName ?: "",
                profilePic = uiState?.userData?.profilePic,
                onNavigate = {
                    backStack.add(it)
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
                    homeNavHostScreensGraph(bottomTabBarHeight, onEarningScreen = {
                        bottomNavBackstack.add(HomeHostNavScreens.EarningsScreen)
                    })
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
            onNavigate = { indexValue ->
                Log.d("fkbnfkbnf", indexValue.toString())
                val navKey = when (indexValue) {
                    HomeHostNavScreensTypes.HOME_SCREEN.value -> HomeHostNavScreens.HomeScreen
                    HomeHostNavScreensTypes.EARNING_SCREEN.value -> HomeHostNavScreens.EarningsScreen
                    HomeHostNavScreensTypes.CHAT_LIST_SCREEN.value -> HomeHostNavScreens.ChatScreen
                    HomeHostNavScreensTypes.PROFILE_SETTINGS_SCREEN.value -> HomeHostNavScreens.ProfileScreen
                    else -> HomeHostNavScreens.HomeScreen
                }
                bottomNavBackstack.add(navKey)

            })
    }

}

