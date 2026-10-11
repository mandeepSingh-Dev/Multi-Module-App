package com.app.feature_review.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.feature_review.domain.repository.ReviewRepository
import com.app.feature_review.remote.model.response.ReviewsListResponse
import com.app.feature_review.remote.model.response.ReviewsListResponse.ReviewData.Summary
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.first

class ReviewListDataSource(
    private val reviewRepository: ReviewRepository,
    private val onGetSummary : (Summary?) -> Unit,
) : PagingSource<Int, ReviewsListResponse.ReviewData.ReviewDataItem>() {

    override fun getRefreshKey(state: PagingState<Int, ReviewsListResponse.ReviewData.ReviewDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewsListResponse.ReviewData.ReviewDataItem> {
        val nextPage = params.key ?: 1


        val result = reviewRepository.getReviewsList(
            page = nextPage, limit = params.loadSize,
        ).first()



        return when (result) {
            is NetworkResult.Success -> {
                 val reviewsList = result.data?.data?.items ?: emptyList()
                onGetSummary(result.data?.data?.summary)

                val isLastPage = reviewsList.isEmpty() ?: false

                LoadResult.Page(
                    data = reviewsList,
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