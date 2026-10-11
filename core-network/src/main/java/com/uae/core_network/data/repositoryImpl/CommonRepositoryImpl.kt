package com.uae.core_network.data.repositoryImpl

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
import com.uae.core_network.data.service.CommonService
import com.uae.core_network.domain.repository.CommonRepository
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    val networkHelper: NetworkHelper,
    val commonService: CommonService
) : CommonRepository {


    override fun uploadImage(file : MultipartBody.Part, folder : RequestBody): Flow<NetworkResult<ImageUploadResponse>> {
        return try {
            networkHelper.executeWithFlow<ImageUploadResponse>(
                call = {
                    commonService.uploadImage(files = file,folderName = folder)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }



    override fun getInterests(): Flow<NetworkResult<InterestResponse>> {
        return try {
            networkHelper.executeWithFlow<InterestResponse>(
                call = {
                    commonService.getInterests()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun getCities(search : String?): Flow<NetworkResult<CitiesResponse>> {
        return try {
            networkHelper.executeWithFlow<CitiesResponse>(
                call = {
                    commonService.getCities(search = search)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }


    override fun getLanguages(): Flow<NetworkResult<LanguageListResponse>> {
        return try {
            networkHelper.executeWithFlow<LanguageListResponse>(
                call = {
                    commonService.getLanguages()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun review(reviewRequestBody: ReviewRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    commonService.review(reviewRequestBody = reviewRequestBody)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getUnreadNotification(): Flow<NetworkResult<UnreadNotificationResponse>> {
        return try {
            networkHelper.executeWithFlow<UnreadNotificationResponse>(
                call = {
                    commonService.getUnreadNotification()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }


    override fun getCms(
        type : Int,
        role : Int
    ): Flow<NetworkResult<CMSResponse>> {
        return try {
            networkHelper.executeWithFlow<CMSResponse>(
                call = {
                    commonService.getCms(
                        type = type,
                        role = role
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getFaqs(): Flow<NetworkResult<FaqsResponse>> {
        return try {
            networkHelper.executeWithFlow<FaqsResponse>(
                call = {
                    commonService.getFaqs()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }


    override fun uploadFCMToken(fcmToken : String?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            val body = hashMapOf(ApiConstants.fcmToken to fcmToken)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    commonService.uploadFCMToken(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun favouriteHost(id: String?): Flow<NetworkResult<FavouriteUnFavResponse>> {

        val body = hashMapOf(ApiConstants.id to id)
        return try {
            networkHelper.executeWithFlow<FavouriteUnFavResponse>(
                call = {
                    commonService.favouriteHost(body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun getFavouriteHost(page : Int, limit : Int): Flow<NetworkResult<FavouriteHostsListResponse>> {

        return try {
            networkHelper.executeWithResponseManipulate<FavouriteHostsListResponse>(
                call = {
                    commonService.getFavouriteHost(
                        page = page,
                        limit = limit
                    )
                },
                onSuccess = { response ->
                    response
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}