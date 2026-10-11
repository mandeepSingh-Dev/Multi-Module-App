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
import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.paginations.FavouriteHostsDataSource
import com.app.feature_home.remote.model.response.HostData
import com.app.feature_home.ui.states.FavouriteHostScreenState
import com.uae.core_network.domain.repository.CommonRepository
import com.uae.core_network.networkUtils.getCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class FavouriteHostsListViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    private val blockAllUseCases : BlockAllUseCases,
    ): BaseViewModel<FavouriteHostScreenState>(
    FavouriteHostScreenState()
) {

    private val _isFirstTimeApiHit = MutableStateFlow(false)
    val isFirstTimeApiHit = _isFirstTimeApiHit.asStateFlow()


    val favouriteListPagingStateFlow = MutableStateFlow<PagingData<HostData>>(PagingData.empty())

    val favouriteListPaging =  Pager(
        config = PagingConfig(
            initialLoadSize = 10,
            pageSize = 10,
        ),
        pagingSourceFactory = {
            _isFirstTimeApiHit.value = true
            FavouriteHostsDataSource(
                commonRepository = commonRepository,
            )
        }
    ).flow.cachedIn(viewModelScope)
        .onEach {
            favouriteListPagingStateFlow.value = it
        }.launchIn(viewModelScope)


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

        favouriteListPagingStateFlow.value = PagingData.from(mList)
    }


    fun changeOnlineNowMenuVisibility(snapshotList: ItemSnapshotList<HostData>, item: HostData?) {
        if (item == null) return
        val mList = snapshotList.items.toMutableList()

        val index = mList.indexOfFirst { it.id == item.id } ?: -1
        if (index != -1) {
            mList[index] = item.copy(isMenuOpen = !item.isMenuOpen)
        }

        favouriteListPagingStateFlow.value = PagingData.from(mList)
    }
    fun block(id : String?){
        blockAllUseCases.blockUnblockUseCase(id = id).onEach {
            it.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(isLoading = false)
                    }
//                    removeFromOnlineNowList(id = id)
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false) }
                }
            )
        }.launchIn(viewModelScope)
    }


}