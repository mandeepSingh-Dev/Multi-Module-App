package com.app.feature_story.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.app.core_navigation.StoriesScreens
import com.app.feature_story.ui.post2.StoryCameraScreen
import com.app.feature_story.ui.story_view.StoryViewScreen

fun EntryProviderScope<NavKey>.storiesGraph() {

    entry<StoriesScreens.StoryViewScreen> {
        StoryViewScreen()
    }
    entry<StoriesScreens.StoryPostScreen> {
        StoryCameraScreen()
    }
}
