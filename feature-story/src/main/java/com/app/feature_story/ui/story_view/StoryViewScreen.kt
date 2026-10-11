package com.app.feature_story.ui.story_view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.app.core_common.extensions.showSnackBarWithDismiss
import com.app.core_common.utils.CommonUIEvents
import com.app.core_common.utils.ObserveUIEvents
import com.app.core_common.utils.enums.CallType
import com.app.core_common.utils.enums.UserType
import com.app.core_navigation.ChatScreens
import com.app.core_navigation.NavigationProvider
import com.app.core_story.remote.model.response.StoriesListResponse
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import com.app.core_ui.components.ProfileImage
import com.app.core_ui.components.clickableDebAnim
import com.app.core_ui.local_providers.LocalBackStackNav
import com.app.core_ui.local_providers.LocalSnackBarHostState
import com.app.core_ui.ui.theme.theme_color_1
import com.app.feature_story.ui.events.StoryViewScreenEvents
import com.app.feature_story.ui.viewmodels.StoryViewScreenViewModel
import com.zodiaq.ui.theme.Shape_15
import com.zodiaq.ui.theme.Shape_20
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


const val LONG_PRESS_DURATION_FOR_STORY = 200

@Composable
fun StoryViewScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    viewmodel: StoryViewScreenViewModel = hiltViewModel()
) {

    val users by viewmodel.users.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(initialPage = 0) { users.size }
    val scope = rememberCoroutineScope()

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    Log.d("vkfnvkfvnf", users.size.toString())

    val context = LocalContext.current
    ObserveUIEvents(viewmodel.uiEvent) { uIEvent ->
        when (uIEvent) {
            is CommonUIEvents.ShowError -> {
                snackBarHostState.showSnackBarWithDismiss(message = uIEvent.error)
            }

            is CommonUIEvents.NavigateTo -> {
                backStack.add(uIEvent.navKey)
            }

            is StoryViewScreenEvents.CallPartner -> {
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




    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.1f))
            .navigationBarsPadding()
    )
    {
        if (uiState?.isLoading == true) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            )
            { page ->
                // Only the settled, current page auto-advances its timer.
                val isActive = pagerState.currentPage == page && !pagerState.isScrollInProgress

                users.getOrNull(page)?.let { userstoryGroup ->
                    UserStoryPage(
                        userStoryGroup = userstoryGroup,
                        isActive = isActive,
                        onNextUser = {
                            if (page < users.lastIndex) {
                                scope.launch { pagerState.animateScrollToPage(page + 1) }
                            } else {
                                backStack.removeLastOrNull()
                            }
                            // else: last user, last story -> just stay (or close screen in real app)
                        },
                        onPrevUser = {
                            if (page > 0) {
                                scope.launch { pagerState.animateScrollToPage(page - 1) }
                            }
                        },
                        onMessage = {
                            viewmodel.navigationEvent(
                                ChatScreens.ChatScreen(
                                    userType = UserType.USER.value,
                                    partnerID = userstoryGroup.user?.id,
                                    partnerName = userstoryGroup.user?.fullName,
                                    partnerProfilePic = userstoryGroup?.user?.profilePic,
                                    isOnline = 1, //TODO STATIC MAKE API DYNAMIC
                                    isPartnerFav = 1,  //TODO STATIC MAKE API DYNAMIC
                                    chatID = userstoryGroup.user?.chatId,
                                    isVerified = userstoryGroup.user?.isVerified,
                                    gender = userstoryGroup.user?.gender,
                                )
                            )
                        },
                        onCall = {
                            viewmodel.onEvent(
                                StoryViewScreenEvents.CallPartner(
                                    chatID = userstoryGroup.user?.chatId,
                                    partnerID = userstoryGroup.user?.id,
                                    personName = userstoryGroup.user?.fullName,
                                    profilePic = userstoryGroup.user?.profilePic,
                                    callType = it.type
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
//    }

/**
 * Renders a single user's story experience: top progress bars,
 * header, dummy content, and left/right tap zones + long-press pause.
 */
@Composable
fun UserStoryPage(
    userStoryGroup: StoriesListResponse.UserStoryGroup,
    isActive: Boolean,
    onNextUser: () -> Unit,
    onPrevUser: () -> Unit,
    onMessage: () -> Unit,
    onCall: (CallType) -> Unit
) {
    val stories = userStoryGroup.stories

    // Keyed on userId so each user gets fresh state the first time
    // their page is composed, but keeps state while it stays alive
    // in the pager's composition (e.g. swiping back and forth).
    var currentIndex by remember(userStoryGroup.user?.id) { mutableStateOf(0) }
    val progressList: SnapshotStateList<Float> = remember(userStoryGroup.user?.id) {
        mutableStateListOf(*FloatArray((stories?.size ?: 0)).toTypedArray())
    }
    var isPaused by remember(userStoryGroup.user?.id) { mutableStateOf(false) }

    fun goToStory(targetIndex: Int) {
        when {
            targetIndex < 0 -> onPrevUser()
            targetIndex >= (stories?.size ?: 0) -> onNextUser()
            else -> {
                stories?.let {
                    for (i in it.indices) {
                        progressList[i] = if (i < targetIndex) 1f else 0f
                    }
                }
                currentIndex = targetIndex
            }
        }
    }

    // Drives the currently-visible bar's fill. Ticks every 16ms
    // (~60fps) so the animation is smooth regardless of duration.
    LaunchedEffect(currentIndex, isActive, userStoryGroup.user?.id) {
        if (!isActive || stories?.isEmpty() == true) return@LaunchedEffect

        val duration = if (stories?.getOrNull(currentIndex)?.mediaType == "video") 15_000 else 5_000

        Log.d("fbknfkbnf", currentIndex.toString() + " Current Index")
        Log.d("fbknfkbnf", isActive.toString() + " is Active")
        Log.d("fbknfkbnf", userStoryGroup.user?.id.toString() + " user id ")

//        val duration = 15_000  // TODO CHANGE THIS LATER ACCORDING TO MEDIA TYPE.
        var elapsedMs = (progressList[currentIndex] * duration).toLong()

        while (elapsedMs < duration) {
            if (isPaused) {
                delay(50.milliseconds)
            } else {
                delay(16.milliseconds)
                elapsedMs += 16
                progressList[currentIndex] = (elapsedMs / duration.toFloat()).coerceIn(0f, 1f)
            }
        }
        goToStory(currentIndex + 1)
    }

    if (stories.isNullOrEmpty()) return
    val currentStory = stories.getOrNull(currentIndex)

    Box(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Black.copy(0.3f),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black.copy(0.3f)
                    )
                )
            )
            .pointerInput(userStoryGroup.user?.id, currentIndex) {
                detectTapGestures(
                    onPress = {
                        // Only pause on a genuine hold, not a quick tap.
                        coroutineScope {
                            val pauseJob = launch {
                                delay(180.milliseconds)
                                isPaused = true
                            }
                            tryAwaitRelease()
                            pauseJob.cancel()
                            isPaused = false
                        }
                    },
                    onTap = { offset ->
                        val isLeftSide = offset.x < size.width / 2f
                        if (isLeftSide) goToStory(currentIndex - 1) else goToStory(currentIndex + 1)
                    }
                )
            }
    ) {


        StoryContent(
            stories,
            currentIndex,
            userStoryGroup
        )


        // ---- Top overlay: progress bars + header ----
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPaddingCompat()
                .padding(horizontal = 8.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            StoryProgressRow(progressList = progressList)
            Spacer(Modifier.height(10.dp))
            UserHeader(
                userStoryGroup,
                onMessage = onMessage,
                onCall = onCall
            )
        }


        // ---- Emoji / text overlays for this story, drawn above the content ----
        currentStory?.overlays?.let {
            Log.d("fkbnjfnbf", it.toString())
            StoryOverlaysLayer(overlays = it)
        }
        /*
        Box(modifier = Modifier.fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color.Black.copy(0.3f),Color.Transparent,Color.Transparent,Color.Black.copy(0.3f),))))
*/

    }
}

@Composable
fun BoxScope.StoryContent(
    stories: List<StoriesListResponse.UserStoryGroup.Story?>,
    currentIndex: Int,
    userStoryGroup: StoriesListResponse.UserStoryGroup?
) {
    // ---- Dummy story content (swap this for Image/Video later) ----

    val currentStory = stories.getOrNull(currentIndex)
    Log.d("knbkfnbf", currentStory.toString())
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        if (currentStory?.mediaType == "image") {
            AsyncImageWithLoader(
                image = currentStory.url,
                modifier = Modifier.fillMaxSize(),
                placeholder = null
            )
        } else {
            Text(
                text = userStoryGroup?.user?.fullName ?: "",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Video content: ${currentStory?.index.toString()}",
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = "${currentIndex + 1} / ${stories.size}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        }
    }
}

/** Row of tab-bars — count always matches the current user's story count. */
@Composable
fun StoryProgressRow(progressList: List<Float>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        progressList.forEach { progress ->
            StoryBar(progress = progress, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun StoryBar(progress: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(2.5.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.35f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .background(Color.White, RoundedCornerShape(50))
        )
    }
}

@Composable
fun UserHeader(
    userStoryGroup: StoriesListResponse.UserStoryGroup,
    onCall: (CallType) -> Unit,
    onMessage: () -> Unit
) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileImage(
            image = userStoryGroup.user?.profilePic,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Blue)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = userStoryGroup.user?.fullName ?: "",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier) {

            Image(
                painterResource(R.drawable.outline_more_vert_24),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = null,
                modifier = Modifier.clickable {
                    isExpanded = true
                })
            DropdownMenu(
                shape = Shape_20,
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                containerColor = Color.Black.copy(0.4f),
                modifier = Modifier
                    .dropShadow(
                        Shape_15,
                        shadow = Shadow(
                            color = theme_color_1.copy(alpha = 0.6f),
                            radius = 10.dp,
                            offset = DpOffset(x = 5.dp, y = 5.dp)
                        ),
                    )
                    .width(130.dp)
                    .background(Color.Black.copy(0.4f), shape = Shape_20)
                    .padding(horizontal = 12.dp, vertical = 20.dp)
            )
            {
                //Messages
                Row(
                    modifier = Modifier.padding(top = 5.dp)
                        .clip(Shape_15)
                        .clickableDebAnim{
                            onMessage()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(painterResource(R.drawable.message_icon), contentDescription = null)
                    Text("Message", color = Color.White)
                }
                //Voice Call
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(Shape_15)
                        .clickableDebAnim {
                            onCall(CallType.Audio)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(painterResource(R.drawable.voice_call), contentDescription = null)
                    Text("Audio Call", color = Color.White)
                }
                //Video Call
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(Shape_15)
                        .clickableDebAnim {
                            onCall(CallType.Video)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(painterResource(R.drawable.video_call), contentDescription = null)
                    Text("Video Call", color = Color.White)
                }
                //Hide
             /*   Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(painterResource(R.drawable.eye_close), contentDescription = null)
                    Text("Hide", color = Color.White)
                }*/
            }
        }
    }
}

/**
 * Tiny helper so this file compiles without pulling in the
 * accompanist/system-ui-controller dependency. Replace with
 * Modifier.statusBarsPadding() (androidx.compose.foundation.layout)
 * if you already have that import available in your project.
 */
@Composable
private fun Modifier.statusBarsPaddingCompat(): Modifier = this.padding(top = 24.dp)
