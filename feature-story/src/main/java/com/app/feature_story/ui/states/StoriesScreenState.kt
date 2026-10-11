package com.app.feature_story.ui.states

import com.app.core_common.base.UIState
import com.app.feature_story.remote.model.response.StoriesListResponse
import com.app.feature_story.remote.model.response.StoriesListResponse.StoryData

data class StoriesScreenState(
    val isStoriesLoading : Boolean = false,
    val storiesList : List<StoryData?>? = null
) : UIState