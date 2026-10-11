package com.uae.core_network.domain.repository

import com.uae.core_network.data.model.CMSResponse
import com.uae.core_network.data.model.CitiesResponse
import com.uae.core_network.data.model.FaqsResponse
import com.uae.core_network.data.model.FavouriteHostsListResponse
import com.uae.core_network.data.model.FavouriteUnFavResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.InterestResponse
import com.uae.core_network.data.model.LanguageListResponse
 import com.uae.core_network.data.model.UnreadNotificationResponse
import com.uae.core_network.data.model.requestBody.ReviewRequestBody
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CommonRepository {
    fun uploadImage(
        file: MultipartBody.Part,
        folder: RequestBody
    ): Flow<NetworkResult<ImageUploadResponse>>

    fun getInterests(): Flow<NetworkResult<InterestResponse>>
    fun getLanguages(): Flow<NetworkResult<LanguageListResponse>>
    fun getCities(search: String?): Flow<NetworkResult<CitiesResponse>>

    fun review(reviewRequestBody: ReviewRequestBody?): Flow<NetworkResult<ApiResponse2>>
    fun getUnreadNotification(): Flow<NetworkResult<UnreadNotificationResponse>>
    fun getCms(type: Int, role: Int): Flow<NetworkResult<CMSResponse>>
    fun getFaqs(): Flow<NetworkResult<FaqsResponse>>
    fun uploadFCMToken(fcmToken: String?): Flow<NetworkResult<ApiResponse2>>
    fun favouriteHost(id: String?): Flow<NetworkResult<FavouriteUnFavResponse>>
    fun getFavouriteHost(page: Int, limit: Int): Flow<NetworkResult<FavouriteHostsListResponse>>
}