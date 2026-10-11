package com.app.feature_story.ui.viewmodels

import com.app.core_common.base.UIState
import com.app.core_story.remote.model.response.StoriesListResponse

data class StoriesScreenUIState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val stories: List<StoriesListResponse.UserStoryGroup?>? = null
) : UIState