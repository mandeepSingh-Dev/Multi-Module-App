package com.app.feature_home.ui.states

import com.app.core_auth.model.response.ProfileResponse
import com.app.core_common.base.UIState
import com.app.core_story.remote.model.response.StoriesListResponse
import com.app.feature_home.remote.model.response.HomeDataUserResponse
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_wallet.remote.model.response.WalletResponse

data class HomeUserScreenState(
    val isLoading: Boolean = false,
    val isBlockLoading: Boolean = false,
    val profileData: ProfileResponse.UserProfileData? = null,
    val isStoriesLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isCitiesLoading: Boolean = false,
    val storiesList: List<StoriesListResponse.UserStoryGroup?>? = null,
    val onlineNow: List<HostData?>? = null,
    val recomended: List<HostData?>? = null,
    val homeUserData: HomeDataUserResponse.HomeDataUser? = null,
    val isWalletLoading: Boolean = false,
    val walletData: WalletResponse.WalletData? = null,
) : UIState