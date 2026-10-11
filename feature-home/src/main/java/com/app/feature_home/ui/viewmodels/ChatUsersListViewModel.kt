package com.app.feature_home.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.enums.ChatListType
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.domain.pagination.ChatListDataSource
import com.app.feature_home.remote.model.response.ChatListResponse
import com.app.feature_home.ui.states.ChatListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ChatUsersListViewModel @Inject constructor(
    private val homeUserRepository: HomeUserRepository
) : BaseViewModel<ChatListScreenState>(ChatListScreenState()) {

    private val _search = MutableStateFlow<String?>("")
    val search = _search.asStateFlow()

    private val _filter = MutableStateFlow<String?>(ChatListType.ALL.value)
    val filter = _filter.asStateFlow()


    @OptIn(FlowPreview::class)
    val allChatUsersList = search.debounce(500.milliseconds).distinctUntilChanged().filterNotNull()
        .flatMapLatest {
            getChatUsersList(search = it, filter = "all")
        }.cachedIn(viewModelScope)

    @OptIn(FlowPreview::class)
    val unreadChatUsersList = search.debounce(500.milliseconds).distinctUntilChanged().filterNotNull()
        .flatMapLatest {
            getChatUsersList(search = it, filter = "unread")
        }.cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val chatUsersList = combine(
        search.debounce(500.milliseconds).distinctUntilChanged().filterNotNull(),
        filter/*.debounce(if (filter.value.isNullOrEmpty()) 0.milliseconds else 500.milliseconds)*/
    ) { search, filter ->
        search to filter
    }.flatMapLatest {

        getChatUsersList(search = it.first, filter = it.second)
    }.cachedIn(viewModelScope)

    /*

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
val chatUsersList = search
    .debounce(500.milliseconds)
    .distinctUntilChanged()
    .flatMapLatest {
        getChatUsersList(search = it, filter = null)
    }.cachedIn(viewModelScope)
    .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
*/


    fun updateSearch(search: String?) {
        _search.value = search
    }

    fun updateFilter(filter: String?) {
        _filter.value = filter
    }

    fun getChatUsersList(
        search: String?,
        filter: String?
    ): Flow<PagingData<ChatListResponse.ChatListData.Chat>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                ChatListDataSource(
                    search = search?.ifBlank { null },
                    filter = filter ?: "all",
                    homeUserRepository = homeUserRepository,
                    onGetChatCounts = {counts ->
                        updateState{state ->
                            state.copy(chatCounts  = counts)
                        }
                    }
                )
            }
        ).flow
    }
}