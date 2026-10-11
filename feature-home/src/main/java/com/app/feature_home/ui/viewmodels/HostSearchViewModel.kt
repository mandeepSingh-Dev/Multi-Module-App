package com.app.feature_home.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.ItemSnapshotList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.app.core_common.base.BaseViewModel
import com.app.feature_block.domain.BlockAllUseCases
import com.app.feature_home.domain.pagination.HostListDataSource
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.states.HostSearchUIState
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@HiltViewModel
class HostSearchViewModel @Inject constructor(
    private val homeUserRepository: HomeUserRepository,
    private val commonAllUseCases: CommonAllUseCases,
    private val blockAllUseCases: BlockAllUseCases,
) : BaseViewModel<HostSearchUIState>(HostSearchUIState()) {

    private val _search = MutableStateFlow<String?>("")
    val search = _search.asStateFlow()

    val _manualTrigger = MutableSharedFlow<Boolean?>()
    val manualTrigger = _manualTrigger.asSharedFlow()

    private val _isFirstTimeApiHit = MutableStateFlow(false)
    val isFirstTimeApiHit = _isFirstTimeApiHit.asStateFlow()

    val hostListPagingStateFlow = MutableStateFlow<PagingData<HostData>>(PagingData.empty())

    init {
        getAllInterests()
        getCities()


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val hostsListPagingJob = combine(
        search.debounce(350.milliseconds).distinctUntilChanged().filterNotNull(),
        manualTrigger.filterNotNull()/*.debounce(if (filter.value.isNullOrEmpty()) 0.milliseconds else 500.milliseconds)*/
    ) { search, filter ->
        search to filter
    }.flatMapLatest { pair ->
        _isFirstTimeApiHit.value = true
        getHostList(pair.first)
    }.cachedIn(viewModelScope)
        .onEach { pagingData ->
            hostListPagingStateFlow.value = pagingData
        }.launchIn(viewModelScope)


    fun triggerFilter(isApplied: Boolean) = viewModelScope.launch {
        _manualTrigger.emit(isApplied)
    }

    fun getHostList(search: String): Flow<PagingData<HostData>> {
        _isFirstTimeApiHit.value = true
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                HostListDataSource(
                    search = search.ifBlank { null },
                    filterState = uiState.value?.filterState,
                    homeUserRepository = homeUserRepository
                )
            }
        ).flow
    }

    fun updateSearch(search: String?) {
        _search.value = search
    }

    fun changeOnlineNowCallControllerVisibility(
        listPaging: LazyPagingItems<HostData>,
        item: HostData?
    ) {
        if (item == null) return
        val mList = listPaging.itemSnapshotList.items.toMutableList()

        val index = mList.indexOfFirst { it.id == item.id } ?: -1
        if (index != -1) {
            mList[index] = item.copy(isCallControllerViewOpen = !item.isCallControllerViewOpen)
        }

        hostListPagingStateFlow.value = PagingData.from(mList)
    }


    fun changeOnlineNowMenuVisibility(snapshotList: ItemSnapshotList<HostData>, item: HostData?) {
        if (item == null) return
        val mList = snapshotList.items.toMutableList()

        val index = mList.indexOfFirst { it.id == item.id } ?: -1
        if (index != -1) {
            mList[index] = item.copy(isMenuOpen = !item.isMenuOpen)
        }

        hostListPagingStateFlow.value = PagingData.from(mList)
    }


    fun getAllInterests() {
        commonAllUseCases.getInterestsUseCase().onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {
                    updateState { state ->
                        state.copy(filterState = state.filterState?.copy(interests = networkResult.data?.data))
                    }
                }

                is NetworkResult.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getCities() {
        commonAllUseCases.getCitiesUseCase(search = "").onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isCitiesLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isCitiesLoading = false,
                            citiesList = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state -> state.copy(isCitiesLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }


    fun block(id: String?) {
        blockAllUseCases.blockUnblockUseCase(id = id).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
                    removeFromOnlineNowList(id = id)
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }

    fun removeFromOnlineNowList(id: String?) {

//        updateState { state ->
//            val mListOnlineNow = state.onlineNow?.toMutableList()
//            val mListRecommended = state.recomended?.toMutableList()
//            mListOnlineNow?.removeIf { it?.id == id }
//            mListRecommended?.removeIf { it?.id == id }
//            state.copy(onlineNow = mListOnlineNow, recomended = mListRecommended)
//        }

    }


}