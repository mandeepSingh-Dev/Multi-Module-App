package com.uae.core_network.domain.CommonUseCases

import android.content.Context
import androidx.core.net.toUri
import com.app.core_common.R
import com.uae.core_network.data.model.CMSResponse
import com.uae.core_network.data.model.CitiesResponse
import com.uae.core_network.data.model.FaqsResponse
import com.uae.core_network.data.model.FavouriteUnFavResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.InterestResponse
import com.uae.core_network.data.model.LanguageListResponse
import com.uae.core_network.data.model.UnreadNotificationResponse
import com.uae.core_network.data.model.requestBody.ReviewRequestBody
import com.uae.core_network.domain.repository.CommonRepository
import com.uae.core_network.extensions.toMultipartBodyPart
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class CommonAllUseCases @Inject constructor(
    val imageUploadUseCase: ImageUploadUseCase,
    val getInterestsUseCase: GetInterestsUseCase,
    val getLanguagesUseCase: GetLanguagesUseCase,
    val getCitiesUseCase: GetCitiesUseCase,
    val reviewUseCase: ReviewUseCase,
    val getCmsUseCase: GetCmsUseCase,
    val getFaqsResponse: GetFaqsResponse,
    val getUnreadNotificationUseCase : GetUnreadNotificationUseCase,
    val uploadFCMTokenResponse : UploadFCMTokenResponse,
    val favouriteHostUseCase: FavouriteHostUseCase,
)

class GetInterestsUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(): Flow<NetworkResult<InterestResponse>> {
        return commonRepository.getInterests()
    }
}

class GetLanguagesUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(): Flow<NetworkResult<LanguageListResponse>> {
        return commonRepository.getLanguages()
    }
}

class GetCitiesUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(search: String?): Flow<NetworkResult<CitiesResponse>> {
        return commonRepository.getCities(search = search)
    }
}
class GetUnreadNotificationUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(): Flow<NetworkResult<UnreadNotificationResponse>> {
        return commonRepository.getUnreadNotification()
    }
}

class GetCmsUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(type : Int, role : Int): Flow<NetworkResult<CMSResponse>> {
        return commonRepository.getCms(
            type = type,
            role = role
        )
    }
}

class GetFaqsResponse @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(): Flow<NetworkResult<FaqsResponse>> {
        return commonRepository.getFaqs()
    }
}
class UploadFCMTokenResponse @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(fcmToken : String?): Flow<NetworkResult<ApiResponse2>> {
        return commonRepository.uploadFCMToken(fcmToken = fcmToken)
    }
}

class FavouriteHostUseCase @Inject constructor(private val homeUserRepository: CommonRepository) {
    operator fun invoke(id : String?): Flow<NetworkResult<FavouriteUnFavResponse>> {
        return homeUserRepository.favouriteHost(
            id = id
        )
    }
}


class ReviewUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    operator fun invoke(body: ReviewRequestBody?): Flow<NetworkResult<ApiResponse2>> {

        return if (body?.rating == null || body.rating == 0) {
            flowOf(NetworkResult.Error("Please select rating."))
        } else if (body.comment.isNullOrEmpty()) {
            flowOf(NetworkResult.Error("Please write your comment"))
        } else {
            commonRepository.review(reviewRequestBody = body)
        }
    }
}


class ImageUploadUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val commonRepository: CommonRepository
) {
    operator fun invoke(
        fileUri: String?,
        folderName: String?
    ): Flow<NetworkResult<ImageUploadResponse>> {

        try {
            val fileMultiPartBody = fileUri?.toUri()?.let {
                it.toMultipartBodyPart(context = context)
            }
            return if (fileUri.isNullOrEmpty()) {
                flowOf(NetworkResult.Error(error = "Something went wrong with File Uri"))
            } else {
                commonRepository.uploadImage(
                    file = fileMultiPartBody!!,
                    folder = (folderName
                        ?: context.getString(R.string.app_name)).toRequestBody("text/plain".toMediaType())
                )
            }
        } catch (e: Exception) {
            return flowOf(NetworkResult.Error(error = "Something went wrong: ${e.message}"))
        }
    }
}