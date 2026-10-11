package com.app.feature_story.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.fromJson
import com.app.core_ui.components.PROGRESS_DELAY
import com.app.core_ui.components.PROGRESS_FACTOR
import com.app.core_ui.components.StoryProgressItem
import com.app.feature_story.domain.StoriesAllUseCases
import com.app.feature_story.remote.model.response.StoriesListResponse
import com.app.feature_story.ui.states.StoriesScreenState
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@HiltViewModel
class StoryScreenViewModel @Inject constructor(private val storiesAllUseCases: StoriesAllUseCases) :
    BaseViewModel<StoriesScreenState>(StoriesScreenState()) {


    var job: Job? = null
    private val _storiesProgressBarsList =
        MutableStateFlow<MutableList<StoryProgressItem?>>(mutableListOf())
    val storiesProgressBarsList = _storiesProgressBarsList.asStateFlow()

    val _currentProgressIndex = MutableStateFlow(0)
    val currentProgressIndex = _currentProgressIndex.asStateFlow()

    init {
        getStoriesList()
    }

    fun setUpAllProgressBar(counts: Int) {
        Log.d("dvkndjknd", counts.toString())
        if (counts == 0) return

        _storiesProgressBarsList.value = mutableListOf()

        repeat(counts) { index ->
            _storiesProgressBarsList.value = _storiesProgressBarsList.value.toMutableList().apply {
                add(
                    index,
                    StoryProgressItem(
                        0f,
                        index = index
                    )
                )
            }
        }
    }

    fun startSelectedIndex(
        selectedIndex: Int,
        wasHold: Boolean = false,
        id: String?
    ): Job? {
        Log.d("fkbnku8473nbkf", selectedIndex.toString())
        job?.cancel()
        job = viewModelScope.launch {
            _currentProgressIndex.value = selectedIndex

            updateProgressIndexInList(selectedIndex, storyUserId = id)

            storiesProgressBarsList.value.forEachIndexed { index, item ->
                _storiesProgressBarsList.value =
                    _storiesProgressBarsList.value.toMutableList().apply {
                        val item = getOrNull(index)
                        val progress = when {
                            index == selectedIndex -> {
                                if (wasHold) item?.progress else 0f
                            }

                            index < selectedIndex -> {
                                1f
                            }

                            else -> {
                                0f
                            }
                        } ?: 0f

                        val uItem = item?.copy(progress = progress)
                        set(index, uItem)
                    }
            }
            runningCurrentIndex(selectedIndex)
        }
        return job

    }

    fun updateProgressIndexInList(
        progressIndex: Int = 0,
        storyUserId: String?
    ) {
        updateState { state ->
            val mList = state.storiesList?.toMutableList()
            val index = mList?.indexOfFirst {
                it?.user?.id == storyUserId
            } ?: -1
            if(index != -1) {
                val item = mList?.getOrNull(index)
                mList?.set(index, item?.copy(currentStoryProgressIndex = progressIndex))
                state.copy(storiesList = mList)
            }else{
                state
            }
        }
    }

    fun holdCurrentProgressBar(selectedIndex: Int): Job? {

        job?.cancel()
        job = viewModelScope.launch {
            storiesProgressBarsList.value.forEachIndexed { index, item ->
                _storiesProgressBarsList.value =
                    _storiesProgressBarsList.value.toMutableList().apply {
                        val item = getOrNull(index)
                        val progress = when {
                            index < selectedIndex -> {
                                1f
                            }

                            index == selectedIndex -> {
                                item?.progress
                            }

                            else -> {
                                0f
                            }
                        } ?: 0f

                        val uItem = item?.copy(progress = progress)
                        set(index, uItem)
                    }
            }
        }
        return job
    }

//    fun releaseCurrentProgressBar(selectedIndex : Int) {
//        job?.cancel()
//        job = viewModelScope.launch {
//            storiesProgressBarsList.value.forEachIndexed { index, item ->
//                _storiesProgressBarsList.value =
//                    _storiesProgressBarsList.value.toMutableList().apply {
//                        val item = getOrNull(index)
//                        val progress = when {
//                            index < selectedIndex -> {
//                                1f
//                            }
//                            index == selectedIndex -> {
//                                item?.progress
//                            }
//                            else -> {
//                                0f
//                            }
//                        } ?: 0f
//
//                        val uItem = item?.copy(progress = progress)
//                        set(index, uItem)
//                    }
//            }
//        }
//        return job
//
//    }


    suspend fun runningCurrentIndex(index: Int) = coroutineScope {

        val currentItem = storiesProgressBarsList.value.getOrNull(index)
        Log.d("fbnfbnf", currentItem.toString())
        var progress = currentItem?.progress ?: 0f
        while ((progress ?: 0f) <= 1f) {
            delay(PROGRESS_DELAY.milliseconds)
            progress += PROGRESS_FACTOR
            _storiesProgressBarsList.value = storiesProgressBarsList.value.toMutableList().apply {
                Log.d("fkbnjkfnbf", progress.coerceAtMost(1f).toString())
                val uItem = currentItem?.copy(progress = progress.coerceAtMost(1f))
                set(index = index, uItem)
            }
        }
    }

//    fun start

    fun getStoriesList(isRefresh: Boolean = false) {

        storiesAllUseCases.getStoriesUseCase().onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(
                            isStoriesLoading = (uiState.value?.storiesList == null && !isRefresh)
                        )
                    }
                },
                onSuccess = {
                    updateState { state ->

                        val res = json.fromJson<StoriesListResponse>()

                        Log.d("fbknfkbnf", res?.data.toString())

                        state.copy(
                            isStoriesLoading = false,
                            storiesList = res?.data
                        )
                    }
                },
                onError = {
                    updateState { state ->
                        state.copy(
                            isStoriesLoading = false,
                        )
                    }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }


}

/*
LaunchedEffect(pagerState.currentPage) {
//        repeat(count){
    job?.cancel()
    job = coroutineScope.launch {
        var progress = 0f
        val currentItem = mutableList[currentIndex]

        while(progress <= 1f){
            delay(PROGRESS_DELAY.milliseconds)
            progress += PROGRESS_FACTOR
            val uItem = currentItem.copy(progress = progress)
            mutableList.set(index = currentIndex, uItem)
        }
    }
    job.join()
//        }
}
*/

val json = "{\n" +
        "  \"status\": 1,\n" +
        "  \"message\": \"Stories fetched successfully\",\n" +
        "  \"data\": [\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cce1\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_01.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": [\n" +
        "            {\n" +
        "              \"y\": 0.829420976235833,\n" +
        "              \"rotation\": 0,\n" +
        "              \"value\": \"Car For Sale Now\",\n" +
        "              \"scale\": 1,\n" +
        "              \"x\": 0.4830364586439448,\n" +
        "              \"color\": \"#F23E24\",\n" +
        "              \"type\": \"text\"\n" +
        "            },\n" +
        "            {\n" +
        "              \"value\": \"\uD83D\uDE0D\",\n" +
        "              \"x\": 0.9257845502469982,\n" +
        "              \"rotation\": 0,\n" +
        "              \"y\": 0.8309859154929577,\n" +
        "              \"scale\": 1,\n" +
        "              \"type\": \"emoji\"\n" +
        "            }\n" +
        "          ]\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cce2\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_02.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cce3\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_03.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cce4\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_04.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cce5\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_05.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47cce1\",\n" +
        "        \"fullName\": \"User 1\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_01.jpg\",\n" +
        "        \"rating\": 4.5,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47ccf1\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_11.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47ccf2\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_12.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf2\",\n" +
        "        \"fullName\": \"User 2\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_02.jpg\",\n" +
        "        \"rating\": 4.2,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cd01\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_21.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cd02\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_22.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cd03\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_23.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cd04\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_24.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cd05\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_25.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf3\",\n" +
        "        \"fullName\": \"User 3\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_03.jpg\",\n" +
        "        \"rating\": 4.7,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47ce01\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_31.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47ce02\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_32.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47ce03\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_33.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47ce04\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_34.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf4\",\n" +
        "        \"fullName\": \"User 4\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_04.jpg\",\n" +
        "        \"rating\": 4.4,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47cf01\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_41.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf5\",\n" +
        "        \"fullName\": \"User 5\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_05.jpg\",\n" +
        "        \"rating\": 4.1,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d001\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_51.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d002\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_52.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d003\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_53.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d004\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_54.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d005\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_55.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf6\",\n" +
        "        \"fullName\": \"User 6\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_06.jpg\",\n" +
        "        \"rating\": 4.8,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d101\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_61.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d102\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_62.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d103\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_63.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d104\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_64.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d105\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_65.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf7\",\n" +
        "        \"fullName\": \"User 7\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_07.jpg\",\n" +
        "        \"rating\": 4.6,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d201\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_71.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d202\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_72.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d203\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_73.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d204\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_74.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d205\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_75.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf8\",\n" +
        "        \"fullName\": \"User 8\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_08.jpg\",\n" +
        "        \"rating\": 4.3,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d301\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_81.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d302\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_82.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d303\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_83.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d304\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_84.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d305\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_85.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47ccf9\",\n" +
        "        \"fullName\": \"User 9\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_09.jpg\",\n" +
        "        \"rating\": 4.9,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    },\n" +
        "    {\n" +
        "      \"stories\": [\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d401\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_91.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T07:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T07:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d402\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_92.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T08:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T08:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d403\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_93.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T09:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T09:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d404\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_94.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T10:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T10:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        },\n" +
        "        {\n" +
        "          \"_id\": \"6aa8ed4f63ecf5c85c47d405\",\n" +
        "          \"url\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Stories/story_95.jpg\",\n" +
        "          \"mediaType\": \"image\",\n" +
        "          \"expiresAt\": \"2026-09-16T11:01:35.557Z\",\n" +
        "          \"createdAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "          \"overlays\": []\n" +
        "        }\n" +
        "      ],\n" +
        "      \"latestAt\": \"2026-09-15T11:01:35.562Z\",\n" +
        "      \"user\": {\n" +
        "        \"_id\": \"6aa8ecd663ecf5c85c47cd10\",\n" +
        "        \"fullName\": \"User 10\",\n" +
        "        \"profilePic\": \"https://appsinvo-stag-dev.s3.ap-south-1.amazonaws.com/uaeassist/Profile%20Pic/profile_10.jpg\",\n" +
        "        \"rating\": 4.5,\n" +
        "        \"chatId\": null\n" +
        "      }\n" +
        "    }\n" +
        "  ],\n" +
        "  \"count\": 10\n" +
        "}"
