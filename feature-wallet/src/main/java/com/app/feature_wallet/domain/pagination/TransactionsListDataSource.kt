package com.app.feature_wallet.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_wallet.domain.repository.WalletRepository
import com.app.feature_wallet.remote.model.response.TransactionsListResponse
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class TransactionsListDataSource(
    private val walletRepository: WalletRepository,
    private val fromDate: String?,
    private val endDate: String?,
    private val type: String?,
) : PagingSource<Int, TransactionsListResponse.TransactionData>() {

    override fun getRefreshKey(state: PagingState<Int, TransactionsListResponse.TransactionData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionsListResponse.TransactionData> {
        val nextPage = params.key ?: 1

        val result = walletRepository.getTransactions(
            page = nextPage, limit = params.loadSize,
            fromDate = fromDate,
            toDate = endDate,
            type = type,
        ).first()



        return when (result) {
            is NetworkResult.Success -> {
                val transactionsList = result.data?.data ?: emptyList()
                val isLastPage = transactionsList.isEmpty() ?: false

                LoadResult.Page(
                    data = transactionsList,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (isLastPage) null else nextPage + 1
                )
            }

            is NetworkResult.Error -> {
                LoadResult.Error(Exception(result.error ?: "Unknown Error"))
            }

            else -> {
                LoadResult.Error(Exception("Unexpected result type"))
            }
        }
    }
}