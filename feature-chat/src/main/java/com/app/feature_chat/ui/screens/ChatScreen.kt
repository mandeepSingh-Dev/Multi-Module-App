package com.app.feature_chat.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
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
import com.app.core_common.utils.enums.UserType
import com.app.core_common.utils.localeUtils.getTodayYesterdayElseDate2
import com.app.core_navigation.UserScreens
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.BoxCommon
import com.app.core_ui.components.ChatTextField
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.components.dropShadowCustom
import com.app.core_ui.ui.theme.HAFFER_TRIAL_BOLD
import com.app.core_ui.ui.theme.green_4
import com.app.core_ui.ui.theme.grey_10
import com.app.core_ui.ui.theme.grey_12
import com.app.core_ui.ui.theme.grey_4
import com.app.core_ui.ui.theme.theme_color_1
import com.app.core_ui.ui.theme.theme_color_18
import com.app.core_ui.ui.theme.theme_color_19
import com.app.feature_chat.remote.model.response.ChatMessagesResponse
import com.app.feature_chat.ui.viewmodels.ChatScreenViewModel
import com.zodiaq.ui.theme.Shape_20
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.window.Dialog
import com.app.core_ui.components.ButtonsDialog
import com.app.core_ui.ui.theme.grey_1
import com.app.core_ui.ui.theme.theme_color_11
import com.app.feature_chat.ui.events.ChatScreenEvents
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.app.core_ui.components.BlockedUserBottomHeader

@Composable
fun ChatScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    chatScreenViewModel: ChatScreenViewModel = hiltViewModel(),
    userType: Int,
    chatID: String?,
    partnerName: String?,
    partnerProfilePic: String?,
    partnerID: String?,
    isPartnerFav: Int?,
    isOnline: Int?,
    isVerified: Boolean?,
    gender: Int?,
) {

    val uiState by chatScreenViewModel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    var showEmojiDialog by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        chatScreenViewModel.loadMessages(page = 1, chatId = chatID)
//    }
    var showBlockHostDialog by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        chatScreenViewModel.chatID = chatID
        chatScreenViewModel.partnerID = partnerID

        chatScreenViewModel.updateState { state ->
            state.copy(
                isPartnerFav = isPartnerFav
            )
        }

        chatScreenViewModel.sendJoinChatSocketEvent()
    }

    ObserveUIEvents(chatScreenViewModel.uiEvent) { event ->
        when (event) {
            is CommonUIEvents.ShowMessage -> {
            }

            is CommonUIEvents.NavigateTo -> {

            }

            is CommonUIEvents.ShowError -> {
                showBlockHostDialog = null
                snackBarHostState.showSnackBarWithDismiss(event.error)
            }

            is ChatScreenEvents.BlockedSuccessfully -> {
                showBlockHostDialog = null
            }
            is ChatScreenEvents.UnblockedSuccessfully -> {
                showBlockHostDialog = null
            }
        }
    }

    LaunchedEffect(listState) {

        snapshotFlow { listState.canScrollForward }
            .collect {
                Log.d("fkbnfkbnf", listState.canScrollForward.toString())

                if (!listState.canScrollForward &&
                    !(uiState?.isLoading ?: false && !(uiState?.futureListEmpty ?: false))
                /* && !listState.isScrollInProgress*/) {
                    chatScreenViewModel.loadMessages(
                        page = chatScreenViewModel.page.plus(1),
                        chatId = chatID
                    )
                }
            }
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.collect { firstVisibleIndex ->


            Log.d(
                "fkbnkfbnfk",
                "first Visible index $firstVisibleIndex -> ${uiState?.messages?.size}"
            )

            if (firstVisibleIndex == 0) {
                chatScreenViewModel.page += 1
                chatScreenViewModel.loadMessages(page = chatScreenViewModel.page, chatId = chatID)
            }
        }
    }

    BoxCommon(
        modifier = Modifier
            .fillMaxSize()
            .blur(if (showEmojiDialog) 5.dp else 0.dp)
    )
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {

            // =========================
            // FIXED HEADER
            // =========================
            ChatScreenHeader(
                modifier = Modifier,
                partnerName = partnerName,
                profilePic = partnerProfilePic,
                isOnline = isOnline,
                isPartnerFav = uiState?.isPartnerFav,
                onProfileIconClick = {
                    if (userType == UserType.HOST.value && uiState?.isBlocked == false) {
                        backStack.add(
                            UserScreens.HostDetailsScreen(
                                id = partnerID,
                                chatId = chatID,
                                fullName = partnerName,
                                profilePic = partnerProfilePic,
                                rating = null,
                                isVerified = isVerified,
                                isWishlist = isPartnerFav == 1,
                                gender = gender
                            )
                        )
                    }
                },
                onBack = {
                    backStack.removeLastOrNull()
                },
                onFav = {
                    if(uiState?.isBlocked == false) {
                        val isFav = uiState?.isPartnerFav == 1
                        chatScreenViewModel.updateFavourite(
                            !isFav
                        )
                    }
                },
                onBlock = {
                    if(uiState?.isBlocked == false) {
                        showBlockHostDialog = partnerID
                    }
                }
            )
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                reverseLayout = true
            )
            {


                items(uiState?.messages ?: emptyList()) { item ->

                    if (item?.userId == uiState?.profileData?.id) {
                        rightMessageView(
                            modifier = Modifier.padding(top = 10.dp),
                            item
                        )
                    } else {
                        leftMessageView(
                            modifier = Modifier.padding(top = 10.dp),
                            item
                        )
                    }
                }
                item {
//                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
//                            horizontalArrangement = Arrangement.Center) {
                    AnimatedVisibility(
                        uiState?.isLoading ?: false && chatScreenViewModel.page >= 1,
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ) {
                        Column(modifier = Modifier.padding(vertical = 10.dp)) {
                            CircularProgressIndicator(
                                color = theme_color_1,
                                modifier = Modifier.size(25.dp)
                            )
                        }
//                            }
                    }
                }
            }


            AnimatedContent(uiState?.isBlocked ?: false) { isBlocked ->
                if (isBlocked) {
                    BlockedUserBottomHeader(
                        isLoading = uiState?.isBlockLoading,
                        isBlocked = uiState?.isBlocked ?: false,
                        onUnblockClick = {
                            showBlockHostDialog = partnerID
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {

                    // =========================
                    // INPUT
                    // =========================
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .dropShadow(
                                shape = Shape_20,
                                shadow = Shadow(color = Color.Black.copy(0.2f), radius = 10.dp)
                            )
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            )
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .padding(bottom = 5.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {


                            ChatTextField(
                                modifier = Modifier.weight(1f),
                                value = uiState?.message ?: "",
                                onValueChange = {
                                    chatScreenViewModel.updateState { state ->
                                        state.copy(message = it)
                                    }
                                },
                                startIcon = R.drawable.emoji_vectorclarity_happy_face_line,
                                startIconSize = 20.dp,
                                startIconTint = grey_4,
//                            endIcon = R.drawable.mikemike,
//                            endIconSize = 20.dp,
                                bgColor = grey_12,
                                borderColor = Color.Transparent,
                                verticalPadding = 12.dp,
                                hint = "Type a message...",
                                onShowEmojis = {
                                    showEmojiDialog = true
                                }
                            )

                            Image(
                                painter = painterResource(
                                    R.drawable.send_iconbuttonsendreply
                                ),
                                contentDescription = null,
                                modifier = Modifier.clickableDebAnim {
                                    if (!uiState?.message.isNullOrEmpty()) {
                                        chatScreenViewModel.updateState { state ->
                                            state.copy(message = null)
                                        }
                                        chatScreenViewModel.sendMessage(
                                            message = uiState?.message,
                                            partnerId = partnerID,
                                            chatId = chatID
                                        )
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }


    if (showEmojiDialog) {
        EmojiPickerDialog(
            onEmojiSelected = {
                showEmojiDialog = false
                chatScreenViewModel.sendMessage(
                    message = it,
                    partnerId = partnerID,
                    chatId = chatID
                )
            },
            onDismiss = { showEmojiDialog = false },
        )
    }

    ButtonsDialog(
        showDialog = !showBlockHostDialog.isNullOrEmpty(),
        showLoader = uiState?.isBlockLoading ?: false,
        title = if (uiState?.isBlocked ?: false) "Unblock User?" else "Block User?",
        description = if (uiState?.isBlocked ?: false
        ) "Are you sure you want to unblock this user?" else "Are you sure you want to block this user? You will no longer be able to interact with each other.",
        icon = R.drawable.block_userblockedusers,
        onDismiss = {
            showBlockHostDialog = null
        },
        showBothButtons = true,
        positiveBtnText = if (uiState?.isBlocked ?: false) "Unblock?" else "Block",
        negativeBtnText = "Cancel",
        onPositiveClick = {
            chatScreenViewModel.block(showBlockHostDialog)
        },
        onNegativeClick = {
            showBlockHostDialog = null
        }
    )


}

@Composable
private fun ChatScreenHeader(
    modifier: Modifier,
    partnerName: String?,
    profilePic: String?,
    isOnline: Int?,
    isPartnerFav: Int?,
    onBack: () -> Unit,
    onProfileIconClick: () -> Unit,
    onFav: () -> Unit,
    onBlock: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = theme_color_18)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .clickableDebAnim {
                    onBack()
                }
        )

        Box(
            modifier = Modifier
                .clickableDebAnim {
                    onProfileIconClick()
                }
                .clip(CircleShape)
                .border(width = 1.dp, color = theme_color_1, shape = CircleShape)
                .padding(1.dp)
        ) {
            AsyncImageWithLoader(
                profilePic,
                modifier = Modifier.size(35.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(

                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(partnerName ?: "", fontFamily = HAFFER_TRIAL_BOLD, fontSize = 16.sp)

                Image(
                    painter = painterResource(R.drawable.verifies_iconsymbol),
                    contentDescription = null
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isOnline == 1) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(7.dp)
                            .background(green_4)
                    )
                }
                Text(if (isOnline == 1) "Online" else "Offline", color = grey_4, fontSize = 11.sp)
            }
        }

        Image(
            painter = painterResource(R.drawable.starcontainer),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = if (isPartnerFav == 0) grey_1 else theme_color_11),
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .clickable {
                    onFav()
                }
        )
        Image(
            painter = painterResource(R.drawable.outline_more_vert_24),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .clickable {
                    onBlock()
                }
        )

    }

}

@Composable
fun leftMessageView(modifier: Modifier = Modifier, item: ChatMessagesResponse.MessageChat?) {


    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {

        ProfileImage(
            image = item?.user?.profilePic,
            modifier = Modifier.size(25.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f, false)
                .dropShadowCustom(Shape_20, color = Color.Black.copy(0.05f), radius = 5.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp)
                )
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier,
            ) {
                Text(item?.message ?: "")
                item?.createdAt.getTodayYesterdayElseDate2()?.let {
                    Text(
                        it, color = grey_10, fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }

}

@Composable
fun rightMessageView(modifier: Modifier, item: ChatMessagesResponse.MessageChat?) {


    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End,
        modifier = modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .weight(1f, false)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp))
                .background(
                    color = theme_color_19,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .padding(15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier,
            ) {
                Text(item?.message ?: "")
                item?.createdAt.getTodayYesterdayElseDate2()?.let {
                    Text(
                        it, color = grey_10, fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }

}


private data class EmojiCategory(
    val name: String,
    val icon: String,
    val emojis: List<String>
)

private val emojiCategories = listOf(
    EmojiCategory(
        "Smileys", "😀",
        listOf(
            "😀", "😃", "😄", "😁", "😆", "😅", "😂",
            "🤣", "🥹", "😊", "😇", "🙂", "🙃", "😉",
            "😍", "🥰", "😘", "😋", "😎", "🤩", "🥳",
            "😭", "😢", "😡", "🤬", "🤔", "🫡", "🤗",
            "🤫", "😴"
        )
    ),
    EmojiCategory(
        "Gestures", "👋",
        listOf(
            "👋", "🤚", "✋", "🖐️", "👌", "🤌", "🤏",
            "✌️", "🤞", "🤟", "🤘", "🤙", "👈", "👉",
            "👆", "👇", "👍", "👎", "👏", "🙌", "🤝",
            "🙏", "💪", "🫶"
        )
    ),
    EmojiCategory(
        "Hearts", "❤️",
        listOf(
            "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤",
            "🤍", "💔", "💕", "💖", "💗", "💘", "💝",
            "💯", "💥", "✨", "⭐", "🌟", "🔥", "🎉",
            "🎊", "💫", "✅", "❌", "⚡"
        )
    ),
    EmojiCategory(
        "Animals", "🐶",
        listOf(
            "🐶", "🐱", "🐼", "🦁", "🐯", "🐸", "🐵",
            "🦊", "🐻", "🐰", "🐨", "🐮", "🐷", "🐸",
            "🐔", "🐧", "🐦", "🦄", "🦋", "🐢", "🐬",
            "🐳", "🦖", "🐙"
        )
    ),
    EmojiCategory(
        "Food", "🍕",
        listOf(
            "🍕", "🍔", "🍟", "🍎", "🍉", "🍓", "🍌",
            "🍇", "🍍", "🥭", "🍒", "🍩", "🍪", "🎂",
            "🍰", "🍫", "🍿", "☕", "🍵", "🥤", "🍜",
            "🍕", "🍟"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerDialog(
    onEmojiSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedCategory by remember { mutableIntStateOf(0) }

    val currentEmojis = remember(selectedCategory) {
        emojiCategories[selectedCategory].emojis
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    emojiCategories.forEachIndexed { index, category ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedCategory == index) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                            modifier = Modifier
                                .clickable {
                                    selectedCategory = index
                                }
                        ) {
                            Text(
                                text = category.icon,
                                fontSize = 25.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 52.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(currentEmojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onEmojiSelected(emoji)

                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 36.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}







