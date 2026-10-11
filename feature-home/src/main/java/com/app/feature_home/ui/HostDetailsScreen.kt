package com.app.feature_home.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.extensions.showToast
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.CallType
import com.app.core_common.utils.enums.StoryMediaType
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.ChatScreens
import com.app.core_navigation.NavigationProvider
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.components.FilledCustomButton
import com.app.core_ui.components.GiftCard
import com.app.core_ui.components.LanguageCard
import com.app.core_ui.components.ShimmerBox
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.components.shimmerEffect
import com.app.core_ui.ui.theme.HAFFER_TRIAL_MEDIUM
import com.app.core_ui.ui.theme.HAFFER_TRIAL_SEMI_BOLD
import com.app.core_ui.ui.theme.HEADING_ROW_SP
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.grey_5
import com.app.core_ui.ui.theme.grey_9
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.core_ui.ui.theme.theme_color_7
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.app.feature_gift.ui.GiftSentScreen
import com.app.feature_gift.ui.SendGiftScreen
import com.app.feature_gift.ui.events.GiftEvents
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.remote.model.response.Story
import com.app.feature_home.ui.events.HostDetailsScreenEvents
import com.app.feature_home.ui.viewmodels.HostDetailsViewModel
import com.zodiaq.ui.theme.Shape_10
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostDetailsScreen(
    id: String?,
    chatId: String?,
    fullName: String?,
    profilePic: String?,
    rating: Double?,
    isVerified: Boolean?,
    isWishlist: Boolean?,
    gender: Int?,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    hostDetailsViewModel: HostDetailsViewModel = hiltViewModel()
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp

    val imageHeight = (screenHeight * 0.4).dp

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    val uiState by hostDetailsViewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        Log.d("fkb23424324nkfbnf", uiState?.hostDetailsData?.user?.languages.toString())
    }


    LaunchedEffect(id) {

        hostDetailsViewModel.updateDataWithExistingDetails(
            id = id,
            chatID = chatId,
            fullName = fullName,
            profilePic = profilePic,
            rating = rating,
            verified = isVerified,
            gender = gender
        )
        hostDetailsViewModel.getHostDetailsData(id = id)
    }

    val context = LocalContext.current

    val user = uiState?.hostDetailsData?.user
    val stories = uiState?.hostDetailsData?.stories

    var isSomeViewsVisible by remember { mutableStateOf(false) }
    var showSendGiftSheet by remember { mutableStateOf(false) }
    var showGiftSentSuccessfully by remember { mutableStateOf<GiftListResponse.GiftData?>(null) }

    ObserveUIEvents(hostDetailsViewModel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is CommonUIEvents.ShowMessage -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.message)
            }

            is CommonUIEvents.NavigateTo -> {}
            is GiftEvents.GiftSentSuccessfully -> {
                showGiftSentSuccessfully = uIEvent.gift
            }

            is GiftEvents.GiftSentError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.msg)
            }

            is GiftEvents.GiftListFetchError -> {
                snackBarHostState.showSnackBarWithDismiss(uIEvent.msg)
            }

            is HostDetailsScreenEvents.BlockedSuccessfully -> {
                context.showToast(uIEvent.message)
                backStack.removeLastOrNull()
            }

            is HostDetailsScreenEvents.CallPartner -> {
                NavigationProvider.callNavigator.startCallActivity(
                    context,
                    chatID = uIEvent.chatID,
                    partnerID = uIEvent.partnerID,
                    personName = uIEvent.personName,
                    callType = uIEvent.callType,
                    profilePic = uIEvent.profilePic
                )
            }
        }
    }

    var showMenu by remember { mutableStateOf(false) }
    var showBlockHostDialog by rememberSaveable { mutableStateOf<String?>(null) }

    BoxCommon(modifier = Modifier.blur(if (showSendGiftSheet || showGiftSentSuccessfully != null) 10.dp else 0.dp)) {
        BottomSheetScaffold(
            containerColor = Color.Transparent,
            scaffoldState = sheetState,
            sheetPeekHeight = (screenHeight - (imageHeight.value.toInt())).dp,
            sheetDragHandle = null,
            sheetShape = RoundedCornerShape(0.dp),
            sheetContent = {

                if (isSomeViewsVisible) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .dropShadowCustom()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                            )
                    ) {
                                SheetContent(
                                    user = user,
                                    stories = stories,
                                    giftsList = uiState?.giftsList,
                                    isGiftListLoading = uiState?.isGiftsListLoading ?: false,
                                    onViewAllGift = {
                                        showSendGiftSheet = true
                                    },
                                    isLoading = uiState?.isLoading ?: false,
                                    onSendGift = { gift ->
                                        hostDetailsViewModel.onEvent(CommonUIEvents.ShowMessage("Sending your gift to ${uiState?.hostDetailsData?.user?.fullName}..."))
                                        hostDetailsViewModel.sendGift(
                                            receiverId = uiState?.hostDetailsData?.user?.id,
                                            gift = gift
                                        )
                                    })
                    }
                }
            }
        )
        {

            // Main screen
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImageWithLoader(
                    user?.profilePic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .onVisibilityChanged {
                            if (it) {
                                isSomeViewsVisible = true
                            }
                        }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.systemBarsPadding()
                    )
                    {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(color = grey_9, shape = CircleShape)
                                .padding(10.dp)
                        )
                        {
                            Image(
                                painter = painterResource(R.drawable.back),
                                contentDescription = null,
                                modifier = Modifier.clickableDebAnim {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))


                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(color = grey_4, shape = CircleShape)
                                .padding(8.dp)
                        ) {
                            AnimatedContent(user?.isWishlisted) { isWishlist ->
                                if (isWishlist == null) {

                                    CircularProgressIndicator(
                                        color = theme_color_1,
                                        modifier = Modifier.size(15.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(R.drawable.lovevector),
                                        colorFilter = ColorFilter.tint(color = if (user?.isWishlisted == true) Color.Red else Color.White),
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            hostDetailsViewModel.updateFavourite(
                                                !(user?.isWishlisted ?: false)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        Box {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(35.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        showMenu = !showMenu
                                    }
                                    .background(color = grey_9, shape = CircleShape)
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.outline_more_vert_24),
                                    contentDescription = null,

                                    )

                            }

                            // Popup below menu icon
                            DropdownMenu(
                                expanded = showMenu,
                                properties = PopupProperties(
                                    usePlatformDefaultWidth = false
                                ),
                                onDismissRequest = {
                                    showMenu = false
                                },
                                containerColor = Color.Transparent
                            )
                            {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .dropShadowCustom(
                                                shape = Shape_10,
                                                color = Color.Black.copy(alpha = 0.1f)
                                            )
                                            .clip(Shape_10)
                                            .background(
                                                color = Color.White,
                                                shape = Shape_10
                                            ),
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {

                                        Text(
                                            text = "Block",
                                            modifier = Modifier
                                                .clickableDebAnim {
                                                    showBlockHostDialog =
                                                        uiState?.hostDetailsData?.user?.id
                                                }
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        )

                                        /*   Text(
                                            text = "Report",
                                            modifier = Modifier
                                                .clickableDebAnim {}
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
*/

                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        user?.fullName ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        AnimatedVisibility(user?.isVerified == true) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(color = grey_9, shape = CircleShape)
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {

                                Image(
                                    painterResource(R.drawable.verifies_iconsymbol),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text("Verified", fontSize = 11.sp, modifier = Modifier)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .background(color = grey_9, shape = CircleShape)
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            AnimatedVisibility(user?.isOnline == 1) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(color = green_4, CircleShape)
                                )
                            }
                            Text(
                                if (user?.isOnline == 1) "Online" else "Offline",
                                fontSize = 11.sp,
                                modifier = Modifier
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        AnimatedVisibility(user?.age != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(
                                        color = Color.White.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.age_iconcontainer),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    "${(user?.age ?: 0)} Years",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                )
                            }
                        }
                        AnimatedVisibility(!user?.city.isNullOrEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(
                                        color = Color.White.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.country_iconsymbol),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    user?.city ?: "",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                )
                            }
                        }
                        AnimatedVisibility(!user?.sign.isNullOrEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .background(
                                        color = Color.White.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.rashi_iconcontainer),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    user?.sign ?: "",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .dropShadow(
                    shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp),
                    shape = CircleShape
                )
                .background(color = Color.White.copy(alpha = 0.4f), shape = CircleShape)
                .padding(horizontal = 5.dp, vertical = 10.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickableDebAnim {
                        backStack.add(
                            ChatScreens.ChatScreen(
                                userType = UserType.USER.value,
                                partnerID = uiState?.hostDetailsData?.user?.id,
                                partnerName = uiState?.hostDetailsData?.user?.fullName,
                                partnerProfilePic = uiState?.hostDetailsData?.user?.profilePic,
                                isOnline = uiState?.hostDetailsData?.user?.isOnline,
                                isPartnerFav = if (uiState?.hostDetailsData?.user?.isWishlisted
                                        ?: false
                                ) 1 else 0,
                                chatID = uiState?.hostDetailsData?.user?.chatId,
                                isVerified = uiState?.hostDetailsData?.user?.isVerified,
                                gender = uiState?.hostDetailsData?.user?.gender,
                            )
                        )
                    }
                    .dropShadow(
                        shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp),
                        shape = CircleShape
                    )
                    .background(color = Color.White, shape = CircleShape)
                    .padding(5.dp)
            ) {
                Image(
                    painterResource(com.app.feature_home.R.drawable.chat_call_icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickableDebAnim {
                        hostDetailsViewModel.onEvent(
                            HostDetailsScreenEvents.CallPartner(
                                chatID = uiState?.hostDetailsData?.user?.chatId,
                                partnerID = uiState?.hostDetailsData?.user?.id,
                                personName = uiState?.hostDetailsData?.user?.fullName,
                                profilePic = uiState?.hostDetailsData?.user?.profilePic,
                                callType = CallType.Video.type
                            )
                        )
                    }
                    .dropShadow(
                        shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp),
                        shape = CircleShape
                    )
                    .background(color = Color.White, shape = CircleShape)
                    .padding(5.dp)
            ) {
                Image(
                    painterResource(com.app.feature_home.R.drawable.video_call_icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)

                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickableDebAnim {
                        hostDetailsViewModel.onEvent(
                            HostDetailsScreenEvents.CallPartner(
                                chatID = uiState?.hostDetailsData?.user?.chatId,
                                partnerID = uiState?.hostDetailsData?.user?.id,
                                personName = uiState?.hostDetailsData?.user?.fullName,
                                profilePic = uiState?.hostDetailsData?.user?.profilePic,
                                callType = CallType.Audio.type
                            )
                        )
                    }
                    .dropShadow(
                        shadow = Shadow(color = Color.Black.copy(0.1f), radius = 10.dp),
                        shape = CircleShape
                    )
                    .background(color = Color.White, shape = CircleShape)
                    .padding(5.dp)
            ) {
                Image(
                    painterResource(com.app.feature_home.R.drawable.phone_call_icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }

        }

    }


    SendGiftScreen(
        show = showSendGiftSheet,
        onDismiss = {
            showSendGiftSheet = false
        },
        isGiftsListLoading = uiState?.isGiftsListLoading,
        isGiftSending = uiState?.isGiftSending,
        giftsList = uiState?.giftsList,
        onSend = { gift ->
            hostDetailsViewModel.onEvent(CommonUIEvents.ShowMessage("Sending your gift to ${uiState?.hostDetailsData?.user?.fullName}..."))
            hostDetailsViewModel.sendGift(
                receiverId = uiState?.hostDetailsData?.user?.id,
                gift
            )
        },
    )

    GiftSentScreen(
        giftData = showGiftSentSuccessfully,
        onDismiss = {
            showGiftSentSuccessfully = null
        })

    ButtonsDialog(
        showDialog = !showBlockHostDialog.isNullOrEmpty(),
        showLoader = uiState?.isBlockLoading ?: false,
        title = "Block User?",
        description = "Are you sure you want to block this user? You will no longer be able to interact with each other.",
        icon = R.drawable.block_userblockedusers,
        onDismiss = {
            showBlockHostDialog = null
        },
        showBothButtons = true,
        positiveBtnText = "Block",
        negativeBtnText = "Cancel",
        onPositiveClick = {
            hostDetailsViewModel.block(showBlockHostDialog)
        },
        onNegativeClick = {
            showBlockHostDialog = null
        }
    )

}


@OptIn(ExperimentalFlexBoxApi::class)
@Composable
fun SheetContent(
    user: HostData?,
    stories: List<Story?>?,
    giftsList: List<GiftListResponse.GiftData?>?,
    isGiftListLoading: Boolean,
    isLoading : Boolean,
    onViewAllGift: () -> Unit,
    onSendGift: (GiftListResponse.GiftData?) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .animateContentSize()

    )
    {

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp)
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Gray)
        )

        Log.d("fbknfkbnfb", (isLoading && user?.languages.isNullOrEmpty()).toString())
        if(isLoading && user?.languages.isNullOrEmpty()){
            LanguageShimmer()
        }else{
            Text(
                "Languages",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                fontSize = HEADING_ROW_SP,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            )
            {
                user?.languages?.forEach { lang ->
                    LanguageCard(name = lang)
                }
            }
        }

        if(isLoading && user?.about.isNullOrEmpty()){
            AboutMeShimmer()
        }else {
            user?.about?.let {
                Text(
                    "About Me",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = HEADING_ROW_SP,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp)
                )
                Text(
                    user.about ?: "",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 20.dp),
                    lineHeight = 20.sp
                )
            }
        }


        if(isLoading && user?.about.isNullOrEmpty()){
            StatsCardShimmer()
        }else {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .dropShadow(
                        shape = Shape_20,
                        shadow = Shadow(color = theme_color_1.copy(0.5f), radius = 15.dp)
                    )
                    .background(color = theme_color_7, shape = Shape_20)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            )
            {

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(
                        painterResource(R.drawable.talk_time_iconbackground_shadow),
                        contentDescription = null
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total Talk Time", color = grey_5, fontSize = 12.sp)
                        Text(
                            user?.totalTalkTime ?: "",
                            color = Color.Black,
                            modifier = Modifier.padding(top = 3.dp),
                            fontSize = 17.sp,
                            fontFamily = HAFFER_TRIAL_MEDIUM
                        )
                    }
                }
                VerticalDivider(
                    thickness = 1.dp,
                    color = grey_9,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 5.dp)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                )
                {
                    Image(
                        painterResource(R.drawable.level_iconbackground_shadow),
                        contentDescription = null
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Level", color = grey_5, fontSize = 12.sp)
                        Text(
                            "Gold host",
                            color = theme_color_11,
                            modifier = Modifier.padding(top = 3.dp),
                            fontSize = 17.sp,
                            fontFamily = HAFFER_TRIAL_MEDIUM
                        )
                    }
                }

            }
        }
        if(isLoading && giftsList.isNullOrEmpty()){
            GiftsShimmer()
        }else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    "Popular Gifts",
                    fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                    fontSize = HEADING_ROW_SP,
                    modifier = Modifier
                        .weight(1f)

                )

                FilledCustomButton(
                    onClick = onViewAllGift,
                    color = Color.Transparent,
                    vertical = 0.dp,
                    horizontal = 0.dp,
                    text = "View All",
                    endIcon = R.drawable.baseline_arrow_forward_ios_24,
                    endIconColor = theme_color_11,
                    textColor = theme_color_11
                )
            }

            AnimatedVisibility(
                isGiftListLoading && giftsList.isNullOrEmpty(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(IntrinsicSize.Max)
                    .horizontalScroll(rememberScrollState())
            ) {
                giftsList?.forEachIndexed { index, gift ->
                    val padding = if (index == 0) 20.dp else 15.dp
                    val paddingEnd = if (index == 9) 20.dp else 0.dp

                    GiftCard(
                        image = gift?.image,
                        name = gift?.name,
                        coinCost = gift?.coinCost,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = padding, end = paddingEnd),
                        onClick = {
                            onSendGift(gift)
                        }
                    )
                }
            }
        }

        if(isLoading && giftsList.isNullOrEmpty()){
            ActiveCardShimmer()
        }else {
            ActiveStoriesCard(
                stories,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

    }


}



@Composable
fun dotIndicators(
    modifier: Modifier = Modifier,
    count: Int,
    currentIndex: Int = 0,
    onChange: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(count) { index ->

            val color by animateColorAsState(if (currentIndex == index) theme_color_11 else Color.Gray)

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color = color, CircleShape)
            )
        }
    }

}

@Composable
fun ActiveStoriesCard(stories: List<Story?>?, modifier: Modifier) {

    var selectedTab by remember { mutableIntStateOf(0) }
    Column(modifier = modifier.fillMaxWidth()) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Active Stories",
                fontFamily = HAFFER_TRIAL_SEMI_BOLD,
                fontSize = HEADING_ROW_SP,
                modifier = Modifier.weight(1f)
            )

            /*   AnimatedIconsTabBar(
                tabs = listOf(
                    R.drawable.video_icon_1group,
                    R.drawable.camera_icon_1ic_outlinephotocamerafront
                ),
                selectedTabIndex = selectedTab,
                labelMapper = {
                    "Ok"
                },
                onSelect = { item, index ->
                    selectedTab = index
                },
                modifier = Modifier
            )*/

        }


        stories?.chunked(3)?.forEach { gridList ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                gridList.forEach { story ->
                    ActiveStoryCard(story, modifier = Modifier)
                }
                if (gridList.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f))
                } else if (gridList.size == 2) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun RowScope.ActiveStoryCard(story: Story?, modifier: Modifier) {
    Box(
        modifier = modifier
            .weight(1f)
            .aspectRatio(1f)
            .clip(Shape_15)
            .dropShadow(
                shape = Shape_15,
                shadow = Shadow(color = Color.Black.copy(0.05f), radius = 10.dp)
            )
            .background(color = Color.White, Shape_15)
    )
    {
        AsyncImageWithLoader(
            story?.url,
            modifier = Modifier.matchParentSize()
        )
        if (story?.mediaType == StoryMediaType.VIDEO.apiValue) {
            Image(
                painterResource(R.drawable.video_icon_1group),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            )
        }
        story?.viewsFormatted?.let {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .border(width = 1.dp, color = grey_4, Shape_15)
                    .background(color = Color.Black.copy(0.3f), Shape_15)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Image(painterResource(R.drawable.eye_icon), contentDescription = null)
                Text("6.2M", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}
@Composable
fun LanguageShimmer(){
    // Languages
    ShimmerBox(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 20.dp)
            .width(95.dp)
            .height(20.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 15.dp,
                start = 20.dp,
                end = 20.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
        repeat(3) {
            ShimmerBox(
                modifier = Modifier
                    .width(70.dp)
                    .height(32.dp),
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@Composable
fun AboutMeShimmer(){
    // About Me
    ShimmerBox(
        modifier = Modifier
            .padding(top = 22.dp)
            .padding(horizontal = 20.dp)
            .width(90.dp)
            .height(20.dp)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                start = 20.dp,
                end = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        )

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(14.dp)
        )

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(14.dp)
        )
    }

}
@Composable
fun StatsCardShimmer(){
    // Stats card
    Row(
        modifier = Modifier
            .padding(
                top = 20.dp,
                start = 20.dp,
                end = 20.dp
            )
            .fillMaxWidth()
            .height(85.dp)
            .clip(Shape_20)
            .background(grey_9)
            .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    )
    {

        // Total Talk Time
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBox(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(11.dp)
                )

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(17.dp)
                )
            }
        }

        VerticalDivider(
            thickness = 1.dp,
            color = grey_9,
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 5.dp)
        )

        // Level
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBox(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(11.dp)
                )

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(17.dp)
                )
            }
        }
    }
}
@Composable
fun GiftsShimmer(){
    // Popular Gifts
    ShimmerBox(
        modifier = Modifier
            .padding(
                top = 20.dp,
                start = 20.dp,
                end = 20.dp
            )
            .width(115.dp)
            .height(20.dp)
    )

    // Gift cards
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.width(5.dp))

        repeat(5) {
            ShimmerBox(
                modifier = Modifier
                    .width(90.dp)
                    .height(110.dp),
                shape = Shape_20
            )
        }

        Spacer(modifier = Modifier.width(5.dp))
    }
}
@Composable
fun ActiveCardShimmer(){
    // Active Stories
    ShimmerBox(
        modifier = Modifier
            .padding(
                top = 20.dp,
                start = 20.dp,
                end = 20.dp
            )
            .fillMaxWidth()
            .height(150.dp),
        shape = Shape_20
    )

    Spacer(modifier = Modifier.height(20.dp))
}