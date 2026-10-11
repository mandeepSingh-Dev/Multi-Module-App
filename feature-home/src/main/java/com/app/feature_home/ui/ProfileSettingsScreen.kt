package com.app.feature_home.ui

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.AttachLifecycleObserver
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.AppScreens
import com.app.core_navigation.BlockScreens
import com.app.core_navigation.CallScreens
import com.app.core_navigation.HostScreens
import com.app.core_navigation.ReferralScreens
import com.app.core_navigation.UserScreens
import com.app.core_navigation.WalletScreens
import com.app.core_navigation.WithDrawScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.brush3
import com.app.core_ui.ui.theme.brushSilver
import com.app.core_ui.ui.theme.divider_color_1
import com.app.core_ui.ui.theme.text_grey_3
import com.app.core_ui.ui.theme.text_grey_4
import com.app.core_ui.ui.theme.theme_color_10
import com.app.core_ui.ui.theme.theme_color_11
import com.app.feature_home.ui.events.ProfileSettingsScreenEvents
import com.app.feature_home.ui.viewmodels.ProfileSettingsViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.material3.Button
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import com.app.core_common.utils.enums.CMSType
import com.app.core_ui.ui.theme.green_8
import com.app.core_ui.ui.theme.theme_color_20
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ProfileSettingsScreen(
    bottomTabBarHeight: Dp,
    userType : Int,
    profileSettingsViewModel: ProfileSettingsViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current
) {

    val uiState by profileSettingsViewModel.uiState.collectAsStateWithLifecycle()

    var showLogoutDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    ObserveUIEvents(profileSettingsViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is ProfileSettingsScreenEvents.OnLogout -> {
                showLogoutDialog = false
                context.showToast(message = uIEvent.message)
                backStack.removeAll(backStack)
                backStack.add(AppScreens.UserTypeSelectionScreen)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }
        }
    }

    AttachLifecycleObserver { event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                profileSettingsViewModel.getProfile()
            }

            else -> Unit
        }
    }

    BoxCommon(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {

            Box(modifier = Modifier.padding(top = 20.dp))
            {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clickableDebAnim{
                            if(userType == UserType.HOST.value){
                                backStack.add(HostScreens.AuthHostScreens.SignUpHostScreen(
                                    phone = null,
                                    countryCode = null,
                                    step = null,
                                    isForUpdate = true
                                ))
                            }else{
                                backStack.add(UserScreens.AuthScreens.ProfileUpdateScreen)
                            }
                        }
                        .dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(color = theme_color_11.copy(alpha = 0.5f), radius = 10.dp, offset = DpOffset(x = 5.dp, y = (-5).dp))
                        )
                        .dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(color = theme_color_20.copy(alpha = 0.5f), radius = 10.dp, offset = DpOffset(x = (-5).dp, y = (5).dp))
                        )
                        .clip(CircleShape)

                        .border(width = 3.dp, brush = brush3(), shape = CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                        .padding(1.dp)
                ) {
                    AsyncImageWithLoader(uiState?.profileData?.profilePic, modifier = Modifier.fillMaxSize())
                }
            }

            Text(
                uiState?.profileData?.fullName ?: "",
                fontFamily = HAFFER_TRIAL_BOLD,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 25.dp)
            )
            Text(
                "${uiState?.profileData?.countryCode ?: ""}-${uiState?.profileData?.phone ?: ""}",
                color = text_grey_3,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 5.dp)
            )
            Text(
                uiState?.profileData?.email ?: "",
                color = text_grey_3,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 2.dp)
            )

            VIPActiveCard(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
            )
            AnimatedContent(userType) { userType ->
                userType?.let {
                    if (it == UserType.USER.value) {
                        OtherLinksUserSection(modifier = Modifier, onNavigate = {
                            profileSettingsViewModel.onEvent(CommonUIEvents.NavigateTo(it))
                        })
                    } else {
                        OtherLinksHostSection(modifier = Modifier, onNavigate = {
                            profileSettingsViewModel.onEvent(CommonUIEvents.NavigateTo(it))
                        })
                    }
                }
            }


            AnimatedContent(userType) { userType ->
                userType?.let {
                    if (it == UserType.USER.value) {
                        SecurityUserSection(modifier = Modifier, onNavigate = {
                            backStack.add(it)
                        })
                    } else {
                        SecurityHostSection(modifier = Modifier, onNavigate = {
                            backStack.add(it)
                        })
                    }
                }
            }
            logoutCard(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                onClick = {
                    showLogoutDialog = true
                })

            Spacer(modifier = Modifier.height(bottomTabBarHeight))
        }
    }

    ButtonsDialog(
        showDialog = showLogoutDialog,
        showLoader = uiState?.isLoading ?: false,
        properties = DialogProperties(dismissOnBackPress = true),
        icon = R.drawable.logoutimage78,
        title = "Logout?",
        description = "Are you sure you want to logout?",
        onDismiss = {
            showLogoutDialog = false
        },
        showBothButtons = true,
        positiveBtnText = "Yes",
        negativeBtnText = "No",
        onPositiveClick = {
            profileSettingsViewModel.logout()
        },
        onNegativeClick = {}
    )

}


@Composable
fun VIPActiveCard(modifier: Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = brushSilver(), shape = Shape_15)
            .padding(15.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Image(painterResource(R.drawable.silver_imgimage56), contentDescription = null)
            Text(
                "Silver VIP", color = Color.White, fontFamily = HAFFER_TRIAL_BOLD,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text("Your silver VIP is active", color = Color.White, fontSize = 12.sp)
                    Text("Renews on 24 Oct 2026", color = Color.White, fontSize = 12.sp)
                }
                Image(painterResource(R.drawable.check_iconicon), contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "₹178 / Week",
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = HAFFER_TRIAL_MEDIUM
            )
        }
    }

}

@Composable
private fun OtherLinksUserSection(
    modifier: Modifier,
    onNavigate: (NavKey) -> Unit
) {

    Text(
        "Other Links",
        fontFamily = HAFFER_TRIAL_MEDIUM,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    )
    Column(
        modifier = modifier
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_15,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 15.dp)
            )
            .background(color = Color.White, shape = Shape_15)
    ) {

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(WalletScreens.MyCoinsScreen)
                }
                .clip(Shape_15)
                .clickable {}
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.mycoinsbackground,
            title = "My Coins",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(CallScreens.CallHistoryScreen)
                }
                .clip(Shape_15)
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.call_historybackground,
            title = "Call History",
            description = "View your past audio and video calls."
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(ReferralScreens.ReferralScreen)
                }
                .clip(Shape_15)
                .clickable {}
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.refer_earnbackground,
            title = "Refer & Earn"
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(UserScreens.DaysStreakScreen)
                }
                .clickable {}
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.days_streakbackground,
            title = "Days Streak",
            description = "See top users ranked by coins and activity."
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(AppScreens.LeaderboardListScreen(UserType.USER.value))
                }
                .clip(Shape_15)
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.days_streakbackground,
            title = "Leaderboard",
            description = "Keep your streakm, earn more!"
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(UserScreens.FavouriteHostsScreen)
                }
                .clip(Shape_15)
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.days_streakbackground,
            title = "Favourite Hosts",
            description = "See your favourite hosts"
        )

        Spacer(modifier = Modifier.height(10.dp))

    }

}

@Composable
private fun OtherLinksHostSection(
    modifier: Modifier,
    onNavigate: (NavKey) -> Unit
) {

    Text(
        "Other Links",
        fontFamily = HAFFER_TRIAL_MEDIUM,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    )
    Column(
        modifier = modifier
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_15,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 15.dp)
            )
            .background(color = Color.White, shape = Shape_15)
    ) {

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(WithDrawScreens.WithdrawBeansScreens)
                }
                .clip(Shape_15)
                .clickable {}
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.mycoinsbackground,
            title = "Withdraw Beans",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(CallScreens.CallHistoryScreen)
                }
                .clip(Shape_15)
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.call_historybackground,
            title = "Call History",
            description = "View your past audio and video calls."
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )
        ProfileScreenOptionsItem(
            modifier = Modifier
                .clickableDebAnim {
                    onNavigate(AppScreens.LeaderboardListScreen(UserType.HOST.value))
                }
                .clip(Shape_15)
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.days_streakbackground,
            title = "Leaderboard",
            description = "Keep your streakm, earn more!"
        )
        Spacer(modifier = Modifier.height(10.dp))

    }

}

@Composable
private fun SecurityUserSection(modifier: Modifier,
                                onNavigate: (NavKey) -> Unit) {
    Text(
        "Security",
        fontFamily = HAFFER_TRIAL_MEDIUM,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    )
    Column(
        modifier = modifier
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_15,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 15.dp)
            )
            .background(color = Color.White, shape = Shape_15)
    ) {

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {
                    onNavigate(BlockScreens.BlockScreen(UserType.USER.value))
                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.block_userblockedusers,
            title = "Blocked Hosts",

            )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )
       /* ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickable {}
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.push_notificationbackground,
            title = "Push Notification",
            showToggle = true,
            showArrow = false
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )
*/
        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {
                    onNavigate(AppScreens.HelpSupportScreen)
                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.help_supportbackground,
            title = "Help & Support",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {
                    onNavigate(AppScreens.CMSScreen(type = CMSType.TERMS_CONDITION.value, role = UserType.USER.value))
                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.terms__conditionbackground,
            title = "Terms & Conditions",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {
                    onNavigate(AppScreens.CMSScreen(type = CMSType.PRIVACY_POLICY.value, role = UserType.USER.value))
                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.privacy_policybackground,
            title = "Privacy & Security"
        )

    }

}

@Composable
private fun SecurityHostSection(modifier: Modifier,
                                onNavigate: (NavKey) -> Unit) {

    Text(
        "Security",
        fontFamily = HAFFER_TRIAL_MEDIUM,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    )
    Column(
        modifier = modifier
            .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .dropShadow(
                shape = Shape_15,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 15.dp)
            )
            .background(color = Color.White, shape = Shape_15)
    ) {

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {
                    onNavigate(BlockScreens.BlockScreen(UserType.HOST.value))

                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.block_userblockedusers,
            title = "Blocked Users",

            )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {
                    onNavigate(AppScreens.ChangePasswordScreen)
                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.change_pwdbackground,
            title = "Change Password",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {

                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.help_supportbackground,
            title = "Help & Support",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {

                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.terms__conditionbackground,
            title = "Terms & Conditions",
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = divider_color_1
        )

        ProfileScreenOptionsItem(
            modifier = Modifier
                .clip(Shape_15)
                .clickableDebAnim {

                }
                .background(color = Color.White)
                .padding(15.dp),
            icon = R.drawable.privacy_policybackground,
            title = "Privacy & Security"
        )

    }

}

@Composable

fun ProfileScreenOptionsItem(
    modifier: Modifier,
    icon: Int,
    title: String,
    description: String? = null,
    showArrow: Boolean = true,
    showToggle: Boolean = false
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(painterResource(icon), contentDescription = null)
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(title, modifier = Modifier)
            description?.let {
                Text("View your past audio and video calls", fontSize = 11.sp, color = text_grey_4)
            }
        }
        if (showArrow) {
            Image(
                painterResource(R.drawable.right_arrow),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(10.dp)
            )
        }
        if (showToggle) {
            Switch(checked = true, onCheckedChange = null)
        }
    }
}

@Composable
private fun logoutCard(
    modifier: Modifier,
    onClick: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(Shape_15)
            .clickableDebAnim {
                onClick()
            }
            .dropShadow(
                shape = Shape_15,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.1f), radius = 15.dp)
            )
            .background(color = theme_color_10, Shape_20)
            .padding(horizontal = 15.dp, vertical = 12.dp)) {
        Image(painterResource(R.drawable.logoutimage78), contentDescription = null)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("Logout", color = theme_color_11, fontFamily = HAFFER_TRIAL_BOLD, fontSize = 17.sp)
            Text(
                "Sign out securely from your Kahoji account.", fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}


@Composable
fun ShakeOnErrorButton() {
    val shakeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun triggerShake() {
        scope.launch {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0.0f at 0
                    (-20f) at 50
                    20f at 100
                    (-15f) at 150
                    15f at 200
                    (-8f) at 250
                    8f at 300
                    0.0f at 400
                }
            )
        }
    }

    Box(modifier = Modifier.offset { IntOffset(shakeOffset.value.roundToInt(), 0) }) {
        Button(onClick = { triggerShake() }) {
            Text("Trigger Error Shake")
        }
    }
}