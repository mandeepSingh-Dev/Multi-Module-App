package com.app.feature_wallet.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.enums.ChatListType
import com.app.feature_wallet.domain.WalletAllUseCases
import com.app.feature_wallet.domain.pagination.TransactionsListDataSource
import com.app.feature_wallet.domain.repository.WalletRepository
import com.app.feature_wallet.remote.model.response.TransactionsListResponse
import com.app.feature_wallet.ui.screens.TransactionTypes
import com.app.feature_wallet.ui.states.TransactionHistoryUIState
import com.uae.core_network.networkUtils.getCallbacks
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TransactionsHistoryViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val walletAllUseCases: WalletAllUseCases,
): BaseViewModel<TransactionHistoryUIState>(
    TransactionHistoryUIState()
){

    init {
        getWallet()
    }
    private val _search = MutableStateFlow<String?>("")
    val search = _search.asStateFlow()

    private val _filter = MutableStateFlow<String?>(ChatListType.ALL.value)
    val filter = _filter.asStateFlow()


    private val _isFirstTimeApiHit = MutableStateFlow(false)
    val isFirstTimeApiHit = _isFirstTimeApiHit.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val transactionsList = combine(
        search.debounce(500.milliseconds).distinctUntilChanged().filterNotNull(),
        filter/*.debounce(if (filter.value.isNullOrEmpty()) 0.milliseconds else 500.milliseconds)*/
    ) { search, filter ->
        search to filter
    }.flatMapLatest {
        getTransactionsListt()
    }.cachedIn(viewModelScope)

    fun updateSearch(search: String?) {
        _search.value = search
    }

    fun updateFilter(filter: String?) {
        _filter.value = filter
    }
    fun getTransactionsListt(): Flow<PagingData<TransactionsListResponse.TransactionData>> {
        _isFirstTimeApiHit.value = true
        return Pager(
            config = PagingConfig(
                initialLoadSize = 10,
                pageSize = 10,
            ),
            pagingSourceFactory = {
                TransactionsListDataSource(
                    fromDate = uiState.value?.fromDate,
                    endDate = uiState.value?.toDate,
                    type = uiState.value?.transactionType?.takeIf { it != TransactionTypes.ALL_TYPES.type }?.lowercase(),
                    walletRepository = walletRepository
                )
            }
        ).flow
    }





    fun getWallet(isRefreshing: Boolean = false) {
        walletAllUseCases.GetWalletUseCase().onEach { networkResult ->

            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(
                            isLoading = !isRefreshing,
                            isRefreshing = isRefreshing
                        )
                    }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isLoading = false, isRefreshing = false,
                            walletData = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state -> state.copy(isLoading = false, isRefreshing = false) }
                    errorEvent(it.error)
                }
            )
        }.launchIn(viewModelScope)
    }
}